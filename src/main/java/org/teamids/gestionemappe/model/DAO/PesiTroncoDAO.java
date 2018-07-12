package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.PesiTronco;
import org.teamids.gestionemappe.model.DbTable.Peso;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesiTroncoDAO implements PesiTroncoDAOInterface {

    protected PesiTronco tabella;

    public PesiTroncoDAO() {
        this.tabella = new PesiTronco();
    }

    @Override
    public void updateValorePeso(int troncoId, String peso, float los, Connection db){
        String dati= "valore = " + los;
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set(dati);
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        tabella.execute(db);
    }

    @Override
    public HashMap<Float,Float> getPesiTronco(int troncoId, Connection db) {
        tabella.select("valore,coefficiente");
        tabella.innerjoin("peso", "peso.id = pesoId");
        tabella.where("troncoId = '" + troncoId +"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        HashMap<Float, Float> coeffVal = new HashMap<>();
        for (int i = 0; i<rs.size(); i++) {
            coeffVal.put(Float.parseFloat(rs.get(i).get("coefficiente").toString()), Float.parseFloat(rs.get(i).get("valore").toString()));
        }
        return coeffVal;
    }

    @Override
    public Float geValoreByPesoId(int troncoId, String peso, Connection db) {
        tabella.select("valore");
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        Float valore = null;

        if(rs.size() != 0)
            valore = Float.parseFloat(rs.get(0).get("valore").toString());

        return valore;
    }

    @Override
    public void losToDefault(Connection db) {
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set("valore = 0");
        tabella.where("peso.nome = 'los'");
        tabella.execute(db);
    }

    @Override
    public JsonArray getAllPesiTroncoAggiornati(Timestamp timestamp, Connection db) {
        JsonArrayBuilder pesiTroncoAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
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

    @Override
    public JsonArray getTable(Connection db) {
        JsonArrayBuilder pesiTronco = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
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


    @Override
    public void aggiornaPesiTronco(int troncoId, String peso, float valore, Connection db){

        tabella.select();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        if(rs.size()==0){
            Peso tabella2 = new Peso();                             //non è bellissima sta cosa ma funziona
            tabella2.select("id");
            tabella2.where("nome = '"+ peso +"'");
            List<Map<String, Object>> rs2 = tabella2.fetch(db);
            String dati= "null";
            dati=dati+",'"+troncoId+"'";
            dati=dati+",'"+rs2.get(0).get("id").toString()+"'";
            dati=dati+",'" + valore + "'";
            dati=dati+",null";
            tabella.insert(dati);
            tabella.execute(db);
        }else{
            updateValorePeso(troncoId, peso, valore, db);
        }

    }

    @Override
    public void eliminaPesiTroncoPerPiano(int idPiano, Connection db){

        tabella.delete2();
        tabella.doubleinnerjoin("tronco", "pesitronco.troncoId = tronco.id", "beacon", "tronco.beaconAId=beacon.id");
        tabella.where("beacon.pianoId = '" + idPiano +"'");
        tabella.execute(db);
        tabella.delete2();
        tabella.doubleinnerjoin("tronco", "pesitronco.troncoId = tronco.id", "beacon", "tronco.beaconBId=beacon.id");
        tabella.where("beacon.pianoId = '" + idPiano +"'");
        tabella.execute(db);

    }

    @Override
    public void eliminaPesiTroncoByPeso(int idPeso, Connection db){

        tabella.delete();
        tabella.where("pesoId = '" + idPeso +"'");
        tabella.execute(db);

    }

    @Override
    public void inserisciPesiTronco(ArrayList<Integer> idTronchi, Connection db) {
        PesoDAO pesoDAO = new PesoDAO();
        ArrayList<Integer> idPesi = pesoDAO.getIdPesi(db);
        for(int idTronco: idTronchi){
            for(int idPeso: idPesi) {
                String dati= "0";
                dati=dati+",'"+idTronco+"'";
                dati=dati+",'"+idPeso+"'";
                dati=dati+",'0'";
                dati=dati+",null";
                tabella.insert(dati);
                tabella.execute(db);
            }
        }
    }

    @Override
    public void inserisciPesiTroncoPerNuovoPeso(int idPeso, Connection db) {
        TroncoDAO troncoDAO = new TroncoDAO();
        ArrayList<Integer> idTronchi = troncoDAO.getAllIdTronchi(db);
        for(int idTronco: idTronchi){
            String dati = "0";
            dati=dati+",'"+idTronco+"'";
            dati=dati+",'"+idPeso+"'";
            dati=dati+",'0'";
            dati=dati+",null";
            tabella.insert(dati);
            tabella.execute(db);
        }
    }
}
