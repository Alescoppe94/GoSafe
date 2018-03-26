package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.util.HashMap;

public class GestioneUtente {

    public GestioneUtente() {
    }

    public String loginUtente(UtenteEntity utente){
        UtenteEntity utentedb = UtenteDAO.getUserByUsername(utente.getUsername());
        String esito;
        if(utentedb != null) {
            if(utente.getPassword().equals(utentedb.getPassword())) {
                utente.setId(utentedb.getId());
                utente.setNome(utentedb.getNome());
                utente.setCognome(utentedb.getCognome());
                utente.setIs_autenticato(true);
                HashMap<String, Object> campo = new HashMap<>();
                campo.put("is_autenticato", 1);
                UtenteDAO.updateInfoUtente(utente.getUsername(), campo); // TODO: aggiungere onTokenRefresh
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
        boolean isAutenticato = UtenteDAO.isAutenticato(utente.getUsername(),utente.getPassword());
        return isAutenticato;
    }

    public String registrazioneUtente(UtenteEntity utente){
        boolean isUserInDb = UtenteDAO.findUserByUsername(utente.getUsername());
        String esito;
        if(isUserInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            UtenteDAO.insertUser(utente); // TODO: aggiungere onTokenRefresh
            int id_utente = UtenteDAO.getUserByUsername(utente.getUsername()).getId();
            esito = "{\"id_utente\": \""+ id_utente +"\"}";
        }
        return esito;
    }

    public void logoutUtente(UtenteEntity utente){
        UtenteDAO.logout(utente.getUsername());
    }

}
