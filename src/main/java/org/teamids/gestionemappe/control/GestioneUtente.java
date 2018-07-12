package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.security.SecureRandom;
import java.sql.Connection;
import java.util.HashMap;

public class GestioneUtente implements GestioneUtenteInterface {

    private UtenteDAOInterface utenteDAOInterface;
    private BeaconDAOInterface beaconDAOInterface;

    public GestioneUtente() {
        this.utenteDAOInterface = new UtenteDAO();
        this.beaconDAOInterface = new BeaconDAO();
    }

    @Override
    public String loginUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        UtenteEntity utentedb = utenteDAOInterface.getUserByUsername(utente.getUsername(), db);
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
                String idsessione = generateSessionId();
                utente.setIdsessione(idsessione);
                campo.put("idsessione",idsessione);
                utenteDAOInterface.updateInfoUtente(utente.getId(), campo, db);
                Gson gson = new Gson();
                boolean emergenza = GestioneMappe.isEmergenza();
                String utenteJson = gson.toJson(utente);
                esito = "{\"utente\":"+ utenteJson+", \"emergenza\":\""+emergenza+"\"}";
                System.out.println(esito);
            }
            else
                esito = "{\"esito\": \"ERROR: Password errata\"}";
        }
        else
            esito = "{\"esito\": \"ERROR: Utente non trovato\"}";
        connector.disconnect();
        return esito;
    }

    @Override
    public String registrazioneUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        boolean isUserInDb = utenteDAOInterface.findUserByUsername(utente.getUsername(), db);
        String esito;
        if(isUserInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        else{
            String idsessione = generateSessionId();
            utente.setIdsessione(idsessione);
            utenteDAOInterface.insertUser(utente, db);
            int id_utente = utenteDAOInterface.getUserByUsername(utente.getUsername(), db).getId();
            boolean emergenza = GestioneMappe.isEmergenza();
            esito = "{\"id_utente\": \""+ id_utente +"\", \"idsessione\": \""+ idsessione +"\", \"emergenza\":\""+emergenza+"\"}";
        }
        connector.disconnect();
        return esito;
    }

    @Override
    public String updateUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        UtenteDAOInterface utenteDAOInterface = new UtenteDAO();
        boolean isUsernameInDb = utenteDAOInterface.findUserByUsername(utente.getUsername(), db) && !utenteDAOInterface.isUsernameIdPresent(utente.getUsername(), utente.getId(), db);
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
            utenteDAOInterface.updateInfoUtente(utente.getId(), campo, db);
            esito = "{\"esito\": \"success\"}";
        }
        connector.disconnect();
        return esito;
    }

    @Override
    public String logoutUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        utenteDAOInterface.logout(utente.getUsername(), db);
        String esito = "{\"esito\": \"success\"}";
        connector.disconnect();
        return esito;
    }

    @Override
    public void updateUserPosition(UtenteEntity utente) {
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        HashMap<String, Object> campo = new HashMap<>();
        campo.put("beaconId", utente.getBeaconId());
        utenteDAOInterface.updateInfoUtente(utente.getId(),campo, db);

        connector.disconnect();
    }

    private String generateSessionId(){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for( int i = 0; i < 16; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();

    }
}
