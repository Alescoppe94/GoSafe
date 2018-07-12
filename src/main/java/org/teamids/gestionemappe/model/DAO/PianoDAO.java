package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Piano;
import org.teamids.gestionemappe.model.entity.PianoEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PianoDAO implements PianoDAOInterface {

    protected Piano tabella;

    public PianoDAO() {

         this.tabella = new Piano();

    }

    @Override
    public PianoEntity getPianoById(int idpiano, Connection db){
        tabella.select();
        tabella.where("id = '" + idpiano + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        PianoEntity piano = new PianoEntity();
        piano.setId(Integer.parseInt(rs.get(0).get("id").toString()));
        piano.setImmagine(rs.get(0).get("immagine").toString());
        piano.setPiano(Integer.parseInt(rs.get(0).get("piano").toString()));
        return piano;
    }

    @Override
    public JsonArray getAllPianiAggiornati(Timestamp timestamp, Connection db) {
        JsonArrayBuilder pianiAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            pianiAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("immagine",rs.get(i).get("immagine").toString())
                    .add("piano",rs.get(i).get("piano").toString())
            );
        }
        return pianiAggiornati.build();

    }

    @Override
    public JsonArray getTable(Connection db) {
        JsonArrayBuilder piani = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            piani.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("immagine",rs.get(i).get("immagine").toString())
                    .add("piano",rs.get(i).get("piano").toString())
            );
        }
        return piani.build();
    }

    @Override
    public int inserisciPiano(PianoEntity piano, Connection db){

        String dati= String.valueOf(piano.getId());
        dati=dati+",'"+piano.getImmagine()+"'";
        dati=dati+",'"+piano.getPiano()+"'";
        dati=dati+",null";
        tabella.insert(dati);
        int id_piano = tabella.executeForKey(db);
        piano.setId(id_piano);
        return id_piano;

    }

    @Override
    public void eliminaPiano(int pianoId, Connection db){
        tabella.delete();
        tabella.where("id = '" + pianoId + "'");
        tabella.execute(db);
    }

    @Override
    public ArrayList<PianoEntity> getAllPiani(Connection db){

        ArrayList<PianoEntity> piani = new ArrayList<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            PianoEntity piano = new PianoEntity(Integer.parseInt(rs.get(i).get("id").toString()), "Ciao", Integer.parseInt(rs.get(i).get("piano").toString()));
            piani.add(piano);
        }
        return piani;
    }
}
