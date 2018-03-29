package org.teamids.gestionemappe.boundary;

import com.google.gson.JsonObject;
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
        //beacon.setId(id);
        //BeaconDAO.updateBeacon(beacon);
    }

    @POST
    @Path("tokentest")
    public void tokenUpdater(javax.json.JsonObject token){

        try {
            GestioneDB gestionedb = new GestioneDB();
            gestionedb.tokenUpdater(token.getString("token"));
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
