package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Tappa;
import org.teamids.gestionemappe.model.entity.TappaEntity;

public class TappaDAO {

    protected Tappa tabella;

    public TappaDAO() {
        tabella= new Tappa();
    }

    public void insertTappa(TappaEntity tappa){

        String dati= String.valueOf(tappa.getId());
        dati=dati+",'"+ tappa.getPercorsoId()+"'";
        dati=dati+",'"+ tappa.getTronco()+"'";
        tabella.insert(dati);
        int id_tappa = tabella.executeForKey();
        tappa.setId(id_tappa);

    }

}
