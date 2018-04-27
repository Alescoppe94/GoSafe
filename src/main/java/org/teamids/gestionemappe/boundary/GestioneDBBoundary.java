package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;


@Path("/db")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    GestioneDB gestionedb = new GestioneDB();

    @GET
    @Path("/aggiornadb/{timestamp}")
    public String aggiornaDB(@PathParam("timestamp") Timestamp timestamp){
        return gestionedb.aggiornaDB(timestamp);
    }
}
