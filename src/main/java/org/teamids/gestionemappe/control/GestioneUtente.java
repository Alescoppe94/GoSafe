package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class GestioneUtente{

    private UtenteDAO utenteDAO;
    private BeaconDAO beaconDAO;

    public GestioneUtente() {
        this.utenteDAO = new UtenteDAO();
        this.beaconDAO = new BeaconDAO();
    }

    public String loginUtente(UtenteEntity utente){
        UtenteEntity utentedb = utenteDAO.getUserByUsername(utente.getUsername());
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
                utenteDAO.updateInfoUtente(utente.getId(), campo); // TODO: aggiungere onTokenRefresh
                Gson gson = new Gson();
                esito = gson.toJson(utente);
            }
            else
                esito = "{\"esito\": \"ERROR: Password errata\"}";
        }
        else
            esito = "{\"esito\": \"ERROR: Utente non trovato\"}";
        return esito;
    }

    public boolean autenticazioneUtente(UtenteEntity utente){
        boolean isAutenticato = utenteDAO.isAutenticato(utente.getUsername(),utente.getPassword());
        return isAutenticato;
    }

    public String registrazioneUtente(UtenteEntity utente){
        boolean isUserInDb = utenteDAO.findUserByUsername(utente.getUsername());
        String esito;
        if(isUserInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            utenteDAO.insertUser(utente); // TODO: aggiungere onTokenRefresh
            int id_utente = utenteDAO.getUserByUsername(utente.getUsername()).getId();
            esito = "{\"id_utente\": \""+ id_utente +"\"}";
        }
        return esito;
    }

    public String updateUtente(UtenteEntity utente){
        UtenteDAO utenteDAO = new UtenteDAO();
        boolean isUsernameInDb = utenteDAO.findUserByUsername(utente.getUsername()) && !utenteDAO.isUsernameIdPresent(utente.getUsername(), utente.getId());
        System.out.println("isUserinDB"+isUsernameInDb);
        String esito;
        if(isUsernameInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            System.out.println("Sono qua");
            HashMap<String, Object> campo = new HashMap<>();
            campo.put("username", utente.getUsername());
            campo.put("password", utente.getPassword());
            campo.put("nome", utente.getNome());
            campo.put("cognome", utente.getCognome());
            utenteDAO.updateInfoUtente(utente.getId(), campo);
            esito = "{\"esito\": \"success\"}";
        }
        System.out.println(esito);
        return esito;
    }

    public void logoutUtente(UtenteEntity utente){
        utenteDAO.logout(utente.getUsername());
    }

    public void updateUserPosition(UtenteEntity utente) {
        HashMap<String, Object> campo = new HashMap<>();
        campo.put("beaconId", utente.getBeaconId());
        utenteDAO.updateInfoUtente(utente.getId(),campo);
    }
}
