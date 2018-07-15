package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.DbTable.Beacon;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Beacon del database
 */
public class BeaconDAO implements BeaconDAOInterface {

    protected Beacon tabella;

    /**
     * Costruttore della classe BeaconDAO
     */
    public BeaconDAO() {
        this.tabella = new Beacon();
    }

    /**
     * Permette di recuperare le informazioni di un beacon presente nel database, a partire
     * dal suo identificatore
     * @param beaconId identificatore del beacon
     * @param db parametro utilizzato per la connessione al database
     * @return oggetto di tipo BeaconEntity contenenti tutte le informazioni ad esso associate
     */
    @Override
    public BeaconEntity getBeaconById(String beaconId, Connection db){
        tabella.select();
        tabella.where("id = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        BeaconEntity beacon = new BeaconEntity();
        beacon.setId(rs.get(0).get("id").toString());
        beacon.setPiano(Integer.parseInt(rs.get(0).get("pianoId").toString()));
        beacon.setIs_puntodiraccolta(Boolean.parseBoolean(rs.get(0).get("is_puntodiraccolta").toString()));
        beacon.setCoordx(Integer.parseInt(rs.get(0).get("coordx").toString()));
        beacon.setCoordy(Integer.parseInt(rs.get(0).get("coordy").toString()));
        return beacon;
    }

    /**
     * Permette di recuperare tutti i beacon memorizzati nel database
     * @param db parametro utilizzato per la connessione al database
     * @return Arraylist di oggetti di tipo BeaconEntity in cui ciascun beacon contiene
     * tutte le informazioni ad esso essociate
     */
    @Override
    public ArrayList<BeaconEntity> getAllBeacons(Connection db){

        ArrayList<BeaconEntity> beacons = new ArrayList<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            BeaconEntity beaconDiRaccolta = new BeaconEntity(
                    rs.get(i).get("id").toString(),
                    Boolean.parseBoolean(rs.get(i).get("is_puntodiraccolta").toString()),
                    Integer.parseInt(rs.get(i).get("pianoId").toString()),
                    Integer.parseInt(rs.get(i).get("coordx").toString()),
                    Integer.parseInt(rs.get(i).get("coordy").toString())
            );
            beacons.add(beaconDiRaccolta);
        }
        return beacons;
    }

    /**
     * Permette di recuperare tutti i beacon, che rappresentano punti di raccolta, memorizzati nel database
     * @param db parametro utilizzato per la connessione al database
     * @return Insieme di oggetti di tipo BeaconEntity che rappresentano punti di raccolta
     */
    @Override
    public Set<BeaconEntity> getAllPuntiDiRaccolta(Connection db){
        Set<BeaconEntity> allPuntiDiRaccolta = new HashSet<>();
        tabella.select();
        tabella.where("is_puntodiraccolta = '1'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            BeaconEntity beaconDiRaccolta = new BeaconEntity(
                    rs.get(i).get("id").toString(),
                    Boolean.parseBoolean(rs.get(i).get("is_puntodiraccolta").toString()),
                    Integer.parseInt(rs.get(i).get("pianoId").toString()),
                    Integer.parseInt(rs.get(i).get("coordx").toString()),
                    Integer.parseInt(rs.get(i).get("coordy").toString())
            );
            allPuntiDiRaccolta.add(beaconDiRaccolta);
        }
        return allPuntiDiRaccolta;
    }

    /**
     * Permette di recuperare tutte le informazion di tutti i beacon che sono stati modificati
     * dopo un certo timestamp, sotto forma di json
     * @param timestamp orario usato come soglia
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray che contiene le info di tutti quei beacon modificati dopo un certo timestamp
     */
    @Override
    public JsonArray getAllBeaconAggiornati(Timestamp timestamp, Connection db) {
        JsonArrayBuilder beaconAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            beaconAggiornati.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("is_puntodiraccolta",rs.get(i).get("is_puntodiraccolta").toString())
                    .add("pianoId",rs.get(i).get("pianoId").toString())
                    .add("coordx", rs.get(i).get("coordx").toString())
                    .add("coordy", rs.get(i).get("coordy").toString())
            );
        }
        return beaconAggiornati.build();
    }

    /**
     * Permette di generare un json contenente le info di tutti i beacon
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray contenenti le informazioni di tutti i beacon memorizzati nel db
     */
    @Override
    public JsonArray getTable(Connection db) {
        JsonArrayBuilder beacon = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            beacon.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("is_puntodiraccolta",rs.get(i).get("is_puntodiraccolta").toString())
                    .add("pianoId",rs.get(i).get("pianoId").toString())
                    .add("coordx", rs.get(i).get("coordx").toString())
                    .add("coordy", rs.get(i).get("coordy").toString())
            );
        }
        return beacon.build();
    }

    /**
     * Permette di inserire un'insieme di beacon, relativi ad un piano, che non siano già presenti nel database
     * @param beacons Arraylist di oggetti di tipo BeaconEntity che si intende inserire
     * @param piano_id numero del piano dei beacon
     * @param db parametro utilizzato per la connessione al database
     * @return Array di stringhe che contiene l'identificatore di quei beacon che non stati inseriti perchè già presenti
     */
    @Override
    public ArrayList<String> inserisciBeacons(ArrayList<BeaconEntity> beacons, int piano_id, Connection db){
        ArrayList<String> beaconDoppi = new ArrayList<>();
        for (BeaconEntity beacon : beacons){
            //Se il beacon che si vuole inserire non è già presente, lo inserisco
            if(!isBeaconInDb(beacon.getId(), db)) {
                int puntodiraccolta = beacon.isIs_puntodiraccolta() ? 1 : 0;
                String dati = "'"+beacon.getId()+"'";
                dati = dati + ",'" + puntodiraccolta + "'";
                dati = dati + ",'" + piano_id + "'";
                dati = dati + ",'" + beacon.getCoordx() + "'";
                dati = dati + ",'" + beacon.getCoordy() + "'";
                dati = dati + ",null";
                tabella.insert(dati);
                tabella.execute(db);
            }
            //Se il beacon è già presente, il suo identificatore lo aggiungo all'arraylist beaconDoppi
            else {
                beaconDoppi.add(beacon.getId());
            }
        }
        return beaconDoppi;
    }

    /**
     * Permette di eliminare tutti i beacon di un piano
     * @param pianoId numero del piano
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void eliminaBeaconsPerPiano(int pianoId, Connection db){
        tabella.delete();
        tabella.where("pianoId = '" + pianoId + "'");
        tabella.execute(db);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        GestioneDB.setLast_time_deleted(time);
    }

    /**
     * Permette di verificare se un certo beacon è presente nel database
     * @param idbeacon identificatore del beacon di cui si vuole verificare la presenza
     * @param db parametro utilizzato per la connessione al database
     * @return True se esiste un beacon con quel identificatore, altrimenti False
     */
    @Override
    public boolean isBeaconInDb(String idbeacon, Connection db){
        boolean success = false;
        tabella.select();
        tabella.where("id ='" + idbeacon + "'");
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }
}
