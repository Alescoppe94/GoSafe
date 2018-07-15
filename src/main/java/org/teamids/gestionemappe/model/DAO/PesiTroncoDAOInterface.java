package org.teamids.gestionemappe.model.DAO;

import javax.json.JsonArray;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaccia della classe PesiTroncoDAO,
 * include l'elenco delle funzionalità che la classe PesiTroncoDAO implementa
 */
public interface PesiTroncoDAOInterface {
    void updateValorePeso(int troncoId, String peso, float los, Connection db);

    HashMap<Float,Float> getPesiTronco(int troncoId, Connection db);

    Float geValoreByPesoId(int troncoId, String peso, Connection db);

    void losToDefault(Connection db);

    JsonArray getAllPesiTroncoAggiornati(Timestamp timestamp, Connection db);

    JsonArray getTable(Connection db);

    void aggiornaPesiTronco(int troncoId, String peso, float valore, Connection db);

    void eliminaPesiTroncoPerPiano(int idPiano, Connection db);

    void eliminaPesiTroncoByPeso(int idPeso, Connection db);

    void inserisciPesiTronco(ArrayList<Integer> idTronchi, Connection db);

    void inserisciPesiTroncoPerNuovoPeso(int idPeso, Connection db);
}
