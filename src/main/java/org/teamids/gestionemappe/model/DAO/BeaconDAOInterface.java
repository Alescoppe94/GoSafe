package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.BeaconEntity;

import javax.json.JsonArray;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

/**
 * Interfaccia della classe BeaconDAO,
 * include l'elenco delle funzionalità che la classe BeaconDAO implementa
 */
public interface BeaconDAOInterface {
    BeaconEntity getBeaconById(String beaconId, Connection db);

    ArrayList<BeaconEntity> getAllBeacons(Connection db);

    Set<BeaconEntity> getAllPuntiDiRaccolta(Connection db);

    JsonArray getAllBeaconAggiornati(Timestamp timestamp, Connection db);

    JsonArray getTable(Connection db);

    ArrayList<String> inserisciBeacons(ArrayList<BeaconEntity> beacons, int piano_id, Connection db);

    void eliminaBeaconsPerPiano(int pianoId, Connection db);

    boolean isBeaconInDb(String idbeacon, Connection db);
}
