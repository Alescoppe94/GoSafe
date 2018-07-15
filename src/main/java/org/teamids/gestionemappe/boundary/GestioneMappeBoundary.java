package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneMappe;
import org.teamids.gestionemappe.control.GestioneMappeInterface;
import org.teamids.gestionemappe.model.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Classe che fornisce le API relative alla gestione delle mappe
 */
@Path("/mappe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneMappeBoundary {

    GestioneMappeInterface gestionemappe = new GestioneMappe();

    /**
     * Permette il lancio di un'emergenza
     */
    @POST
    @Path("/lanciaemergenza")
    public void lanciaEmergenza(){
        gestionemappe.lanciaEmergenza();
    }

    /**
     * Permette di calcolare un percorso in condizioni ordinarie
     * @param beaconPart stringa contenente l'identificativo del beacon di partenza
     * @param beaconArr stringa contenente l'identificativo del beacon di arrivo
     * @return percorso che consente di andare dal beacon di partenza a quello di destinazione
     */
    @GET
    @Path("/secured/calcolapercorso/{beaconPart}/{beaconArr}")
    public PercorsoEntity calcoloPercorsoNoEmergenza(@PathParam("beaconPart")String beaconPart, @PathParam("beaconArr")String beaconArr) {
        return gestionemappe.calcoloPercorsoNoEmergenza(beaconPart,beaconArr);
    }

    /**
     * Permette di notificare il percorso più sicuro ad un utente in condizioni di emergenza
     * @param utenteId identificatore dell'utente
     * @param beaconPart stringa contenente l'identificativo del beacon di partenza dell'utente
     * @return la notifica per l'utente, incluso il percorso che dovrà seguire, l'orario ed un messaggio
     */
    @GET
    @Path("/secured/visualizzapercorso/{utenteId}/{beaconPart}")
    public NotificaEntity visualizzaPercorso(@PathParam("utenteId")int utenteId, @PathParam("beaconPart")String beaconPart){
        return gestionemappe.visualizzaPercorso(utenteId, beaconPart);
    }

    /**
     * Permette terminare la fase di emergenza e ripristinare la fase ordinaria
     */
    @PUT
    @Path("backtonormalmode") // chiamato dall'amministratore
    public void backToNormalMode(){
        gestionemappe.backToNormalMode();
    }
}
