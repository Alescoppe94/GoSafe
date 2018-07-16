package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PianoEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;


/**
 * Classe che si occupa di implementare i metodi utili alla gestione del database
 * e all'ottenimento di informazioni presenti nel database
 */
public class GestioneDB implements GestioneDBInterface {

    private TroncoDAOInterface troncoDAOInterface;
    private PianoDAOInterface pianoDAOInterface;
    private BeaconDAOInterface beaconDAOInterface;
    private PesoDAOInterface pesoDAOInterface;
    private PesiTroncoDAOInterface pesiTroncoDAOInterface;
    private UtenteDAOInterface utenteDAOInterface;
    private static Timestamp last_time_deleted;

    /**
     * Costruttore della classe GestioneDB
     */
    public GestioneDB(){

        this.troncoDAOInterface = new TroncoDAO();
        this.pianoDAOInterface = new PianoDAO();
        this.beaconDAOInterface = new BeaconDAO();
        this.pesoDAOInterface = new PesoDAO();
        this.pesiTroncoDAOInterface = new PesiTroncoDAO();
        this.utenteDAOInterface = new UtenteDAO();
    }

    /**
     * Recupera tutti i piani con le relative informazioni
     * @return la lista dei piani
     */
    @Override
    public Map<String, Integer> getAllPiani(){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        ArrayList<PianoEntity> piani = pianoDAOInterface.getAllPiani(db);
        Map<String, Integer> model = new HashMap<>();
        for(PianoEntity piano : piani){
            model.put(String.valueOf(piano.getId()), piano.getPiano());
        }
        connector.disconnect();
        return model;
    }

    /**
     * Recupera il numero di utenti collegati ad ogni beacon
     * @return la lista dei beacon e per ogni beacon il numero di utenti connessi
     */
    @Override
    public Map<BeaconEntity, Integer> getPeoplePerBeacon(){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        ArrayList<BeaconEntity> beacons = beaconDAOInterface.getAllBeacons(db);
        Map<BeaconEntity, Integer> numPersBeacon = new HashMap<>();

        for(BeaconEntity beacon : beacons){
            int numeroPersone = utenteDAOInterface.countUsersPerBeacon(beacon.getId(), db);
            if(numeroPersone != 0) {
                numPersBeacon.put(beacon, numeroPersone);
            }
        }

        connector.disconnect();
        return numPersBeacon;
    }

    /**
     * Recupera tutti i tronchi di un piano e i relativi pesi
     * @param pianoId l'id del piano di cui si vuole avere le informazioni
     * @return la lista dei tronchi del piano e per ogni tronco i pesi associati
     */
    @Override
    public HashMap<TroncoEntity, HashMap<String, Float>> getTronchiPiano(int pianoId){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        HashMap<TroncoEntity, HashMap<String, Float>> troncopesi = new HashMap<>();
        ArrayList<TroncoEntity> tronchi = troncoDAOInterface.getTronchiPiano(pianoId, db);
        Map<Integer, Map<String,Float>> nomiPesi = pesoDAOInterface.getPesi(db);
        for(TroncoEntity tronco : tronchi){
            HashMap<String, Float> nomeval = new HashMap<>();
            for(Map.Entry<Integer, Map<String,Float>> peso : nomiPesi.entrySet()) {
                Map.Entry<String, Float> entry = peso.getValue().entrySet().iterator().next();
                nomeval.put(entry.getKey(), pesiTroncoDAOInterface.geValoreByPesoId(tronco.getId(), entry.getKey(), db));
            }
            troncopesi.put(tronco, nomeval);
        }
        connector.disconnect();
        return troncopesi;

    }

    /**
     * Aggiorna il valore di un peso di un tronco
     * @param peso il peso che si vuole modificare
     * @param troncoId l'id del tronco che si vuole aggiornare
     * @param valore il nuovo valore del peso
     */
    @Override
    public void aggiornaPesiTronco(String peso, int troncoId, float valore){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        pesiTroncoDAOInterface.aggiornaPesiTronco(troncoId, peso, valore, db);
        connector.disconnect();
    }

