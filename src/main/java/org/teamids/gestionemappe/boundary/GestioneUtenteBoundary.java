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
    @Path("/login")
    public String loginUtente(UtenteEntity utente){
        return gestioneutente.loginUtente(utente);
    }

    @POST
    @Path("/registrazione")
    public String registrazioneUtente(UtenteEntity utente){
        return gestioneutente.registrazioneUtente(utente);
    }

    @PUT
    @Path("/logout")
    public void logoutUtente(UtenteEntity utente){
        gestioneutente.logoutUtente(utente);
    }

    @POST
    @Path("modifica")
    public String updateUtente(UtenteEntity utente){
        return gestioneutente.updateUtente(utente);
    }

    @PUT
    @Path("/updateposition")
    public void updateUserPosition(UtenteEntity utente){
        gestioneutente.updateUserPosition(utente);
    }

    @GET
    @Path("/updateposition/{b}/{c}")
    public int updateUserPosition(@PathParam("b")String beaconPart, @PathParam("c")String beaconArr){
        int p;
        p=1;
        return p;
        //gestioneutente.updateUserPosition(utente);
    }
}
