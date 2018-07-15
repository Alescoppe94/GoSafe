package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.control.GestioneDB;
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
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Peso del database
 */
public class PesoDAO implements PesoDAOInterface {

    protected Peso tabella;

    public PesoDAO() {
        this.tabella = new Peso();
    }

    /**
     * Permette di recuperare le informazioni di tutti i pesi che sono stati modificati
     * dopo un certo timestamp, sotto forma di json
     * @param timestamp orario usato come soglia
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray che contiene le info di tutti quei pesi modificati dopo un certo timestamp
     */
    @Override
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

    /**
     * Permette di ottenere tutti i pesi con i relativi coefficienti memorizzati nel database
     * @param db parametro utilizzato per la connessione al database
     * @return Hashmap di coppie chiave-valore in cui la chiave è il nome del peso ed il valore
     * è il valore del coefficiente
     */
    @Override
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

    /**
     * Permette di generare un json contenente le info di tutti i pesi
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray contenenti le informazioni di tutti i pesi memorizzati nel db
     */
    @Override
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

    /**
     * Permette di aggiornare il coefficente di un certo peso
     * @param id identificatore del peso
     * @param coefficiente nuovo valore del coefficente
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void aggiornaPeso(int id, float coefficiente, Connection db){

        String dati= "coefficiente = " + coefficiente;
        tabella.update();
        tabella.set(dati);
        tabella.where("id = '" + id +"'");
        tabella.execute(db);
    }

    /**
     * Permette di aggiungere un nuovo peso
     * @param nome nome del peso
     * @param valore valore del coefficente
     * @param db parametro utilizzato per la connessione al database
     * @return l'identificatore del peso appena inserito
     */
    @Override
    public int inserisciPeso(String nome, float valore, Connection db){

        String dati= "null";
        dati=dati+",'"+ nome +"'";
        dati=dati+",'"+ valore +"'";
        dati=dati+",null";
        tabella.insert(dati);
        return tabella.executeForKey(db);

    }

    /**
     * Permette di eliminare un peso
     * @param idPeso identificatore del peso da elimiare
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void eliminaPeso(int idPeso, Connection db){
        tabella.delete();
        tabella.where("id = '" + idPeso +"'");
        tabella.execute(db);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        GestioneDB.setLast_time_deleted(time);
    }

    /**
     * Permette di recuperare gli identificatori di tutti i pesi memorizzati
     * @param db parametro utilizzato per la connessione al database
     * @return ArraList di interi contenente tutti gli identificati dei pesi memorizzati
     */
    @Override
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
