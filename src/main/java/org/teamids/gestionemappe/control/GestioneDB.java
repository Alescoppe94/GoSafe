package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.DAO.*;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Timestamp;


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
}
