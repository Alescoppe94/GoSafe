package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.control.GestioneMappe;
import org.teamids.gestionemappe.model.entity.Percorso;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/mappe")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneMappeBoundary {


    @GET
    @Path("calcolapercorso/{beaconPart}/{beaconArr}")
    public String calcoloPercorso(@PathParam("beaconPart") int beaconPart,
                                  @PathParam("beaconArr") int beaconArr) {
        // Calcolo del percorso con il codice del tizio
        GestioneMappe gestionemappe = new GestioneMappe();
        return gestionemappe.calcoloPercorso(beaconPart, beaconArr);

    }

    @GET
    @Path("generanotifiche")
    public String generaNotifiche(Percorso percorso) {
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
