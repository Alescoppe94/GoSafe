package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.DbTable.Tronco;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Tronco del database
 */
public class TroncoDAO implements TroncoDAOInterface {

    protected Tronco tabella;

    /**
     * Costruttore della classe TroncoDAO
     */
    public TroncoDAO() {
        this.tabella = new Tronco();
    }

    /**
     * Permette di ottenere tutti i tronchi memorizzati nel database
     * @param db parametro utilizzato per la connessione al database
     * @return Insieme di oggetti di tipo TroncoEntity contenente le info di tutti i tronchi memorizzati
     */
    @Override
    public Set<TroncoEntity> getAllTronchi(Connection db){
        Set<TroncoEntity> allTronchiEdificio = new HashSet<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        BeaconDAO beaconDAO = new BeaconDAO();
        for (int i = 0; i<rs.size(); i++) {
                ArrayList<BeaconEntity> estremiOrdinati = new ArrayList<>();
                ArrayList<BeaconEntity> estremiInvertiti = new ArrayList<>();
                estremiOrdinati.add(beaconDAO.getBeaconById(rs.get(i).get("beaconAId").toString(), db));
                estremiOrdinati.add(beaconDAO.getBeaconById(rs.get(i).get("beaconBId").toString(), db));
                estremiInvertiti.add(beaconDAO.getBeaconById(rs.get(i).get("beaconBId").toString(), db));
                estremiInvertiti.add(beaconDAO.getBeaconById(rs.get(i).get("beaconAId").toString(), db));
                TroncoEntity troncoOrd = new TroncoEntity(
                        Integer.parseInt(rs.get(i).get("id").toString()),
                        Boolean.parseBoolean(rs.get(i).get("agibile").toString()),
                        estremiOrdinati,
                        Float.parseFloat(rs.get(i).get("area").toString())
                );
                TroncoEntity troncoInv = new TroncoEntity(
                        Integer.parseInt(rs.get(i).get("id").toString()),
                        Boolean.parseBoolean(rs.get(i).get("agibile").toString()),
                        estremiInvertiti,
                        Float.parseFloat(rs.get(i).get("area").toString())
                );
                allTronchiEdificio.add(troncoOrd);
                allTronchiEdificio.add(troncoInv);
            }
        return allTronchiEdificio;
    }

    /**
     * Permette di ottenere tutti i tronchi di un certo piano
     * @param pianoId identificatore del piano
     * @param db parametro utilizzato per la connessione al database
     * @return ArrayList di oggetti di tipo TroncoEntity contenente le info di tutti i tronchi memorizzati di un determinato piano
     */
    @Override
    public ArrayList<TroncoEntity> getTronchiPiano(int pianoId, Connection db){
        ArrayList<TroncoEntity> allTronchiPiano = new ArrayList<>();
        tabella.select("tronco.*");
        tabella.innerjoin("beacon", "tronco.beaconAId = beacon.id");
        tabella.where("beacon.pianoId = '" + pianoId +"'");
        tabella.order("tronco.id");
        List<Map<String, Object>> rs = tabella.fetch(db);
        BeaconDAO beaconDAO = new BeaconDAO();
        for (int i = 0; i<rs.size(); i++) {
            ArrayList<BeaconEntity> estremi = new ArrayList<>();
            estremi.add(beaconDAO.getBeaconById(rs.get(i).get("beaconAId").toString(), db));
            estremi.add(beaconDAO.getBeaconById(rs.get(i).get("beaconBId").toString(), db));
            TroncoEntity tronco = new TroncoEntity(
                    Integer.parseInt(rs.get(i).get("id").toString()),
                    Boolean.parseBoolean(rs.get(i).get("agibile").toString()),
                    estremi,
                    Float.parseFloat(rs.get(i).get("area").toString())
            );
            allTronchiPiano.add(tronco);
        }
        return allTronchiPiano;
    }

