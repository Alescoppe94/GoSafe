package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DbTable.Beacon;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class BeaconDAO {

    protected Beacon tabella;

    public BeaconDAO() {
        tabella = new Beacon();
    }

    public void updateBeacon(BeaconEntity beacon){
        String dati= "los = " + beacon.getLos();
        tabella.update(dati);
        tabella.where("id ='" + beacon.getId() + "'");
        tabella.execute();
    }

    public BeaconEntity getBeaconById(int beaconId){
        tabella.select();
        tabella.where("id = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        BeaconEntity beacon = new BeaconEntity();
        beacon.setId(Integer.parseInt(rs.get(0).get("id").toString()));
        beacon.setLos(Float.parseFloat(rs.get(0).get("los").toString()));
        beacon.setV(Float.parseFloat(rs.get(0).get("v").toString()));
        beacon.setR(Float.parseFloat(rs.get(0).get("r").toString()));
        beacon.setK(Float.parseFloat(rs.get(0).get("k").toString()));
        beacon.setL(Float.parseFloat(rs.get(0).get("l").toString()));
        beacon.setArea(Float.parseFloat(rs.get(0).get("area").toString()));
        return beacon;

    }

}
