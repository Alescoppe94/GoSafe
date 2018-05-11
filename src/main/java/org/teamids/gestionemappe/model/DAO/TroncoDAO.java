package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Tronco;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Timestamp;
import java.util.*;

public class TroncoDAO {

    protected Tronco tabella;

    public TroncoDAO() {
        this.tabella = new Tronco();
    }

    public Set<TroncoEntity> getAllTronchi(){
        Set<TroncoEntity> allTronchiEdificio = new HashSet<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch();
        BeaconDAO beaconDAO = new BeaconDAO();
        for (int i = 0; i<rs.size(); i++) {
                ArrayList<BeaconEntity> estremiOrdinati = new ArrayList<>();
                ArrayList<BeaconEntity> estremiInvertiti = new ArrayList<>();
                estremiOrdinati.add(beaconDAO.getBeaconById(rs.get(i).get("beaconAId").toString()));
                estremiOrdinati.add(beaconDAO.getBeaconById(rs.get(i).get("beaconBId").toString()));
                estremiInvertiti.add(beaconDAO.getBeaconById(rs.get(i).get("beaconBId").toString()));
                estremiInvertiti.add(beaconDAO.getBeaconById(rs.get(i).get("beaconAId").toString()));
                TroncoEntity troncoOrd = new TroncoEntity(
                        Integer.parseInt(rs.get(i).get("id").toString()),
                        Boolean.parseBoolean(rs.get(i).get("agibile").toString()),
                        estremiOrdinati,
                        Float.parseFloat(rs.get(i).get("area").toString())
                );
                TroncoEntity troncoInv = new TroncoEntity(
                        Integer.parseInt(rs.get(i).get("id").toString()),
                        Boolean.parseBoolean(rs.get(i).get("agibile").toString()),
                        estremiInvertiti,
                        Float.parseFloat(rs.get(i).get("area").toString())
                );
                allTronchiEdificio.add(troncoOrd);
                allTronchiEdificio.add(troncoInv);
            }
        return allTronchiEdificio;
    }

    public TroncoEntity getTroncoByBeacons(BeaconEntity beaconA, BeaconEntity beaconB){

        tabella.select();
        tabella.where("beaconAId = '" + beaconA.getId() + "' and beaconBId = '" + beaconB.getId() + "' or beaconAId = '" + beaconB.getId() + "' and beaconBId = '" + beaconA.getId() + "'"  );
        List<Map<String, Object>> rs = tabella.fetch();
        ArrayList<BeaconEntity> estremiTronco = new ArrayList<>();
        estremiTronco.add(beaconA);
        estremiTronco.add(beaconB);
        TroncoEntity tronco = new TroncoEntity(
                Integer.parseInt(rs.get(0).get("id").toString()),
                Boolean.parseBoolean(rs.get(0).get("agibile").toString()),
                estremiTronco,
                Float.parseFloat(rs.get(0).get("area").toString())
        );
        return tronco;
    }

    public TroncoEntity getTroncoById(String troncoId, boolean direzione) {
        tabella.select();
        tabella.where("id = '" + troncoId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        ArrayList<BeaconEntity> estremiTronco = new ArrayList<>();
        BeaconDAO beaconDAO = new BeaconDAO();
        BeaconEntity beaconA;
        BeaconEntity beaconB;
        if(direzione) {
            beaconA = beaconDAO.getBeaconById(rs.get(0).get("beaconAId").toString());
            beaconB = beaconDAO.getBeaconById(rs.get(0).get("beaconBId").toString());
        } else {
            beaconA = beaconDAO.getBeaconById(rs.get(0).get("beaconBId").toString());
            beaconB = beaconDAO.getBeaconById(rs.get(0).get("beaconAId").toString());
        }
        estremiTronco.add(beaconA);
        estremiTronco.add(beaconB);
        TroncoEntity tronco = new TroncoEntity(
                Integer.parseInt(rs.get(0).get("id").toString()),
                Boolean.parseBoolean(rs.get(0).get("agibile").toString()),
                estremiTronco,
                Float.parseFloat(rs.get(0).get("area").toString())
        );
        return tronco;
    }

    public boolean checkDirezioneTronco(TroncoEntity troncoOttimo) {
        boolean success = false;
        tabella.select();
        tabella.where("id='" + troncoOttimo.getId() + "' AND beaconAId = '" + troncoOttimo.getBeaconEstremi().get(0).getId() + "' and beaconBId = '" + troncoOttimo.getBeaconEstremi().get(1).getId() + "'" );
        if(tabella.fetch().size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    public JsonArray getAllTronchiAggiornati(Timestamp timestamp) {
        JsonArrayBuilder tronchiAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            tronchiAggiornati.add(Json.createObjectBuilder()
                                .add("id",rs.get(i).get("id").toString())
                                .add("beaconAId",rs.get(i).get("beaconAId").toString())
                                .add("beaconBId",rs.get(i).get("beaconBId").toString())
                                .add("agibile",rs.get(i).get("agibile").toString())
                                .add("area",rs.get(i).get("area").toString())
                                );
        }
        return tronchiAggiornati.build();
    }

    public JsonArray getTable() {
        JsonArrayBuilder tronchi = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            tronchi.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("beaconAId",rs.get(i).get("beaconAId").toString())
                    .add("beaconBId",rs.get(i).get("beaconBId").toString())
                    .add("agibile",rs.get(i).get("agibile").toString())
                    .add("area",rs.get(i).get("area").toString())
            );
        }
        return tronchi.build();
    }

    public void inserisciTronchi(ArrayList<TroncoEntity> tronchi){
        for(TroncoEntity tronco : tronchi){
            String dati= String.valueOf(tronco.getId());
            dati=dati+",'"+tronco.getBeaconEstremi().get(0)+"'";
            dati=dati+",'"+tronco.getBeaconEstremi().get(1)+"'";
            dati=dati+",'"+tronco.isAgibile()+"'";
            dati=dati+",'"+tronco.getArea()+"'";
            tabella.insert(dati);
            tabella.execute();
        }
    }
    public void eliminaTronchiPerPiano(int pianoId){
        tabella.delete();
        tabella.innerjoin("beacon", "tronco.beaconAId = beacon.id");
        tabella.where("beacon.pianoId = '" + pianoId +"'");
        tabella.execute();
        tabella.delete();
        tabella.innerjoin("beacon", "tronco.beaconBId = beacon.id");
        tabella.where("beacon.pianoId = '" + pianoId +"'");
        tabella.execute();
    }
}
