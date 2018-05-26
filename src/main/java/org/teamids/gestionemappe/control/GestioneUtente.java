package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.Connection;
import java.util.HashMap;

public class GestioneUtente{

    private UtenteDAO utenteDAO;
    private BeaconDAO beaconDAO;

    public GestioneUtente() {
        this.utenteDAO = new UtenteDAO();
        this.beaconDAO = new BeaconDAO();
    }

    public String loginUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        UtenteEntity utentedb = utenteDAO.getUserByUsername(utente.getUsername(), db);
        String esito;
        if(utentedb != null) {
            if(utente.getPassword().equals(utentedb.getPassword())) {
                utente.setId(utentedb.getId());
                utente.setNome(utentedb.getNome());
                utente.setCognome(utentedb.getCognome());
                utente.setIs_autenticato(true);
                HashMap<String, Object> campo = new HashMap<>();
                campo.put("is_autenticato", 1);
                if(utente.getToken()!=null)
                    campo.put("token", utente.getToken());
                utenteDAO.updateInfoUtente(utente.getId(), campo, db);
                Gson gson = new Gson();
                esito = gson.toJson(utente);
            }
            else
                esito = "{\"esito\": \"ERROR: Password errata\"}";
        }
        else
            esito = "{\"esito\": \"ERROR: Utente non trovato\"}";
        connector.disconnect();
        return esito;
    }

    public boolean autenticazioneUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        boolean isAutenticato = utenteDAO.isAutenticato(utente.getUsername(),utente.getPassword(), db);

        connector.disconnect();
        return isAutenticato;
    }

    public String registrazioneUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        boolean isUserInDb = utenteDAO.findUserByUsername(utente.getUsername(), db);
        String esito;
        if(isUserInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            utenteDAO.insertUser(utente, db);
            int id_utente = utenteDAO.getUserByUsername(utente.getUsername(), db).getId();
            esito = "{\"id_utente\": \""+ id_utente +"\"}";
        }
        connector.disconnect();
        return esito;
    }

    public String updateUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        UtenteDAO utenteDAO = new UtenteDAO();
        boolean isUsernameInDb = utenteDAO.findUserByUsername(utente.getUsername(), db) && !utenteDAO.isUsernameIdPresent(utente.getUsername(), utente.getId(), db);
        String esito;
        if(isUsernameInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            HashMap<String, Object> campo = new HashMap<>();
            campo.put("username", utente.getUsername());
            campo.put("password", utente.getPassword());
            campo.put("nome", utente.getNome());
            campo.put("cognome", utente.getCognome());
            utenteDAO.updateInfoUtente(utente.getId(), campo, db);
            esito = "{\"esito\": \"success\"}";
        }
        connector.disconnect();
        return esito;
    }

    public void logoutUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        utenteDAO.logout(utente.getUsername(), db);
        connector.disconnect();
    }

    public void updateUserPosition(UtenteEntity utente) {
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        HashMap<String, Object> campo = new HashMap<>();
        campo.put("beaconId", utente.getBeaconId());
        utenteDAO.updateInfoUtente(utente.getId(),campo, db);

        connector.disconnect();
    }
}
