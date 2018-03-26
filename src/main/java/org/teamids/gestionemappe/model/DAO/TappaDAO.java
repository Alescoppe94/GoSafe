package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Tappa;
import org.teamids.gestionemappe.model.entity.TappaEntity;

public class TappaDAO {

    protected  static Tappa tabella = new Tappa();

    public TappaDAO() {
    }

    public static void insertTappa(TappaEntity tappa){

        String dati= "0";
        dati=dati+",'"+ tappa.getPercorsoId()+"'";
        dati=dati+",'"+ tappa.getTronco().getId()+"'"; //vedere se va bene
        tabella.insert(dati);
        int id_tappa = tabella.executeForKey();
        tappa.setId(id_tappa);

    }

}
