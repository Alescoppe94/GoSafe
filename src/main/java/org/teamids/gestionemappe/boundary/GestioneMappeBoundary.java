package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.Communication;
import org.teamids.gestionemappe.control.GestioneMappe;
import org.teamids.gestionemappe.model.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public PercorsoEntity calcoloPercorsoNoEmergenza(@PathParam("beaconPart")String beaconPart, @PathParam("beaconArr")String beaconArr) {
        return gestionemappe.calcoloPercorsoNoEmergenza(beaconPart,beaconArr);
    }

    @GET
    @Path("visualizzapercorso/{utenteId}/{beaconPart}")
    public NotificaEntity visualizzaPercorso(@PathParam("utenteId")int utenteId, @PathParam("beaconPart")String beaconPart){
        return gestionemappe.visualizzaPercorso(utenteId, beaconPart);
    }

    @PUT
    @Path("backtonormalmode") // chiamato dall'amministratore
    public void backToNormalMode(){
        gestionemappe.backToNormalMode();
    }

    @GET
    @Path("not")
    public void not(){
        new Communication().inviaNotifica("dJUwYnXY6S8:APA91bEEPJcM-31_C6urBvkgNSnBVusYg7efEiwfqKywr8Brn_mE3-3cS4glFwsIHj28HpOjOV1TYePc2aolsiPOPAZPGoeZmST8qsaz5LBGP6A1ES7i8oK2W9V2Kel91ALXbAU4i64V");
    }
}
