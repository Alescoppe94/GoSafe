package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.control.GestioneMappe;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/mappe")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneMappeBoundary {

    GestioneMappe gestionemappe = new GestioneMappe();

    @GET
    @Path("calcolapercorso/{beaconPart}/{beaconArr}")
    public String calcoloPercorsoNoEmergenza(@PathParam("beaconPart") int beaconPart,
                                             @PathParam("beaconArr") int beaconArr) {
        // Calcolo del percorso con il codice del tizio
        return gestionemappe.calcoloPercorsoNoEmergenza(beaconPart, beaconArr);

    }

    @GET
    @Path("generanotifiche")
    public String generaNotifiche(PercorsoEntity percorso) {
        //Fa partire il calcolo del percorso nel controller+
        return "bro";

    }

    @GET
    @Path("lanciaemergenza")
    public void lanciaEmergenza(){

        try {
            GestioneDB connection = new GestioneDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
