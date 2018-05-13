package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.DbTable.PesiTronco;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Set;


public class GestioneDB {

    private TroncoDAO troncoDAO;
    private PianoDAO pianoDAO;
    private BeaconDAO beaconDAO;
    private PesoDAO pesoDAO;
    private PesiTroncoDAO pesiTroncoDAO;
    private static Timestamp last_time_deleted;

    public GestioneDB(){

        this.troncoDAO = new TroncoDAO();
        this.pianoDAO = new PianoDAO();
        this.beaconDAO = new BeaconDAO();
        this.pesoDAO = new PesoDAO();
        this.pesiTroncoDAO = new PesiTroncoDAO();
    }

    public ArrayList<PianoEntity> getPiani(){
        return pianoDAO.getAllPiani();
    }

    public HashMap<TroncoEntity, HashMap<String, Float>> getTronchiPiano(int pianoId){

        HashMap<TroncoEntity, HashMap<String, Float>> troncopesi = new HashMap<>();
        Set<TroncoEntity> tronchi = troncoDAO.getTronchiPiano(pianoId);
        ArrayList<String> nomiPesi = pesoDAO.getPesiNames();
        for(TroncoEntity tronco : tronchi){
            HashMap<String, Float> nomeval = new HashMap<>();
            for(String peso : nomiPesi) {
                nomeval.put(peso, pesiTroncoDAO.geValoreByPesoId(tronco.getId(), peso));
            }
            troncopesi.put(tronco, nomeval);
        }

        return troncopesi;

    }

    public void aggiornaPesi(String peso, int troncoId, float valore){

        pesiTroncoDAO.aggiornaPesiTronco(troncoId, peso, valore);
    }

    public String aggiornaDB(Timestamp timestamp_client){
        if(last_time_deleted!= null && timestamp_client.before(last_time_deleted)){
            JsonArray tronchiTable = troncoDAO.getTable();
            JsonArray pianiTable = pianoDAO.getTable();
            JsonArray beaconTable = beaconDAO.getTable();
            JsonArray pesoTable = pesoDAO.getTable();
            JsonArray pesitroncoTable = pesiTroncoDAO.getTable();
            JsonObject db = Json.createObjectBuilder()
                    .add("tipologia", "ricrea")
                    .add("tronco", tronchiTable)
                    .add("piano", pianiTable)
                    .add("beacon", beaconTable)
                    .add("peso", pesoTable)
                    .add("pesitronco", pesitroncoTable)
                    .build();
            return db.toString();
        } else {
            JsonArray tronchiaggionati = troncoDAO.getAllTronchiAggiornati(timestamp_client);
            JsonArray pianiaggionati = pianoDAO.getAllPianiAggiornati(timestamp_client);
            JsonArray beaconaggiornati = beaconDAO.getAllBeaconAggiornati(timestamp_client);
            JsonArray pesoaggiornati = pesoDAO.getAllPesiAggiornati(timestamp_client);
            JsonArray pesitroncoaggiornati = pesiTroncoDAO.getAllPesiTroncoAggiornati(timestamp_client);
            JsonObject dbAggiornato = Json.createObjectBuilder()
                    .add("tipologia", "modifica")
                    .add("tronco", tronchiaggionati)
                    .add("piano", pianiaggionati)
                    .add("beacon", beaconaggiornati)
                    .add("peso", pesoaggiornati)
                    .add("pesitronco", pesitroncoaggiornati)
                    .build();
            return dbAggiornato.toString();
        }
    }

    public String aggiungiPiano(String path, com.google.gson.JsonObject jsonRequest){

        int numeropiano = jsonRequest.get("piano").getAsInt();
        String immagine =  jsonRequest.get("immagine").getAsString().split(",")[1];
        PianoEntity newpiano = new PianoEntity(immagine, numeropiano);

        PianoDAO pianoDAO = new PianoDAO();
        pianoDAO.inserisciPiano(newpiano);

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

                BeaconEntity beacon = new BeaconEntity(field[0], isPuntodiRaccolta, newpiano, Float.parseFloat(field[2]), Float.parseFloat(field[3]));

                nuoviBeacon.add(beacon);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        BeaconDAO beaconDAO = new BeaconDAO();
        beaconDAO.inserisciBeacons(nuoviBeacon);

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
        troncoDAO.inserisciTronchi(nuoviTronchi);

        return null;

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
