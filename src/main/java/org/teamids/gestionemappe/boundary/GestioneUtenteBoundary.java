package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneUtente;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/utente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneUtenteBoundary {

    @POST
    @Path("login")
    public String loginUtente(UtenteEntity utente) {
        GestioneUtente gestioneutente = new GestioneUtente();
        return gestioneutente.loginUtente(utente);
    }


}
