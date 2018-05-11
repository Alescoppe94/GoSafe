package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.PesiTronco;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesiTroncoDAO {

    protected PesiTronco tabella;

    public PesiTroncoDAO() {
        this.tabella = new PesiTronco();
    }

    public void updateValorePeso(int troncoId, String peso, float los){
        String dati= "valore = " + los;
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set(dati);
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        tabella.execute();
    }

    public HashMap<Float,Float> getPesiTronco(int troncoId) {
        tabella.select("valore,coefficiente");
        tabella.innerjoin("peso", "peso.id = pesoId");
        tabella.where("troncoId = '" + troncoId +"'");
        List<Map<String, Object>> rs = tabella.fetch();
        HashMap<Float, Float> coeffVal = new HashMap<>();
        for (int i = 0; i<rs.size(); i++) {
            coeffVal.put(Float.parseFloat(rs.get(i).get("coefficiente").toString()), Float.parseFloat(rs.get(i).get("valore").toString()));
        }
        return coeffVal;
    }

    public Float geValoreByPesoId(int troncoId, String peso) {
        tabella.select("valore");
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        List<Map<String, Object>> rs = tabella.fetch();
         return Float.parseFloat(rs.get(0).get("valore").toString());
    }

    public void losToDefault() {
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set("valore = 0");
        tabella.where("peso.nome = 'los'");
        tabella.execute();
    }

    public JsonArray getAllPesiTroncoAggiornati(Timestamp timestamp) {
        JsonArrayBuilder pesiTroncoAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            pesiTroncoAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("troncoId",rs.get(i).get("troncoId").toString())
                    .add("pesoId",rs.get(i).get("pesoId").toString())
                    .add("valore",rs.get(i).get("valore").toString())
            );
        }
        return pesiTroncoAggiornati.build();
    }

    public JsonArray getTable() {
        JsonArrayBuilder pesiTronco = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch();
        for (int i = 0; i<rs.size(); i++) {
            pesiTronco.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("troncoId",rs.get(i).get("troncoId").toString())
                    .add("pesoId",rs.get(i).get("pesoId").toString())
                    .add("valore",rs.get(i).get("valore").toString())
            );
        }
        return pesiTronco.build();
    }
}
