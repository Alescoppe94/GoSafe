package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.control.GestioneMappe;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/mappe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneMappeBoundary {

    GestioneMappe gestionemappe = new GestioneMappe();

    @GET
    @Path("calcolapercorso/{beaconPart}/{beaconArr}")
    public PercorsoEntity calcoloPercorsoNoEmergenza(@PathParam("beaconPart") int beaconPart,
                                             @PathParam("beaconArr") int beaconArr) {
        // Calcolo del percorso con il codice del tizio
        return gestionemappe.calcoloPercorsoNoEmergenza(beaconPart, beaconArr);

    }

    @GET
    @Path("generanotifiche")
    public String generaNotifiche() {
        //Fa partire il calcolo del percorso nel controller+
        return "bro";

    }

    @GET
    @Path("lanciaemergenza/{beaconPart}")
    public PercorsoEntity lanciaEmergenza(@PathParam("beaconPart") int beaconPart){
        return gestionemappe.calcoloPercorsoEvacuazione(beaconPart);
    }

}
