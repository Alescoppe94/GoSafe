package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.util.HashMap;

import static java.sql.Types.NULL;

public class GestioneUtente {

    public GestioneUtente() {
    }

    public String loginUtente(UtenteEntity utente){
        UtenteDAO utenteDAO = new UtenteDAO();
        UtenteEntity utentedb = utenteDAO.getUserByUsername(utente.getUsername());
        String esito;
        if(utentedb != null) {
            if(utente.getPassword().equals(utentedb.getPassword())) {
                utente.setId(utentedb.getId());
                utente.setNome(utentedb.getNome());
                utente.setCognome(utentedb.getCognome());
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
        UtenteDAO utenteDAO = new UtenteDAO();
        boolean isAutenticato = utenteDAO.findUserByCredential(utente.getUsername(),utente.getPassword());
        return isAutenticato;
    }

    public String registrazioneUtente(UtenteEntity utente){
        UtenteDAO utenteDAO = new UtenteDAO();
        boolean isUserInDb = utenteDAO.findUserByUsername(utente.getUsername());
        String esito;
        if(isUserInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            utenteDAO.insertUser(utente);
            int id_utente = utenteDAO.getUserByUsername(utente.getUsername()).getId();
            esito = "{\"id_utente\": \""+ id_utente +"\"}";
        }
        return esito;
    }

    public void logoutUtente(UtenteEntity utente){
        UtenteDAO utenteDAO = new UtenteDAO();
        utenteDAO.logout(utente.getUsername());
    }

}
