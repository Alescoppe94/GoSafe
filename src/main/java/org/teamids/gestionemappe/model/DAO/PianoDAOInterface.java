package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.PianoEntity;

import javax.json.JsonArray;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface PianoDAOInterface {
    PianoEntity getPianoById(int idpiano, Connection db);

    JsonArray getAllPianiAggiornati(Timestamp timestamp, Connection db);

    JsonArray getTable(Connection db);

    int inserisciPiano(PianoEntity piano, Connection db);

    void eliminaPiano(int pianoId, Connection db);

    ArrayList<PianoEntity> getAllPiani(Connection db);
}
