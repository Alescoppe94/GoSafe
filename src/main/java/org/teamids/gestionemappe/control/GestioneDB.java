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

    public GestioneDB(){

        this.troncoDAO = new TroncoDAO();
        this.pianoDAO = new PianoDAO();
        this.beaconDAO = new BeaconDAO();
        this.pesoDAO = new PesoDAO();
        this.pesiTroncoDAO = new PesiTroncoDAO();
    }
    public String aggiornaDB(Timestamp timestamp){
        JsonArray tronchiaggionati = troncoDAO.getAllTronchiAggiornati(timestamp);
        JsonArray pianiaggionati = pianoDAO.getAllPianiAggiornati(timestamp);
        JsonArray beaconaggiornati = beaconDAO.getAllBeaconAggiornati(timestamp);
        JsonArray pesoaggiornati = pesoDAO.getAllPesiAggiornati(timestamp);
        JsonArray pesitroncoaggiornati = pesiTroncoDAO.getAllPesiTroncoAggiornati(timestamp);
        JsonObject dbAggiornato = Json.createObjectBuilder()
                .add("tronco",tronchiaggionati)
                .add("piano",pianiaggionati)
                .add("beacon",beaconaggiornati)
                .add("peso",pesoaggiornati)
                .add("pesitronco",pesitroncoaggiornati)
                .build();
        return dbAggiornato.toString();
    }
}
