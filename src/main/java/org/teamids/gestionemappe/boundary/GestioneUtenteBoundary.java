package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneUtente;
import org.teamids.gestionemappe.control.GestioneUtenteInterface;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Classe che fornisce le API relative alla gestione del profilo degli utenti
 */
@Path("/utente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneUtenteBoundary {

    GestioneUtenteInterface gestioneutente = new GestioneUtente();

    /**
     * Permette il login all'app
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     * @return l'esito del login
     */
    @POST
    @Path("/login")
    public String loginUtente(UtenteEntity utente){
        return gestioneutente.loginUtente(utente);
    }

    /**
     * Permette la registrazione all'app
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     * @return l'esito della registrazione
     */
    @POST
    @Path("/registrazione")
    public String registrazioneUtente(UtenteEntity utente){
        return gestioneutente.registrazioneUtente(utente);
    }

    /**
     * Permette il logout dall'app
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     * @return l'esito del logout
     */
    @PUT
    @Path("/secured/logout")
    public String logoutUtente(UtenteEntity utente){
        return gestioneutente.logoutUtente(utente);
    }

    /**
     * Permette la modifica delle informazioni di profilo di un utente
     * @param utente oggetto di tipo Utente contenente le nuove info dell'utente
     * @param authString
     * @return l'esito della modifica
     */
    @POST
    @Path("/secured/modifica")
    public String updateUtente(UtenteEntity utente,@HeaderParam("authorization") String authString){ //TODO: serve authString?
        return gestioneutente.updateUtente(utente);
    }

    /**
     * Permette l'aggiornamento della posizione di un utente
     * @param utente oggetto di tipo Utente contenente le info dell'utente
     */
    @PUT
    @Path("/secured/updateposition")
    public void updateUserPosition(UtenteEntity utente){
        gestioneutente.updateUserPosition(utente);
    }
}
