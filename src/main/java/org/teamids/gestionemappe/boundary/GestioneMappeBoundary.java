package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.control.GestioneMappe;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;
import org.teamids.gestionemappe.model.entity.NotificaEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/mappe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneMappeBoundary {

    GestioneMappe gestionemappe = new GestioneMappe();

    @POST
    @Path("lanciaemergenza")
    public void lanciaEmergenza(){
        gestionemappe.lanciaEmergenza();
    }

    @GET
    @Path("calcolapercorso/{beaconPart}/{beaconArr}")
    public PercorsoEntity calcoloPercorsoNoEmergenza(@PathParam("beaconPart") int beaconPart,
                                             @PathParam("beaconArr") int beaconArr) {
        // Calcolo del percorso con il codice del tizio
        return gestionemappe.calcoloPercorsoNoEmergenza(beaconPart, beaconArr);
    }

    @GET
    @Path("visualizzanotifica/{utenteId}/{beaconPart}")
    public NotificaEntity visualizzaNotifica( @PathParam("utenteId") int utenteId,
                                              @PathParam("beaconPart") int beaconPart){
        return gestionemappe.visualizzaNotifica(utenteId,beaconPart);
    }

    @GET
    @Path("aggiornapercorso/{utenteId}/{beaconPart}")
    public PercorsoEntity aggiornaPercorso(@PathParam("utenteId") int utenteId,
                                           @PathParam("beaconPart") int beaconPart){
        return gestionemappe.aggiornaPercorso(utenteId,beaconPart);
    }
}
