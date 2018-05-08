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
        new Communication().inviaNotifica("f6u18KCsU98:APA91bH3oFveiN03-27o2lHLjlsn6FwvxwHO26c0NUug6KqTlvFXZwgF70nnuKW9MWEjEafGCiVm7lbSPBbLI_VR16_Uyrf0LyTWWmrhD8ZutEHLmEMEtcYTm65ipcYkGf1dBQt1hSup");
    }
}
