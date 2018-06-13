package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Peso;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesoDAO {

    protected Peso tabella;

    public PesoDAO() {
        this.tabella = new Peso();
    }

    public JsonArray getAllPesiAggiornati(Timestamp timestamp, Connection db) {
        JsonArrayBuilder pesiAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            pesiAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("nome",rs.get(i).get("nome").toString())
                    .add("coefficiente",rs.get(i).get("coefficiente").toString())
            );
        }
        return pesiAggiornati.build();
    }

    public Map<Integer, Map<String,Float>> getPesi(Connection db){

        Map<Integer, Map<String,Float>> pesi = new HashMap<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {

            Map<String, Float> nomeVal = new HashMap<>();
            nomeVal.put(rs.get(i).get("nome").toString(), Float.valueOf(rs.get(i).get("coefficiente").toString()));
            pesi.put(Integer.parseInt(rs.get(i).get("id").toString()), nomeVal);
        }

        return pesi;

    }

    public JsonArray getTable(Connection db) {
        JsonArrayBuilder pesi = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            pesi.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("nome",rs.get(i).get("nome").toString())
                    .add("coefficiente",rs.get(i).get("coefficiente").toString())
            );
        }
        return pesi.build();
    }

    public void aggiornaPeso(int id, String nome,float coefficiente, Connection db){

        String dati= "coefficiente = " + coefficiente;
        tabella.update();
        tabella.set(dati);
        tabella.where("id = '" + id +"'");
        tabella.execute(db);
    }

    public void inserisciPeso(String nome, float valore, Connection db){

        String dati= "null";
        dati=dati+",'"+ nome +"'";
        dati=dati+",'"+ valore +"'";
        dati=dati+",null";
        tabella.insert(dati);
        tabella.execute(db);

    }

    public void eliminaPeso(int idPeso, Connection db){
        tabella.delete();
        tabella.where("id = '" + idPeso +"'");
        tabella.execute(db);
    }

    public ArrayList<Integer> getIdPesi(Connection db) {
        ArrayList<Integer> idPesi = new ArrayList<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            idPesi.add(Integer.parseInt(rs.get(i).get("id").toString()));
        }
        return idPesi;
    }
}
