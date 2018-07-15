package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.PianoEntity;

import javax.json.JsonArray;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Interfaccia della classe PianoDAO,
 * include l'elenco delle funzionalità che la classe PianoDAO implementa
 */
public interface PianoDAOInterface {

    JsonArray getAllPianiAggiornati(Timestamp timestamp, Connection db);

    JsonArray getTable(Connection db);

    int inserisciPiano(PianoEntity piano, Connection db);

    void eliminaPiano(int pianoId, Connection db);

    ArrayList<PianoEntity> getAllPiani(Connection db);
}
