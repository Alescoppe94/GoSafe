package org.teamids.gestionemappe.boundary;


import org.teamids.gestionemappe.model.entity.Beacon;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/db")
//@Produces(MediaType.TEXT_PLAIN)
//@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    @PUT
    public void aggiornaDB(Beacon beacon){
        
    }

}
