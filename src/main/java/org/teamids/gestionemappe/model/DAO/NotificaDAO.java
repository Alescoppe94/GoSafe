package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Notifica;
import org.teamids.gestionemappe.model.entity.NotificaEntity;

import java.sql.Connection;

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Notifica del database
 */
public class NotificaDAO implements NotificaDAOInterface {

    protected Notifica tabella;

    /**
     * Costruttore della classe NotificaDAO
     */
    public NotificaDAO(){
        this.tabella = new Notifica();
    }

    /**
     * Permette di memorizzare una notifica nel database
     * @param notifica oggetto di tipo NotificaEntity che contiene tutte le info che si intendono memorizzare
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void insertNotifica(NotificaEntity notifica, Connection db) {
        String dati= String.valueOf(notifica.getId());
        dati=dati+",'"+notifica.getUtenteId()+"'";
        dati=dati+",'"+notifica.getPercorso().getId()+"'";
        dati=dati+",'"+notifica.getDataorario()+"'";
        dati=dati+",'"+notifica.getMessaggio()+"'";
        tabella.insert(dati);
        int id_notifica = tabella.executeForKey(db);
        notifica.setId(id_notifica);
    }
}
