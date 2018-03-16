package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.DAO.BeaconDAO;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/db")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    @PUT
    @Path("/beacons/{beaconId}")
    public void aggiornaDB(@PathParam("beaconId") int id, BeaconEntity beacon){
        beacon.setId(id);
        BeaconDAO beaconDAO = new BeaconDAO();
        beaconDAO.updateBeacon(beacon);
    }
    @GET
    @Path("tokentest")
    public void tokenUpdater(@QueryParam("token") String token){

        try {
            GestioneDB gestionedb = new GestioneDB();
            gestionedb.tokenUpdater(token);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
