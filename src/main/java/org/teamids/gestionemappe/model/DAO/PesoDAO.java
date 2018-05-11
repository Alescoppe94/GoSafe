package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Peso;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public void inserisciPeso(String nome,float coefficiente){
        String dati= String.valueOf(nome);
        dati=dati+",'"+ coefficiente +"'";
        tabella.insert(dati);
        tabella.execute();
    }

    public void eliminaPesi(){
        tabella.delete();
        tabella.execute();
    }
}
