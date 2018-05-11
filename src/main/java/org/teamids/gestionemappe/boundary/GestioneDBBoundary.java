package org.teamids.gestionemappe.boundary;

import org.teamids.gestionemappe.control.GestioneDB;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Base64;


@Path("/db")
@Produces(MediaType.APPLICATION_JSON)
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

        gestionedb.aggiungiPiano(jsonRequest);

        /*String base64Image = piano.getImmagine().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
