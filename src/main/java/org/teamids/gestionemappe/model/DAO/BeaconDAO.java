package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Beacon;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import java.sql.ResultSet;

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
        ResultSet risultato = tabella.fetch();
        try{
            risultato.next();
            BeaconEntity beacon = new BeaconEntity();
            beacon.setId(risultato.getInt("id"));
            beacon.setLos(risultato.getFloat("los"));
            beacon.setV(risultato.getFloat("v"));
            beacon.setR(risultato.getFloat("r"));
            beacon.setK(risultato.getFloat("k"));
            beacon.setL(risultato.getFloat("l"));
            beacon.setArea(risultato.getFloat("area"));
            return beacon;
        }
        catch (Exception e){
            System.out.println("Errore" + e);
            return null;
        }

    }

}
