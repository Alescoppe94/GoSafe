package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneUtente;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/utente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneUtenteBoundary {

    GestioneUtente gestioneutente = new GestioneUtente();

    @POST
    @Path("login")
    public String loginUtente(UtenteEntity utente){
        return gestioneutente.loginUtente(utente);
    }

    @POST
    @Path("registrazione")
    public String registrazioneUtente(UtenteEntity utente){
        return gestioneutente.registrazioneUtente(utente);
    }

    @PUT
    @Path("logout")
    public void logoutUtente(UtenteEntity utente){
        gestioneutente.logoutUtente(utente);
    }


}
