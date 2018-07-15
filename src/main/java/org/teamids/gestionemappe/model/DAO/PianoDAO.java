package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.control.GestioneDB;
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

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Piano del database
 */
public class PianoDAO implements PianoDAOInterface {

    protected Piano tabella;

    /**
     * Costruttore della classe PianoDAO
     */
    public PianoDAO() {

         this.tabella = new Piano();

    }

    /**
     * Permette di recuperare le informazioni di tutti i piani che sono stati modificati
     * dopo un certo timestamp, sotto forma di json
     * @param timestamp orario usato come soglia
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray che contiene le info di tutti quei piani modificati dopo un certo timestamp
     */
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

    /**
     * Permette di generare un json contenente le info di tutti i piani
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray contenenti le informazioni di tutti i piani memorizzati nel db
     */
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

    /**
     * Permette di inserire un nuovo piano
     * @param piano oggetto di tipo PianoEntity che contiene tutte le info del piano da inserire
     * @param db parametro utilizzato per la connessione al database
     * @return l'identificatore del piano appena inserito
     */
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

    /**
     * Permette di eliminare un piano
     * @param pianoId identificatore del piano da eliminare
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void eliminaPiano(int pianoId, Connection db){
        tabella.delete();
        tabella.where("id = '" + pianoId + "'");
        tabella.execute(db);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        GestioneDB.setLast_time_deleted(time);
    }

    /**
     * Permette di ottenere tutti i piani memorizzati nel database
     * @param db parametro utilizzato per la connessione al database
     * @return ArrayList di oggetti PianoEntity che contiene le info di tutti i piani memorizzati
     */
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
