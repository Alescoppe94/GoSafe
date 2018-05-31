package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.DbTable.PesiTronco;
import org.teamids.gestionemappe.model.DbTable.Peso;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PianoEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;


public class GestioneDB {

    private TroncoDAO troncoDAO;
    private PianoDAO pianoDAO;
    private BeaconDAO beaconDAO;
    private PesoDAO pesoDAO;
    private PesiTroncoDAO pesiTroncoDAO;
    private UtenteDAO utenteDAO;
    private static Timestamp last_time_deleted;

    public GestioneDB(){

        this.troncoDAO = new TroncoDAO();
        this.pianoDAO = new PianoDAO();
        this.beaconDAO = new BeaconDAO();
        this.pesoDAO = new PesoDAO();
        this.pesiTroncoDAO = new PesiTroncoDAO();
        this.utenteDAO = new UtenteDAO();
    }

    public Map<String, Integer> getAllPiani(){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        ArrayList<PianoEntity> piani = pianoDAO.getAllPiani(db);
        Map<String, Integer> model = new HashMap<>();
        for(PianoEntity piano : piani){
            model.put(String.valueOf(piano.getId()), piano.getPiano());
        }
        connector.disconnect();
        return model;
    }

    //public ArrayList<PianoEntity> getPiani(){
     //   return pianoDAO.getAllPiani();
    //}

    public Map<BeaconEntity, Integer> getPeoplePerBeacon(){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        ArrayList<BeaconEntity> beacons = beaconDAO.getAllBeacons(db);
        Map<BeaconEntity, Integer> numPersBeacon = new HashMap<>();

        for(BeaconEntity beacon : beacons){

            int numeroPersone = utenteDAO.countUsersPerBeacon(beacon.getId(), db);
            if(numeroPersone != 0) {
                numPersBeacon.put(beacon, numeroPersone);
            }

        }

        connector.disconnect();
        return numPersBeacon;
    }

    public HashMap<TroncoEntity, HashMap<String, Float>> getTronchiPiano(int pianoId){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        HashMap<TroncoEntity, HashMap<String, Float>> troncopesi = new HashMap<>();
        ArrayList<TroncoEntity> tronchi = troncoDAO.getTronchiPiano(pianoId, db);
        Map<Integer, Map<String,Float>> nomiPesi = pesoDAO.getPesi(db);
        for(TroncoEntity tronco : tronchi){
            HashMap<String, Float> nomeval = new HashMap<>();
            for(Map.Entry<Integer, Map<String,Float>> peso : nomiPesi.entrySet()) {
                Map.Entry<String, Float> entry = peso.getValue().entrySet().iterator().next();
                nomeval.put(entry.getKey(), pesiTroncoDAO.geValoreByPesoId(tronco.getId(), entry.getKey(), db));
            }
            troncopesi.put(tronco, nomeval);
        }
        connector.disconnect();
        return troncopesi;

    }

    public void aggiornaPesiTronco(String peso, int troncoId, float valore){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        pesiTroncoDAO.aggiornaPesiTronco(troncoId, peso, valore, db);
        connector.disconnect();
    }

