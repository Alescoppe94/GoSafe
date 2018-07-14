package org.teamids.gestionemappe.control;

import com.google.gson.Gson;
import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.security.SecureRandom;
import java.sql.Connection;
import java.util.HashMap;

/**
 * Classe che si occupa di implementare i metodi utili alla gestione del profilo degli utenti
 */
public class GestioneUtente implements GestioneUtenteInterface {

    private UtenteDAOInterface utenteDAOInterface;
    private BeaconDAOInterface beaconDAOInterface;

    /**
     * Costruttore della classe GestioneUtente
     */
    public GestioneUtente() {
        this.utenteDAOInterface = new UtenteDAO();
        this.beaconDAOInterface = new BeaconDAO();
    }

    /**
     * Effettua il login di un utente, facendo opportuni controlli
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     * @return l'esito del login
     */
    @Override
    public String loginUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        UtenteEntity utentedb = utenteDAOInterface.getUserByUsername(utente.getUsername(), db);
        String esito;
        /* Se l'username dell'utente è registrato nel database del server si procede con la verifica */
        if(utentedb != null) {
            /* Se la password inserita dall'utente coincide con quella memorizzata nel database, si procede con il login */
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
            /* altrimenti si comunica all'utente che la password inserita è errata */
            else
                esito = "{\"esito\": \"ERROR: Password errata\"}";
        }
        /* Se l'username dell'utente non è presente nel database si comunica all'utente che non è stato trovato il suo username */
        else
            esito = "{\"esito\": \"ERROR: Utente non trovato\"}";
        connector.disconnect();
        return esito;
    }

    /**
     * Effettua la registrazione di un utente, facendo opportuni controlli
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     * @return l'esito della registrazione
     */
    @Override
    public String registrazioneUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        boolean isUserInDb = utenteDAOInterface.findUserByUsername(utente.getUsername(), db);
        String esito;
        /* Se l'username scelto dall'utente è già presente nel database viene comunicato all'utente */
        if(isUserInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        /* altrimenti si procede con la registrazione, facendo restituire al client alcuni dati utili */
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

    /**
     * Aggiorna le informazioni di profilo di un utente, facendo opportuni controlli
     * @param utente oggetto di tipo Utente contenente le nuove info dell'utente
     * @return l'esito della modifica
     */
    @Override
    public String updateUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        UtenteDAOInterface utenteDAOInterface = new UtenteDAO();
        boolean isUsernameInDb = utenteDAOInterface.findUserByUsername(utente.getUsername(), db) && !utenteDAOInterface.isUsernameIdPresent(utente.getUsername(), utente.getId(), db);
        String esito;
        /* Se l'username scelto dall'utente è già presente nel database viene comunicato all'utente */
        if(isUsernameInDb) {
            esito = "{\"esito\": \"Username in uso\"}";
        }
        /* altrimenti si procede con la modifica */
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

    /**
     * Effettua il logout dall'app
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     * @return l'esito del logout
     */
    @Override
    public String logoutUtente(UtenteEntity utente){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        utenteDAOInterface.logout(utente.getUsername(), db);
        String esito = "{\"esito\": \"success\"}";
        connector.disconnect();
        return esito;
    }

    /**
     * Aggiorna la posizione di un utente
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     */
    @Override
    public void updateUserPosition(UtenteEntity utente) {
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        HashMap<String, Object> campo = new HashMap<>();
        campo.put("beaconId", utente.getBeaconId());
        utenteDAOInterface.updateInfoUtente(utente.getId(),campo, db);
        connector.disconnect();
    }

    /**
     * Genera un token di sessione per il client per rendere più sicura la comunicazione con il server
     * @return il token di sessione
     */
    private String generateSessionId(){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for( int i = 0; i < 16; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();

    }
}
