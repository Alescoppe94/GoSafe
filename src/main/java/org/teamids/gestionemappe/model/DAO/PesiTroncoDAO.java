package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.DbTable.PesiTronco;
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

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella PesiTronco del database
 */
public class PesiTroncoDAO implements PesiTroncoDAOInterface {

    protected PesiTronco tabella;

    /**
     * Costruttore della classe PesiTroncoDAO
     */
    public PesiTroncoDAO() {
        this.tabella = new PesiTronco();
    }

    /**
     * Permette di aggiornare il valore di un PesiTronco, relativo ad un certo peso, di un tronco
     * @param troncoId identificativo del tronco
     * @param peso nome del peso
     * @param los nuovo valore del PesiTronco
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void updateValorePeso(int troncoId, String peso, float los, Connection db){
        String dati= "valore = " + los;
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set(dati);
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        tabella.execute(db);
    }

    /**
     * Permette di ottenere tutti i valori dei PesiTronco, con i rispettivi coefficienti dei pesi, di un determinato tronco
     * @param troncoId identificatore del tronco
     * @param db parametro utilizzato per la connessione al database
     * @return Hashmap che contiene un insieme di coppie chiave-valore in cui
     * la chiave è il coefficiente del peso ed il valore è il valore del PesiTronco
     */
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

    /**
     * Permette di ottenere il valore di un PesiTronco, relativo ad un peso, di un determinato tronco
     * @param troncoId identificatore del tronco
     * @param peso nome del peso
     * @param db parametro utilizzato per la connessione al database
     * @return Valore del PesiTronco relativo ad un peso di un certo tronco
     */
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

    /**
     * Permette di settare il valore del los di tutti i tronchi al valore originario(cioè zero)
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void losToDefault(Connection db) {
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set("valore = 0");
        tabella.where("peso.nome = 'los'");
        tabella.execute(db);
    }

    /**
     * Permette di recuperare tutti gli elementi della tabella che sono stati modificati
     * dopo un certo timestamp, sotto forma di json
     * @param timestamp orario usato come soglia
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray che contiene le info di tutti quei beacon modificati dopo un certo timestamp
     */
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

    /**
     * Permette di generare un json contenente le info di tutti gli elementi della tabella
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray contenenti le informazioni di tutti gli elementi memorizzati nel db
     */
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


    /**
     * Permette di aggiornare o inserire, se non presente, il valore di un PesiTronco relativo ad un tronco
     * @param troncoId identificatore del tronco
     * @param peso nome del peso
     * @param valore nuovo valore del peso
     * @param db parametro utilizzato per la connessione al database
     */
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

    /**
     * Permette di eliminare tutti i PesiTronco di tutti i tronchi relativi ad un piano
     * @param idPiano numero del piano
     * @param db parametro utilizzato per la connessione al database
     */
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
        Timestamp time = new Timestamp(System.currentTimeMillis());
        GestioneDB.setLast_time_deleted(time);
    }

    /**
     * Permette di eliminare tutti gli elementi della tabella relativi ad un certo peso
     * @param idPeso identificatore del peso
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void eliminaPesiTroncoByPeso(int idPeso, Connection db){

        tabella.delete();
        tabella.where("pesoId = '" + idPeso +"'");
        tabella.execute(db);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        GestioneDB.setLast_time_deleted(time);
    }

    /**
     * Permette di inserire, per ogni tronco contenuto in una lista, tutti i relativi PesiTronco
     * @param idTronchi ArrayList che contiene gli identificatori dei tronchi
     * @param db parametro utilizzato per la connessione al database
     */
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

    /**
     * Permette di inserire,qualora fosse aggiunto un nuovo peso, il valore del relativo PesiTronco per tutti i tronchi
     * @param idPeso identificativo del nuovo peso aggiunto
     * @param db parametro utilizzato per la connessione al database
     */
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
