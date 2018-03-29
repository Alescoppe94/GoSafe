package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DbTable.Beacon;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import java.sql.ResultSet;
import java.util.*;

public class BeaconDAO {

    protected static Beacon tabella = new Beacon();

    public BeaconDAO() {
    }

    /*public static void updateBeacon(BeaconEntity beacon){
        String dati= "los = " + beacon.getLos();
        tabella.update(dati);
        tabella.where("id ='" + beacon.getId() + "'");
        tabella.execute();
    } */

    public static BeaconEntity getBeaconById(int beaconId){
        tabella.select();
        tabella.where("id = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        BeaconEntity beacon = new BeaconEntity();
        beacon.setId(Integer.parseInt(rs.get(0).get("id").toString()));
        beacon.setV(Float.parseFloat(rs.get(0).get("v").toString()));
        beacon.setR(Float.parseFloat(rs.get(0).get("r").toString()));
        beacon.setK(Float.parseFloat(rs.get(0).get("k").toString()));
        beacon.setL(Float.parseFloat(rs.get(0).get("l").toString()));
        return beacon;
    }

    public static Set<BeaconEntity> getAllPuntiDiRaccolta(){
        Set<BeaconEntity> allPuntiDiRaccolta = new HashSet<>();
        tabella.select();
        tabella.where("is_puntodiraccolta = '1'" );
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            BeaconEntity beaconDiRaccolta = new BeaconEntity(
                    Integer.parseInt(rs.get(i).get("id").toString()),
                    Float.parseFloat(rs.get(i).get("v").toString()),
                    Float.parseFloat(rs.get(i).get("r").toString()),
                    Float.parseFloat(rs.get(i).get("k").toString()),
                    Float.parseFloat(rs.get(i).get("l").toString()),
                    Boolean.parseBoolean(rs.get(i).get("is_puntodiraccolta").toString()),
                    PianoDAO.getPianoById(Integer.parseInt(rs.get(i).get("pianoId").toString()))
            );
            allPuntiDiRaccolta.add(beaconDiRaccolta);
        }
        return allPuntiDiRaccolta;
    }
}
