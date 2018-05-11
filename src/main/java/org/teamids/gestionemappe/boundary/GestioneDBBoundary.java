package org.teamids.gestionemappe.boundary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.entity.PianoEntity;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;


@Path("/db")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    GestioneDB gestionedb = new GestioneDB();

    @GET
    @Path("/aggiornadb/{timestamp}")
    public String aggiornaDB(@PathParam("timestamp") Timestamp timestamp){
        return gestionedb.aggiornaDB(timestamp);
    }

    @POST
    @Path("/aggiungipiano")
    public void aggiungiPiano(String piano) {
        Gson pianojson = new Gson();
        JsonObject jsonRequest = pianojson.fromJson(piano, JsonObject.class);
        /*String base64Image = piano.getImmagine().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println(piano);
    }
}
