package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Beacon;

public class BeaconDAO {

    protected Beacon tabella;

    public BeaconDAO() {
        tabella = new Beacon();
    }

    public void updateBeacon(org.teamids.gestionemappe.model.entity.Beacon beacon){
        String dati= "los = " + beacon.getLos();
        tabella.update(dati);
        tabella.where("id ='" + beacon.getId() + "'");
        tabella.execute();
    }

}
