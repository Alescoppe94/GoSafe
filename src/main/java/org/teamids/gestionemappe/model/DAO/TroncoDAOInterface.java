package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.json.JsonArray;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

/**
 * Interfaccia della classe TroncoDAO,
 * include l'elenco delle funzionalità che la classe TroncoDAO implementa
 */
public interface TroncoDAOInterface {
    Set<TroncoEntity> getAllTronchi(Connection db);

    ArrayList<TroncoEntity> getTronchiPiano(int pianoId, Connection db);

    TroncoEntity getTroncoByBeacons(BeaconEntity beaconA, BeaconEntity beaconB, Connection db);

    TroncoEntity getTroncoById(String troncoId, boolean direzione, Connection db);

    boolean checkDirezioneTronco(TroncoEntity troncoOttimo, Connection db);

    JsonArray getAllTronchiAggiornati(Timestamp timestamp, Connection db);

    JsonArray getTable(Connection db);

    ArrayList<Integer> inserisciTronchi(ArrayList<TroncoEntity> tronchi, Connection db);

    void eliminaTronchiPerPiano(int pianoId, Connection db);

    ArrayList<Integer> getAllIdTronchi(Connection db);
}
