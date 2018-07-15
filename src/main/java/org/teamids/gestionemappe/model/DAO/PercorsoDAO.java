package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Percorso;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Percorso del database
 */
public class PercorsoDAO implements PercorsoDAOInterface {

    protected Percorso tabella;

    /**
     * Costruttore della classe PercorsoDAO
     */
    public PercorsoDAO() {
        this.tabella = new Percorso();
    }

    /**
     * Permette di recuperare un percorso memorizzato nel database, a partire dall'identificatore del beacon di partenza
     * @param beaconId identificatore del beacon di partenza del percorso
     * @param db parametro utilizzato per la connessione al database
     * @return il percorso memorizzato nel database
     */
    @Override
    public PercorsoEntity getPercorsoByBeaconId(String beaconId, Connection db){
        tabella.select();
        tabella.where("beaconPartenzaId = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        PercorsoEntity percorso = null;
        BeaconDAO beaconDAO = new BeaconDAO();
        TappaDAO tappaDAO = new TappaDAO();
        if(rs.size() != 0) {
            try {
                int percorsoId = Integer.parseInt(rs.get(0).get("id").toString());
                LinkedList<TappaEntity> tappe = tappaDAO.getTappeByPercorsoId(percorsoId, db);
                percorso = new PercorsoEntity(percorsoId, tappe, beaconDAO.getBeaconById(beaconId, db));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return percorso;
    }

    /**
     * Permette di verificare se un percorso che parte da un certo beacon è già memorizzato nel database o meno
     * @param beaconPart identificatore del beacon di partenza del percorso
     * @param db parametro utilizzato per la connessione al database
     * @return True se esiste un percorso che parte da quel beacon, altrimenti False.
     */
    @Override
    public boolean findPercorsoByBeaconId(String beaconPart, Connection db) {
        boolean success = false;
        tabella.select();
        tabella.where("beaconPartenzaId='" + beaconPart + "'");
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    /**
     * Permette di rimuovere tutti gli elementi presenti nella tabella
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void removeAllPercorsi(Connection db) {
        tabella.delete();
        tabella.execute(db);
    }
}
