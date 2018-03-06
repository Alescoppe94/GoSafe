package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.model.Corridoio;
import org.teamids.gestionemappe.model.Notifica;
import org.teamids.gestionemappe.model.Percorso;
import org.teamids.gestionemappe.model.Tappa;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/gestionemappe")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneMappe{

    public GestioneMappe() {
    }

    @GET
    @Path("calcolapercorso/{beaconPart}/{beaconArr}")
    public String calcoloPercorso(@PathParam("beaconPart") int beaconPart,
                                  @PathParam("beaconArr") int beaconArr) {
        // Calcolo del percorso con il codice del tizio
       return "Ciao"+beaconArr;

    }

    @GET
    @Path("generanotifiche")
    public String generaNotifiche(Percorso percorso) {
        //Fa partire il calcolo del percorso nel controller+
        return "bro";

    }

    @POST
    public void lanciaEmergenza(){

    }

}
