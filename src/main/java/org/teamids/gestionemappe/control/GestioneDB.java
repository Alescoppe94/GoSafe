package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PianoEntity;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;


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

    public String aggiungiPiano(com.google.gson.JsonObject jsonRequest){

        int numeropiano = jsonRequest.get("piano").getAsInt();
        String immagine = jsonRequest.get("immagine").getAsString();
        PianoEntity newpiano = new PianoEntity(immagine, numeropiano);

        //TODO: inserire piano nel db

        String base64 = jsonRequest.get("file").getAsString().split(",")[1];
        byte[] decoded = Base64.getDecoder().decode(base64);
        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Alessandro\\IdeaProjects\\GoSafe\\docs\\test.csv")) {
            fos.write(decoded);
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }catch (Exception e){
            e.printStackTrace();
        }

        String csvFile = "C:\\Users\\Alessandro\\IdeaProjects\\GoSafe\\docs\\test.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] field = line.split(cvsSplitBy);

                BeaconEntity beacon = new BeaconEntity(field[0], Boolean.parseBoolean(field[1]), newpiano);

                //TODO: inserire beacon nel db

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