    public String aggiornaDB(Timestamp timestamp_client){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        if(last_time_deleted!= null && timestamp_client.before(last_time_deleted)){
            JsonArray tronchiTable = troncoDAO.getTable(db);
            JsonArray pianiTable = pianoDAO.getTable(db);
            JsonArray beaconTable = beaconDAO.getTable(db);
            JsonArray pesoTable = pesoDAO.getTable(db);
            JsonArray pesitroncoTable = pesiTroncoDAO.getTable(db);
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
        } else {
            JsonArray tronchiaggionati = troncoDAO.getAllTronchiAggiornati(timestamp_client, db);
            JsonArray pianiaggionati = pianoDAO.getAllPianiAggiornati(timestamp_client, db);
            JsonArray beaconaggiornati = beaconDAO.getAllBeaconAggiornati(timestamp_client, db);
            JsonArray pesoaggiornati = pesoDAO.getAllPesiAggiornati(timestamp_client, db);
            JsonArray pesitroncoaggiornati = pesiTroncoDAO.getAllPesiTroncoAggiornati(timestamp_client, db);
            if(tronchiaggionati.size() == 0 && pianiaggionati.size() == 0 && beaconaggiornati.size() == 0 && pesoaggiornati.size() == 0 && pesitroncoaggiornati.size() == 0){
                return null;        //da vedere se piace
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

    public String aggiungiPiano(String path, com.google.gson.JsonObject jsonRequest){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        int numeropiano = jsonRequest.get("piano").getAsInt();
        String immagine =  jsonRequest.get("immagine").getAsString().split(",")[1];
        PianoEntity newpiano = new PianoEntity(immagine, numeropiano);

        PianoDAO pianoDAO = new PianoDAO();
        pianoDAO.inserisciPiano(newpiano, db);

        creaFileCsv(path, "beaconcsv", jsonRequest);
        creaFileCsv(path, "troncocsv", jsonRequest);

        String line = "";
        String cvsSplitBy = ",";

        ArrayList<BeaconEntity> nuoviBeacon = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path+"beaconcsv.csv"))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] field = line.split(cvsSplitBy);

                boolean isPuntodiRaccolta = "1".equals(field[1]);

                BeaconEntity beacon = new BeaconEntity(field[0], isPuntodiRaccolta, numeropiano, Integer.parseInt(field[2]), Integer.parseInt(field[3]));

                nuoviBeacon.add(beacon);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        BeaconDAO beaconDAO = new BeaconDAO();
        beaconDAO.inserisciBeacons(nuoviBeacon, db);

        ArrayList<TroncoEntity> nuoviTronchi = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path+"troncocsv.csv"))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] field = line.split(cvsSplitBy);

                ArrayList<BeaconEntity> beaconEstremi = new ArrayList<>();
                beaconEstremi.add(new BeaconEntity(field[0]));
                beaconEstremi.add(new BeaconEntity(field[1]));

                boolean agibile = "1".equals(field[2]);

                TroncoEntity tronco = new TroncoEntity(agibile, beaconEstremi, Float.parseFloat(field[3]));

                nuoviTronchi.add(tronco);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        TroncoDAO troncoDAO = new TroncoDAO();
        troncoDAO.inserisciTronchi(nuoviTronchi, db);

        connector.disconnect();

        return null;

    }

    public void eliminaPiano(int idPiano){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        pesiTroncoDAO.eliminaPesiTronco(idPiano, db);
        troncoDAO.eliminaTronchiPerPiano(idPiano, db);
        pianoDAO.eliminaPiano(idPiano, db);
        beaconDAO.eliminaBeaconsPerPiano(idPiano, db);

        connector.disconnect();

    }

    public void aggiornaPesi(int id, String nome, Float valore){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        pesoDAO.aggiornaPeso(id, nome, valore, db);
        connector.disconnect();

    }

    public void inserisciPeso(ArrayList<String> peso){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        pesoDAO.inserisciPeso(peso.get(1), Float.valueOf(peso.get(0)), db);
        connector.disconnect();

    }

    public String downloadDb(){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        JsonArray tronchiTable = troncoDAO.getTable(db);
        JsonArray pianiTable = pianoDAO.getTable(db);
        JsonArray beaconTable = beaconDAO.getTable(db);
        JsonArray pesoTable = pesoDAO.getTable(db);
        JsonArray pesitroncoTable = pesiTroncoDAO.getTable(db);
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

    public void eliminapeso(int idPeso){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        pesiTroncoDAO.eliminaPesiTroncoByPiano(idPeso, db);
        pesoDAO.eliminaPeso(idPeso, db);

        connector.disconnect();

    }

    public Map<Integer, Map<String,Float>> mostraPesi(){

        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        Map<Integer, Map<String,Float>> pesi = pesoDAO.getPesi(db);

        connector.disconnect();
        return pesi;

    }

    private void creaFileCsv(String path, String filename, com.google.gson.JsonObject jsonRequest) {
        String base64 = jsonRequest.get(filename).getAsString().split(",")[1];
        byte[] decoded = Base64.getDecoder().decode(base64);
        try (FileOutputStream fos = new FileOutputStream(path + filename +".csv")) {
            fos.write(decoded);
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
