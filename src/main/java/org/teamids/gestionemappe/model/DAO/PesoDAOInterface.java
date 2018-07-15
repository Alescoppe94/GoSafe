package org.teamids.gestionemappe.model.DAO;

import javax.json.JsonArray;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interfaccia della classe PesoDAO,
 * include l'elenco delle funzionalità che la classe PesoDAO implementa
 */
public interface PesoDAOInterface {
    JsonArray getAllPesiAggiornati(Timestamp timestamp, Connection db);

    Map<Integer, Map<String,Float>> getPesi(Connection db);

    JsonArray getTable(Connection db);

    void aggiornaPeso(int id, float coefficiente, Connection db);

    int inserisciPeso(String nome, float valore, Connection db);

    void eliminaPeso(int idPeso, Connection db);

    ArrayList<Integer> getIdPesi(Connection db);
}
