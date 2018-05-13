package org.teamids.gestionemappe.boundary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.server.mvc.Viewable;
import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.entity.PianoEntity;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Path("/db")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    GestioneDB gestionedb = new GestioneDB();

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_HTML)
    public Viewable showAdmin(@Context HttpServletRequest request) {

        ArrayList<PianoEntity> piani = gestionedb.getPiani();
        Map<String, Integer> model = new HashMap<>();
        for(PianoEntity piano : piani){
            model.put(String.valueOf(piano.getId()), piano.getPiano());
        }
        Viewable test = new Viewable("/index", model);
        return test;
    }


    @GET
    @Path("/aggiornadb/{timestamp}")
    public String aggiornaDB(@PathParam("timestamp") Timestamp timestamp){
        return gestionedb.aggiornaDB(timestamp);
    }

    @POST
    @Path("/aggiungipiano")
    public void aggiungiPiano(@Context HttpServletRequest request, String piano) {
        Gson pianojson = new Gson();
        JsonObject jsonRequest = pianojson.fromJson(piano, JsonObject.class);

        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/docs/");

        gestionedb.aggiungiPiano(path, jsonRequest);

        /*String base64Image = piano.getImmagine().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
