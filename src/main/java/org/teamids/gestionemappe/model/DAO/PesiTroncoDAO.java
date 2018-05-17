package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.PesiTronco;
import org.teamids.gestionemappe.model.DbTable.Peso;

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
        Float valore = null;

        if(rs.size() != 0)
            valore = Float.parseFloat(rs.get(0).get("valore").toString());

        return valore;
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


    public void aggiornaPesiTronco(int troncoId, String peso, float valore){

        tabella.select();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        List<Map<String, Object>> rs = tabella.fetch();
        if(rs.size()==0){
            Peso tabella2 = new Peso();                             //non è bellissima sta cosa ma funziona
            tabella2.select("id");
            tabella2.where("nome = '"+ peso +"'");
            List<Map<String, Object>> rs2 = tabella2.fetch();
            String dati= "null";
            dati=dati+",'"+troncoId+"'";
            dati=dati+",'"+rs2.get(0).get("id").toString()+"'";
            dati=dati+",'" + valore + "'";
            dati=dati+",null";
            tabella.insert(dati);
            tabella.execute();
        }else{
            updateValorePeso(troncoId, peso, valore);
        }

    }

}
