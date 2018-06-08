package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Beacon;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

public class BeaconDAO {

    protected Beacon tabella;

    public BeaconDAO() {
        this.tabella = new Beacon();
    }

    public BeaconEntity getBeaconById(String beaconId, Connection db){
        tabella.select();
        tabella.where("id = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        BeaconEntity beacon = new BeaconEntity();
        beacon.setId(rs.get(0).get("id").toString());
        beacon.setPiano(Integer.parseInt(rs.get(0).get("pianoId").toString()));
        beacon.setIs_puntodiraccolta(Boolean.parseBoolean(rs.get(0).get("is_puntodiraccolta").toString()));
        beacon.setCoordx(Integer.parseInt(rs.get(0).get("coordx").toString()));
        beacon.setCoordy(Integer.parseInt(rs.get(0).get("coordy").toString()));
        return beacon;
    }

    public ArrayList<BeaconEntity> getAllBeacons(Connection db){

        ArrayList<BeaconEntity> beacons = new ArrayList<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            BeaconEntity beaconDiRaccolta = new BeaconEntity(
                    rs.get(i).get("id").toString(),
                    Boolean.parseBoolean(rs.get(i).get("is_puntodiraccolta").toString()),
                    Integer.parseInt(rs.get(i).get("pianoId").toString()),
                    Integer.parseInt(rs.get(i).get("coordx").toString()),
                    Integer.parseInt(rs.get(i).get("coordy").toString())
            );
            beacons.add(beaconDiRaccolta);
        }
        return beacons;
    }

    public Set<BeaconEntity> getAllPuntiDiRaccolta(Connection db){
        Set<BeaconEntity> allPuntiDiRaccolta = new HashSet<>();
        tabella.select();
        tabella.where("is_puntodiraccolta = '1'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            BeaconEntity beaconDiRaccolta = new BeaconEntity(
                    rs.get(i).get("id").toString(),
                    Boolean.parseBoolean(rs.get(i).get("is_puntodiraccolta").toString()),
                    Integer.parseInt(rs.get(i).get("pianoId").toString()),
                    Integer.parseInt(rs.get(i).get("coordx").toString()),
                    Integer.parseInt(rs.get(i).get("coordy").toString())
            );
            allPuntiDiRaccolta.add(beaconDiRaccolta);
        }
        return allPuntiDiRaccolta;
    }

    public JsonArray getAllBeaconAggiornati(Timestamp timestamp, Connection db) {
        JsonArrayBuilder beaconAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            beaconAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("is_puntodiraccolta",rs.get(i).get("is_puntodiraccolta").toString())
                    .add("pianoId",rs.get(i).get("pianoId").toString())
                    .add("coordx", rs.get(i).get("coordx").toString())
                    .add("coordy", rs.get(i).get("coordy").toString())
            );
        }
        return beaconAggiornati.build();
    }

    public JsonArray getTable(Connection db) {
        JsonArrayBuilder beacon = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            beacon.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("is_puntodiraccolta",rs.get(i).get("is_puntodiraccolta").toString())
                    .add("pianoId",rs.get(i).get("pianoId").toString())
                    .add("coordx", rs.get(i).get("coordx").toString())
                    .add("coordy", rs.get(i).get("coordy").toString())
            );
        }
        return beacon.build();
    }

    public ArrayList<String> inserisciBeacons(ArrayList<BeaconEntity> beacons, int piano_id, Connection db){
        ArrayList<String> beaconDoppi = new ArrayList<>();
        for (BeaconEntity beacon : beacons){
            if(!isBeaconInDb(beacon.getId(), db)) {
                int puntodiraccolta = beacon.isIs_puntodiraccolta() ? 1 : 0;
                String dati = String.valueOf(beacon.getId());
                dati = dati + ",'" + puntodiraccolta + "'";
                dati = dati + ",'" + piano_id + "'";
                dati = dati + ",'" + beacon.getCoordx() + "'";
                dati = dati + ",'" + beacon.getCoordy() + "'";
                dati = dati + ",null";
                tabella.insert(dati);
                tabella.execute(db);
            } else {
                beaconDoppi.add(beacon.getId());
            }
        }
        return beaconDoppi;
    }

    public void eliminaBeaconsPerPiano(int pianoId, Connection db){ //TODO: sistemare
        tabella.delete();
        tabella.where("pianoId = '" + pianoId + "'");
        tabella.execute(db);
    }

    public boolean isBeaconInDb(String idbeacon, Connection db){
        boolean success = false;
        tabella.select();
        tabella.where("id ='" + idbeacon + "'");
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }
}
