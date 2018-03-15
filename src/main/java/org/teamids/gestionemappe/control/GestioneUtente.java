package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

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
                //this.is_autenticato = true;
                //utenteDAO.update(utente);
                Gson gson = new Gson();
                esito = gson.toJson(this);
            }
            else
                esito = "{\"esito\": \"ERROR: Password errata\"}";
        }
        else
            esito = "{\"esito\": \"ERROR: UtenteEntity non trovato\"}";
        return esito;
    }

    public void autenticazioneUtente(UtenteEntity utente){
        
    }
}