    /**
     * Recupera le informazioni aggiornate rispetto a un orario
     * @param timestamp_client orario di ultimo aggiornamento del database del client che fa la richiesta
     * @return le informazioni aggiornate sotto forma di json
     */
    @Override
    public String aggiornaDB(Timestamp timestamp_client){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        /* Se l'ultimo orario di aggiornamento del database del client è precedente a quello di un'eliminazione di una riga dal
           database del server, viene restituito l'intero database, specificando al client che dovrà ricrearlo */
        if(GestioneDB.getLast_time_deleted()!= null && timestamp_client.before(GestioneDB.getLast_time_deleted())){
            JsonArray tronchiTable = troncoDAOInterface.getTable(db);
            JsonArray pianiTable = pianoDAOInterface.getTable(db);
            JsonArray beaconTable = beaconDAOInterface.getTable(db);
            JsonArray pesoTable = pesoDAOInterface.getTable(db);
            JsonArray pesitroncoTable = pesiTroncoDAOInterface.getTable(db);
            JsonObject database = Json.createObjectBuilder()
                    .add("tipologia", "ricrea")
                    .add("tronco", tronchiTable)
                    .add("piano", pianiTable)
                    .add("beacon", beaconTable)
                    .add("peso", pesoTable)
                    .add("pesitronco", pesitroncoTable)
                    .build();
            connector.disconnect();
            return database.toString();
        }
        /* Altrimenti verranno restituite soltanto le informazioni aggiornate delle tabelle utili */
        else {
            JsonArray tronchiaggionati = troncoDAOInterface.getAllTronchiAggiornati(timestamp_client, db);
            JsonArray pianiaggionati = pianoDAOInterface.getAllPianiAggiornati(timestamp_client, db);
            JsonArray beaconaggiornati = beaconDAOInterface.getAllBeaconAggiornati(timestamp_client, db);
            JsonArray pesoaggiornati = pesoDAOInterface.getAllPesiAggiornati(timestamp_client, db);
            JsonArray pesitroncoaggiornati = pesiTroncoDAOInterface.getAllPesiTroncoAggiornati(timestamp_client, db);
            /* Se nessuna riga è stata aggiornata a seguito dell'orario fornito dal client, non verrà restituito nulla */
            if(tronchiaggionati.size() == 0 && pianiaggionati.size() == 0 && beaconaggiornati.size() == 0 && pesoaggiornati.size() == 0 && pesitroncoaggiornati.size() == 0){
                return null;
            }else {
                JsonObject dbAggiornato = Json.createObjectBuilder()
                        .add("tipologia", "modifica")
                        .add("tronco", tronchiaggionati)
                        .add("piano", pianiaggionati)
                        .add("beacon", beaconaggiornati)
                        .add("peso", pesoaggiornati)
                        .add("pesitronco", pesitroncoaggiornati)
                        .build();
                connector.disconnect();
                return dbAggiornato.toString();
            }
        }
    }