    /**
     * Permette di ottenere il tronco compreso tra una coppia di beacon
     * @param beaconA oggetto di tipo BeaconEntity che contiene tutte le info del primo beacon
     * @param beaconB oggetto di tipo BeaconEntity che contiene tutte le info del secondo beacon
     * @param db parametro utilizzato per la connessione al database
     * @return oggetto di tipo TroncoEntity con le relative info
     */
    @Override
    public TroncoEntity getTroncoByBeacons(BeaconEntity beaconA, BeaconEntity beaconB, Connection db){

        tabella.select();
        tabella.where("beaconAId = '" + beaconA.getId() + "' and beaconBId = '" + beaconB.getId() + "' or beaconAId = '" + beaconB.getId() + "' and beaconBId = '" + beaconA.getId() + "'"  );
        List<Map<String, Object>> rs = tabella.fetch(db);
        ArrayList<BeaconEntity> estremiTronco = new ArrayList<>();
        estremiTronco.add(beaconA);
        estremiTronco.add(beaconB);
        TroncoEntity tronco = new TroncoEntity(
                Integer.parseInt(rs.get(0).get("id").toString()),
                Boolean.parseBoolean(rs.get(0).get("agibile").toString()),
                estremiTronco,
                Float.parseFloat(rs.get(0).get("area").toString())
        );
        return tronco;
    }

    /**
     * Permette di ottenere un tronco, a partire dal suo identificatore, indipendentemente dalla direzione
     * @param troncoId identificatore del tronco
     * @param direzione variabile che indica se il tronco deve essere recuperato nello stesso verso
     *                 in cui è memorizzato nel database o nel verso opposto
     * @param db parametro utilizzato per la connessione al database
     * @return oggetto di tipo TroncoEntity con le relative info
     */
    @Override
    public TroncoEntity getTroncoById(String troncoId, boolean direzione, Connection db) {
        tabella.select();
        tabella.where("id = '" + troncoId + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        ArrayList<BeaconEntity> estremiTronco = new ArrayList<>();
        BeaconDAO beaconDAO = new BeaconDAO();
        BeaconEntity beaconA;
        BeaconEntity beaconB;
        if(direzione) {
            beaconA = beaconDAO.getBeaconById(rs.get(0).get("beaconAId").toString(), db);
            beaconB = beaconDAO.getBeaconById(rs.get(0).get("beaconBId").toString(), db);
        } else {
            beaconA = beaconDAO.getBeaconById(rs.get(0).get("beaconBId").toString(), db);
            beaconB = beaconDAO.getBeaconById(rs.get(0).get("beaconAId").toString(), db);
        }
        estremiTronco.add(beaconA);
        estremiTronco.add(beaconB);
        TroncoEntity tronco = new TroncoEntity(
                Integer.parseInt(rs.get(0).get("id").toString()),
                Boolean.parseBoolean(rs.get(0).get("agibile").toString()),
                estremiTronco,
                Float.parseFloat(rs.get(0).get("area").toString())
        );
        return tronco;
    }

    /**
     * Permette di verificare se esiste un tronco con una precisa direzione
     * @param troncoOttimo tronco di cui si vuole verificare la presenza nel database
     * @param db parametro utilizzato per la connessione al database
     * @return True se quel tronco esiste, false altrimenti
     */
    @Override
    public boolean checkDirezioneTronco(TroncoEntity troncoOttimo, Connection db) {
        boolean success = false;
        tabella.select();
        tabella.where("id='" + troncoOttimo.getId() + "' AND beaconAId = '" + troncoOttimo.getBeaconEstremi().get(0).getId() + "' and beaconBId = '" + troncoOttimo.getBeaconEstremi().get(1).getId() + "'" );
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    /**
     * Permette di recuperare tutte le informazioni di tutti i tronchi che sono stati modificati
     * dopo un certo timestamp, sotto forma di json
     * @param timestamp orario usato come soglia
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray che contiene le info di tutti quei tronchi modificati dopo un certo timestamp
     */
    @Override
    public JsonArray getAllTronchiAggiornati(Timestamp timestamp, Connection db) {
        JsonArrayBuilder tronchiAggiornati = Json.createArrayBuilder();
        tabella.select();
        tabella.where("last_update>'"+timestamp+"'");
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            tronchiAggiornati.add(Json.createObjectBuilder()
                                .add("id",rs.get(i).get("id").toString())
                                .add("beaconAId",rs.get(i).get("beaconAId").toString())
                                .add("beaconBId",rs.get(i).get("beaconBId").toString())
                                .add("agibile",rs.get(i).get("agibile").toString())
                                .add("area",rs.get(i).get("area").toString())
                                );
        }
        return tronchiAggiornati.build();
    }

    /**
     * Permette di generare un json contenente le info di tutti i tronchi
     * @param db parametro utilizzato per la connessione al database
     * @return JsonArray contenenti le informazioni di tutti i tronchi memorizzati nel db
     */
    @Override
    public JsonArray getTable(Connection db) {
        JsonArrayBuilder tronchi = Json.createArrayBuilder();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            tronchi.add(Json.createObjectBuilder()
                    .add("id",rs.get(i).get("id").toString())
                    .add("beaconAId",rs.get(i).get("beaconAId").toString())
                    .add("beaconBId",rs.get(i).get("beaconBId").toString())
                    .add("agibile",rs.get(i).get("agibile").toString())
                    .add("area",rs.get(i).get("area").toString())
            );
        }
        return tronchi.build();
    }

