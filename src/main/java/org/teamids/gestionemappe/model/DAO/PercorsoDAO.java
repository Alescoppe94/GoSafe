package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Percorso;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;

public class PercorsoDAO {

    protected Percorso tabella;

    public PercorsoDAO() {
        tabella= new Percorso();
    }

    public int insertPercorso(BeaconEntity beaconPartenza){

        String dati= "0";
        dati=dati+",'"+ beaconPartenza.getId() +"'";
        tabella.insert(dati);
        return tabella.executeForKey();

    }

}
