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
    @Path("/lanciaemergenza")
    public void lanciaEmergenza(){
        gestionemappe.lanciaEmergenza();
    }

    @GET
    @Path("/secured/calcolapercorso/{beaconPart}/{beaconArr}")
    public PercorsoEntity calcoloPercorsoNoEmergenza(@PathParam("beaconPart")String beaconPart, @PathParam("beaconArr")String beaconArr) {
        return gestionemappe.calcoloPercorsoNoEmergenza(beaconPart,beaconArr);
    }

    @GET
    @Path("/secured/visualizzapercorso/{utenteId}/{beaconPart}")
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
        new Communication().inviaNotifica("fWIyAdRy6sc:APA91bFdkElV5eaWVmUgh98XWrR9WC404TUEOeWKPf0Ynj3XZXg9ZD3slMItHrZdK_DBDk_gSVNx2ldBANkgqycwfweZO3T0Q0y6LJf50BFyLiNSdxApWsjJhUukWQmOF55DNFPHw3Wz");
    }
}
