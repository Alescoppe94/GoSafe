package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Piano;
import org.teamids.gestionemappe.model.entity.PianoEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class PianoDAO {

    protected Piano tabella;

    public PianoDAO() {

         this.tabella = new Piano();

    }

    public PianoEntity getPianoById(int idpiano){
        tabella.select();
        tabella.where("id = '" + idpiano + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        PianoEntity piano = new PianoEntity();
        piano.setId(Integer.parseInt(rs.get(0).get("id").toString()));
        piano.setImmagine(rs.get(0).get("immagine").toString());
        piano.setPiano(Integer.parseInt(rs.get(0).get("piano").toString()));
        return piano;
    }

    public JsonArray getAllPianiAggiornati(Timestamp timestamp) {
        JsonArrayBuilder pianiAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            pianiAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("immagine",rs.get(i).get("immagine").toString())
                    .add("piano",rs.get(i).get("piano").toString())
            );
        }
        return pianiAggiornati.build();

    }
}
