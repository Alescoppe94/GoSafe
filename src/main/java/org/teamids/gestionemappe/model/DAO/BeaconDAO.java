package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Beacon;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

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

}
