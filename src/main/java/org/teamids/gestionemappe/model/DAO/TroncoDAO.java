package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DbTable.Tronco;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import java.sql.ResultSet;
import java.util.*;

public class TroncoDAO {

    protected Tronco tabella;

    public TroncoDAO() {
        this.tabella = new Tronco();
    }

    public void updateLos(int troncoId,float los){
        String dati= "los = " + los;
        tabella.update(dati);
        tabella.where("id ='" + troncoId + "'");
        tabella.execute();
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
                        Float.parseFloat(rs.get(i).get("costo").toString()),
                        estremiOrdinati,
                        Float.parseFloat(rs.get(i).get("lunghezza").toString()),
                        Float.parseFloat(rs.get(i).get("los").toString()),
                        Float.parseFloat(rs.get(i).get("area").toString())
                );
                TroncoEntity troncoInv = new TroncoEntity(
                        Integer.parseInt(rs.get(i).get("id").toString()),
                        Boolean.parseBoolean(rs.get(i).get("agibile").toString()),
                        Float.parseFloat(rs.get(i).get("costo").toString()),
                        estremiInvertiti,
                        Float.parseFloat(rs.get(i).get("lunghezza").toString()),
                        Float.parseFloat(rs.get(i).get("los").toString()),
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
                Float.parseFloat(rs.get(0).get("costo").toString()),
                estremiTronco,
                Float.parseFloat(rs.get(0).get("lunghezza").toString()),
                Float.parseFloat(rs.get(0).get("los").toString()),
                Float.parseFloat(rs.get(0).get("area").toString())
        );
        return tronco;
    }

    public TroncoEntity getTroncoById(String troncoId) {
        tabella.select();
        tabella.where("id = '" + troncoId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        ArrayList<BeaconEntity> estremiTronco = new ArrayList<>();
        BeaconDAO beaconDAO = new BeaconDAO();
        BeaconEntity beaconA = beaconDAO.getBeaconById(rs.get(0).get("beaconAId").toString());
        BeaconEntity beaconB = beaconDAO.getBeaconById(rs.get(0).get("beaconBId").toString());
        estremiTronco.add(beaconA);
        estremiTronco.add(beaconB);
        TroncoEntity tronco = new TroncoEntity(
                Integer.parseInt(rs.get(0).get("id").toString()),
                Boolean.parseBoolean(rs.get(0).get("agibile").toString()),
                Float.parseFloat(rs.get(0).get("costo").toString()),
                estremiTronco,
                Float.parseFloat(rs.get(0).get("lunghezza").toString()),
                Float.parseFloat(rs.get(0).get("los").toString()),
                Float.parseFloat(rs.get(0).get("area").toString())
        );
        return tronco;
    }

    public void losToDefault() {
        tabella.update("los = 0");
        tabella.execute();
    }
}
