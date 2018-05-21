package org.teamids.gestionemappe.boundary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.server.mvc.Viewable;
import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.DbTable.Peso;
import org.teamids.gestionemappe.model.entity.PianoEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.annotation.Generated;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;


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
    @Path("/piano/{pianoId}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable showTronchiPiano(@PathParam("pianoId") int pianoId){

        HashMap<TroncoEntity, HashMap<String, Float>> model = gestionedb.getTronchiPiano(pianoId);
        /*Map<String, Float> model = new HashMap<>();
        for(TroncoEntity tronco : tronchiPiano){
            model.put(String.valueOf(tronco.getId()), tronco.getArea());
        }*/
        return new Viewable("/tronchipiano", model);

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

    @DELETE
    @Path("/eliminapiano/{idPiano}")
    public void eliminaPiano(@PathParam("idPiano")int idPiano){

        gestionedb.eliminaPiano(idPiano);

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/aggiornapesitronco")
    public void aggiornaPesiTronco(final MultivaluedMap<String, String> formParams){

        //Map<String,String> parameters = new HashMap<String,String>();

        Iterator<String> it = formParams.keySet().iterator();


        while(it.hasNext()){
            String theKey = (String)it.next();
            String nomePeso = theKey.split("-")[0];
            int idTronco = Integer.parseInt(theKey.split("-")[1]);
            float valorePeso = Float.parseFloat(formParams.getFirst(theKey));
            gestionedb.aggiornaPesiTronco(nomePeso, idTronco, valorePeso);
            //parameters.put(theKey,formParams.getFirst(theKey));
        }

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/modificaPesi")
    public void modificaPesi(final MultivaluedMap<String, String> formParams){

        //gestionedb.eliminapesi();   //TODO:eliminare pure pesitronco

        Iterator<String> it = formParams.keySet().iterator();

        while(it.hasNext()){
            String theKey = (String)it.next();
            String nomePeso = theKey.split("-")[0];
            int idTronco = Integer.parseInt(theKey.split("-")[1]);
            float valorePeso = Float.parseFloat(formParams.getFirst(theKey));
            gestionedb.aggiornaPesi(idTronco, nomePeso, valorePeso);
            //parameters.put(theKey,formParams.getFirst(theKey));
        }

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/aggiungiPeso")
    public void aggiungiPeso(final MultivaluedMap<String, String> formParams){

        Iterator<String> it = formParams.keySet().iterator();

        ArrayList<String> peso = new ArrayList<>();

        while(it.hasNext()){

            String theKey = (String)it.next();
            peso.add(formParams.getFirst(theKey));

        }

        gestionedb.inserisciPeso(peso);
    }

    @DELETE
    @Path("/eliminapeso/{idPeso}")
    public void eliminaPeso(@PathParam("idPeso")int idPeso){

        gestionedb.eliminapeso(idPeso);

    }

    @GET
    @Path("/mostraPesi")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostraPesi(){

        Map<Integer, Map<String, Float>> model = gestionedb.mostraPesi();
        return new Viewable("/pesi", model);

    }

    @GET
    @Path("/download")
    public String downloadDb(){

        return gestionedb.downloadDb();

    }

}
