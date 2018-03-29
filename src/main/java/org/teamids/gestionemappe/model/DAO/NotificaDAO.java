package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Notifica;
import org.teamids.gestionemappe.model.entity.NotificaEntity;

public class NotificaDAO {

    protected static Notifica tabella = new Notifica();

    public NotificaDAO(){
    }

    public static void insertNotifica(NotificaEntity notifica) {
        String dati= String.valueOf(notifica.getId());
        dati=dati+",'"+notifica.getUtenteId()+"'";
        dati=dati+",'"+notifica.getPercorso().getId()+"'";
        dati=dati+",'"+notifica.getDataorario()+"'";
        dati=dati+",'"+notifica.getMessaggio()+"'";
        tabella.insert(dati);
        int id_notifica = tabella.executeForKey();
        notifica.setId(id_notifica);
    }
}
