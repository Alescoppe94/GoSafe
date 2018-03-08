package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.model.DAO.BeaconDAO;
import org.teamids.gestionemappe.model.entity.Beacon;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/db")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    @PUT
    @Path("/beacons/{beaconId}")
    public void aggiornaDB(@PathParam("beaconId") int id, Beacon beacon){
        beacon.setId(id);
        BeaconDAO beaconDAO = new BeaconDAO();
        beaconDAO.updateBeacon(beacon);
    }

}
