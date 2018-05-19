package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Peso;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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

    public JsonArray getAllPesiAggiornati(Timestamp timestamp) {
        JsonArrayBuilder pesiAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            pesiAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("nome",rs.get(i).get("nome").toString())
                    .add("coefficiente",rs.get(i).get("coefficiente").toString())
            );
        }
        return pesiAggiornati.build();
    }

    public Map<Integer, Map<String,Float>> getPesi(){

        Map<Integer, Map<String,Float>> pesi = new HashMap<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {

            Map<String, Float> nomeVal = new HashMap<>();
            nomeVal.put(rs.get(i).get("nome").toString(), Float.valueOf(rs.get(i).get("coefficiente").toString()));
            pesi.put(Integer.parseInt(rs.get(i).get("id").toString()), nomeVal);
        }

        return pesi;

    }

    public JsonArray getTable() {
        JsonArrayBuilder pesi = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            pesi.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("nome",rs.get(i).get("nome").toString())
                    .add("coefficiente",rs.get(i).get("coefficiente").toString())
            );
        }
        return pesi.build();
    }

    public void aggiornaPeso(int id, String nome,float coefficiente){

        String dati= "coefficiente = " + coefficiente;
        tabella.update();
        tabella.set(dati);
        tabella.where("id = '" + id +"'");
        tabella.execute();
    }

    public void inserisciPeso(String nome, float valore){

        String dati= "null";
        dati=dati+",'"+ nome +"'";
        dati=dati+",'"+ valore +"'";
        dati=dati+",null";
        tabella.insert(dati);
        tabella.execute();

    }

    public void eliminaPesi(){
        tabella.delete();
        tabella.execute();
    }
}