    /**
     * Aggiunge un piano nel database
     * @param path path in cui andare a salvare i file csv dei tronchi e dei beacon
     * @param jsonRequest richiesta json contenente le informazioni del piano da aggiungere
     * @return lista dei beacon doppi, cioè che erano già presenti nel database
     */
    @Override
    public ArrayList<String> aggiungiPiano(String path, com.google.gson.JsonObject jsonRequest){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        int numeropiano = jsonRequest.get("piano").getAsInt();
        String immagine =  jsonRequest.get("immagine").getAsString().split(",")[1];

        PianoEntity newpiano = new PianoEntity(immagine, numeropiano);

        PianoDAOInterface pianoDAOInterface = new PianoDAO();
        int piano_id = pianoDAOInterface.inserisciPiano(newpiano, db);

        creaFileCsv(path, "beaconcsv", jsonRequest);
        creaFileCsv(path, "troncocsv", jsonRequest);

        String line = "";
        String cvsSplitBy = ",";

        ArrayList<BeaconEntity> nuoviBeacon = new ArrayList<>();

        /* Legge il file csv dei beacon, estrae una riga alla volta che corrisponde a un beacon e lo inserisce nel database */
        try (BufferedReader br = new BufferedReader(new FileReader(path+"beaconcsv.csv"))) {

            boolean firstLine = true;
            while ((line = br.readLine()) != null) {

                if(!firstLine) {
                    String[] field = line.split(cvsSplitBy);

                    boolean isPuntodiRaccolta = "1".equals(field[1]);

                    BeaconEntity beacon = new BeaconEntity(field[0], isPuntodiRaccolta, piano_id, Integer.parseInt(field[2]), Integer.parseInt(field[3]));

                    nuoviBeacon.add(beacon);
                }
                firstLine = false;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        BeaconDAO beaconDAOInterface = new BeaconDAO();
        ArrayList<String> beaconDoppi = beaconDAOInterface.inserisciBeacons(nuoviBeacon, piano_id, db);

        ArrayList<TroncoEntity> nuoviTronchi = new ArrayList<>();

        /* Legge il file csv dei tronchi, estrae una riga alla volta che corrisponde a un tronco e lo inserisce nel database */
        try (BufferedReader br = new BufferedReader(new FileReader(path+"troncocsv.csv"))) {

            boolean firstLine = true;
            while ((line = br.readLine()) != null) {

                if(!firstLine) {
                    String[] field = line.split(cvsSplitBy);

                    ArrayList<BeaconEntity> beaconEstremi = new ArrayList<>();
                    beaconEstremi.add(new BeaconEntity(field[0]));
                    beaconEstremi.add(new BeaconEntity(field[1]));

                    boolean agibile = true; //tutti i tronchi sono agibili, dal momento che non consideriamo più questa caratteristica

                    TroncoEntity tronco = new TroncoEntity(agibile, beaconEstremi, Float.parseFloat(field[2]));

                    nuoviTronchi.add(tronco);
                }
                firstLine = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        TroncoDAOInterface troncoDAOInterface = new TroncoDAO();
        ArrayList<Integer> idTronchi = troncoDAOInterface.inserisciTronchi(nuoviTronchi, db);

        PesiTroncoDAOInterface pesiTroncoDAOInterface = new PesiTroncoDAO();
        pesiTroncoDAOInterface.inserisciPesiTronco(idTronchi, db);

        connector.disconnect();

        return beaconDoppi;

    }

    /**
     * Elimina un piano dal database, eliminando di conseguenza i beacon, i tronchi e i pesi dei tronchi associati al piano
     * @param idPiano l'id del piano da eliminare
     */
    @Override
    public void eliminaPiano(int idPiano){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        pesiTroncoDAOInterface.eliminaPesiTroncoPerPiano(idPiano, db);
        troncoDAOInterface.eliminaTronchiPerPiano(idPiano, db);
        pianoDAOInterface.eliminaPiano(idPiano, db);
        beaconDAOInterface.eliminaBeaconsPerPiano(idPiano, db);

        connector.disconnect();

    }

    /**
     * Aggiorna il peso di un parametro dei tronchi
     * @param id l'id del peso da aggiornare
     * @param valore il nuovo valore del peso
     */
    @Override
    public void aggiornaPesi(int id, Float valore){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        pesoDAOInterface.aggiornaPeso(id, valore, db);
        connector.disconnect();

    }

    /**
     * Inserisce un nuovo peso per i tronchi
     * @param peso le informazioni del nuovo peso, cioè il nome e il valore
     */
    @Override
    public void inserisciPeso(ArrayList<String> peso){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        int idPeso = pesoDAOInterface.inserisciPeso(peso.get(1), Float.valueOf(peso.get(0)), db);
        pesiTroncoDAOInterface.inserisciPesiTroncoPerNuovoPeso(idPeso, db);
        connector.disconnect();

    }

    /**
     * Recupera le tabelle del database del server utili al client
     * @return
     */
    @Override
    public String downloadDb(){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        JsonArray tronchiTable = troncoDAOInterface.getTable(db);
        JsonArray pianiTable = pianoDAOInterface.getTable(db);
        JsonArray beaconTable = beaconDAOInterface.getTable(db);
        JsonArray pesoTable = pesoDAOInterface.getTable(db);
        JsonArray pesitroncoTable = pesiTroncoDAOInterface.getTable(db);
        JsonObject database = Json.createObjectBuilder()
                .add("tronco", tronchiTable)
                .add("piano", pianiTable)
                .add("beacon", beaconTable)
                .add("peso", pesoTable)
                .add("pesitronco", pesitroncoTable)
                .build();
        connector.disconnect();
        return database.toString();
    }

    /**
     * Elimina un peso già presente
     * @param idPeso l'id del peso da eliminare
     */
    @Override
    public void eliminapeso(int idPeso){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        pesiTroncoDAOInterface.eliminaPesiTroncoByPeso(idPeso, db);
        pesoDAOInterface.eliminaPeso(idPeso, db);
        connector.disconnect();

    }

    /**
     * Recupera i pesi dei parametri dei tronchi
     * @return la lista dei pesi con informazioni quali id, nome e valore
     */
    @Override
    public Map<Integer, Map<String,Float>> mostraPesi(){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        Map<Integer, Map<String,Float>> pesi = pesoDAOInterface.getPesi(db);
        connector.disconnect();
        return pesi;

    }

    /**
     * Scrive un file csv, usato per i beacon e per i tronchi
     * @param path il path in cui scrivere il file
     * @param filename il nome del file da scrivere
     * @param jsonRequest le informazioni che saranno memorizzate nel file
     */
    private void creaFileCsv(String path, String filename, com.google.gson.JsonObject jsonRequest) {
        String base64 = jsonRequest.get(filename).getAsString().split(",")[1];
        byte[] decoded = Base64.getDecoder().decode(base64);
        try (FileOutputStream fos = new FileOutputStream(path + filename +".csv")) {
            fos.write(decoded);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo getter per l'attributo last_time_deleted
     * @return valore dell'attributo
     */
    public static Timestamp getLast_time_deleted() {
        return last_time_deleted;
    }

    /**
     * Metodo setter per l'attributo last_time_deleted
     * @param last_time_deleted nuovo valore per l'attributo
     */
    public static void setLast_time_deleted(Timestamp last_time_deleted) {
        GestioneDB.last_time_deleted = last_time_deleted;
    }
}
