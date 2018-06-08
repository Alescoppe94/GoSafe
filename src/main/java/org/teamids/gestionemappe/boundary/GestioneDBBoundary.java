package org.teamids.gestionemappe.boundary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.server.mvc.Viewable;
import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.model.DbTable.Peso;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
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
    @Path("/secured/aggiornadb/{timestamp}")
    public String aggiornaDB(@PathParam("timestamp") Timestamp timestamp){
        return gestionedb.aggiornaDB(timestamp);
    }

    @GET
    @Path("/secured/download")
    public String downloadDb(){
        return gestionedb.downloadDb();

    }

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_HTML)
    public Viewable showAdmin() {

        Map<String, Integer> model = gestionedb.getAllPiani();
        return new Viewable("/index", model);
    }

    @GET
    @Path("/visualizzastatistiche")
    @Produces(MediaType.TEXT_HTML)
    public Viewable visualizzaStatistiche(){

        Map<BeaconEntity, Integer> model = gestionedb.getPeoplePerBeacon();
        return new Viewable("/statistiche", model);
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
        TreeMap<TroncoEntity, HashMap<String, Float>> map = new TreeMap<TroncoEntity, HashMap<String, Float>>(new Comparator<TroncoEntity>() {
            @Override
            public int compare(TroncoEntity o1, TroncoEntity o2) {
                return o1.compareTo(o2);
            }
        });
        map.putAll(model);
        return new Viewable("/tronchipiano", map);

    }

    @POST
    @Path("/aggiungipiano")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<String> aggiungiPiano(@Context HttpServletRequest request, String piano) {
        Gson pianojson = new Gson();
        JsonObject jsonRequest = pianojson.fromJson(piano, JsonObject.class);

        File f = new File(request.getSession().getServletContext().getRealPath("/WEB-INF/docs"));
        if(!f.exists()){
            f.mkdirs();
        }

        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/docs/");

        ArrayList<String> beaconDoppi = gestionedb.aggiungiPiano(path, jsonRequest);

        return beaconDoppi;


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
    @Produces(MediaType.TEXT_HTML)
    public Viewable eliminaPiano(@PathParam("idPiano")int idPiano){

        gestionedb.eliminaPiano(idPiano);
        Map<String, Integer> model = gestionedb.getAllPiani();
        return new Viewable("/index", model);

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/aggiornapesitronco")
    @Produces(MediaType.TEXT_HTML)
    public Viewable aggiornaPesiTronco(final MultivaluedMap<String, String> formParams){

        Iterator<String> it = formParams.keySet().iterator();

        while(it.hasNext()){
            String theKey = (String)it.next();
            String nomePeso = theKey.split("-")[0];
            int idTronco = Integer.parseInt(theKey.split("-")[1]);
            float valorePeso = Float.parseFloat(formParams.getFirst(theKey));
            gestionedb.aggiornaPesiTronco(nomePeso, idTronco, valorePeso);
            //parameters.put(theKey,formParams.getFirst(theKey));
        }

        Map<String, Integer> model = gestionedb.getAllPiani();
        return new Viewable("/index", model);

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/modificaPesi")
    @Produces(MediaType.TEXT_HTML)
    public Viewable modificaPesi(final MultivaluedMap<String, String> formParams){

        Iterator<String> it = formParams.keySet().iterator();

        while(it.hasNext()){
            String theKey = (String)it.next();
            String nomePeso = theKey.split("-")[0];
            int idTronco = Integer.parseInt(theKey.split("-")[1]);
            float valorePeso = Float.parseFloat(formParams.getFirst(theKey));
            gestionedb.aggiornaPesi(idTronco, nomePeso, valorePeso);
            //parameters.put(theKey,formParams.getFirst(theKey));
        }
        Map<Integer, Map<String, Float>> model = gestionedb.mostraPesi();
        return new Viewable("/pesi", model);

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/aggiungiPeso")
    @Produces(MediaType.TEXT_HTML)
    public Viewable aggiungiPeso(final MultivaluedMap<String, String> formParams){

        Iterator<String> it = formParams.keySet().iterator();

        ArrayList<String> peso = new ArrayList<>();

        while(it.hasNext()){

            String theKey = (String)it.next();
            peso.add(formParams.getFirst(theKey));

        }

        gestionedb.inserisciPeso(peso);
        Map<Integer, Map<String, Float>> model = gestionedb.mostraPesi();
        return new Viewable("/pesi", model);
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
}