    /**
     * Permette di inserire un'insieme di tronchi
     * @param tronchi lista di oggetti di tipo TroncoEntity contententi le info dei tronchi da inserire
     * @param db parametro utilizzato per la connessione al database
     * @return ArrayList di interi contenenti gli identificatori dei tronchi appena inseriti
     */
    @Override
    public ArrayList<Integer> inserisciTronchi(ArrayList<TroncoEntity> tronchi, Connection db){
        ArrayList<Integer> idTronchi = new ArrayList<>();
        int id;
        for(TroncoEntity tronco : tronchi){
            int agibile = tronco.isAgibile() ? 1 : 0;
            String dati= String.valueOf(tronco.getId());
            dati=dati+",'"+tronco.getBeaconEstremi().get(0).getId()+"'";
            dati=dati+",'"+tronco.getBeaconEstremi().get(1).getId()+"'";
            dati=dati+",'"+agibile+"'";
            dati=dati+",'"+tronco.getArea()+"'";
            dati=dati+",null";
            tabella.insert(dati);
            id = tabella.executeForKey(db);
            idTronchi.add(id);
        }
        return idTronchi;
    }

    /**
     * Permette di eliminare tutti i tronchi relativi ad un piano
     * @param pianoId identificatore del piano
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void eliminaTronchiPerPiano(int pianoId, Connection db){    //se dovesse cancellare i tronchi sbagliati bisogna fare come nel select di gettronchiPiano
        tabella.delete2();
        tabella.innerjoin("beacon", "tronco.beaconAId = beacon.id");
        tabella.where("beacon.pianoId = '" + pianoId +"'");
        tabella.execute(db);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        GestioneDB.setLast_time_deleted(time);
    }

    /**
     * Permette di ottere gli identificatori di tutti i tronchi memorizzati
     * @param db parametro utilizzato per la connessione al database
     * @return ArrayList di interi contenente gli identificatori di tutti i tronchi presenti nel database
     */
    @Override
    public ArrayList<Integer> getAllIdTronchi(Connection db) {
        ArrayList<Integer> idTronchi = new ArrayList<>();
        tabella.select();
        List<Map<String, Object>> rs = tabella.fetch(db);
        for (int i = 0; i<rs.size(); i++) {
            idTronchi.add(Integer.parseInt(rs.get(i).get("id").toString()));
        }
        return idTronchi;
    }
}
