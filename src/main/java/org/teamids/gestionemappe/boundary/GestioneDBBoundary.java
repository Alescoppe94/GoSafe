package org.teamids.gestionemappe.boundary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.server.mvc.Viewable;
import org.teamids.gestionemappe.control.GestioneDB;
import org.teamids.gestionemappe.control.GestioneDBInterface;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Classe che fornisce le API relative alla gestione del database
 */
@Path("/db")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GestioneDBBoundary {

    GestioneDBInterface gestionedb = new GestioneDB();

    /**
     * Fornisce le informazioni aggiornate del database al client
     * @param timestamp orario dell'utimo aggiornamento del database del client
     * @return json con tutte le informazioni aggiorante del database
     */
    @GET
    @Path("/secured/aggiornadb/{timestamp}")
    public String aggiornaDB(@PathParam("timestamp") Timestamp timestamp){
        return gestionedb.aggiornaDB(timestamp);
    }

    /**
     * Fornisce la parte di database del server utile al client
     * @return json del database del server
     */
    @GET
    @Path("/secured/download")
    public String downloadDb(){
        return gestionedb.downloadDb();

    }

    /**
     * Richiama la homepage della sezione dell'amministratore
     * @return la vista della homepage dell'amministratore, incluso l'elenco dei piani
     */
    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_HTML)
    public Viewable showAdmin() {

        Map<String, Integer> model = gestionedb.getAllPiani();
        return new Viewable("/index", model);
    }

    /**
     * Richiama la pagina in cui l'amministratore può visualizzare le statistiche
     * @return la vista della pagina delle statistiche dell'amministratore, incluse le statistiche
     */
    @GET
    @Path("/visualizzastatistiche")
    @Produces(MediaType.TEXT_HTML)
    public Viewable visualizzaStatistiche(){

        Map<BeaconEntity, Integer> model = gestionedb.getPeoplePerBeacon();
        return new Viewable("/statistiche", model);
    }


    /**
     * Richiama la pagina in cui l'amministatore può gestire un piano
     * @param pianoId l'id del piano che vuole gestire
     * @return la vista della pagina di gestione di un piano, incluse le info del piano
     */
    @GET
    @Path("/piano/{pianoId}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable showTronchiPiano(@PathParam("pianoId") int pianoId){

        HashMap<TroncoEntity, HashMap<String, Float>> model = gestionedb.getTronchiPiano(pianoId);
        TreeMap<TroncoEntity, HashMap<String, Float>> map = new TreeMap<TroncoEntity, HashMap<String, Float>>(new Comparator<TroncoEntity>() {
            @Override
            public int compare(TroncoEntity o1, TroncoEntity o2) {
                return o1.compareTo(o2);
            }
        });
        map.putAll(model);
        return new Viewable("/tronchipiano", map);

    }

    /**
     * Permette l'aggiunta di un nuovo piano nel database
     * @param request le informazioni del nuovo piano
     * @param piano il nome del piano
     * @return la lista dei beacon eventualmente già presenti nel database
     */
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
    }

    /**
     * Permette l'eliminazione di un piano dal database
     * @param idPiano l'id del piano da eliminare
     * @return la vista della homepage dell'amministratore, incluso l'elenco dei piani
     */
    @DELETE
    @Path("/eliminapiano/{idPiano}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable eliminaPiano(@PathParam("idPiano")int idPiano){

        gestionedb.eliminaPiano(idPiano);
        Map<String, Integer> model = gestionedb.getAllPiani();
        return new Viewable("/index", model);

    }

    /**
     * Permette l'aggiornamento delle informazioni di un piano, in particolare i pesi di un tronco
     * @param formParams gli elementi inseriti nella form di modifica
     * @return la vista della homepage dell'amministratore, incluso l'elenco dei piani
     */
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
        }

        Map<String, Integer> model = gestionedb.getAllPiani();
        return new Viewable("/index", model);

    }

    /**
     * Richiama la pagina in cui l'amministatore può gestire i pesi associati ai parametri dei tronchi
     * @return la vista della pagina di gestione dei pesi, incluso l'elenco dei pesi
     */
    @GET
    @Path("/mostraPesi")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostraPesi(){

        Map<Integer, Map<String, Float>> model = gestionedb.mostraPesi();
        return new Viewable("/pesi", model);

    }

    /**
     * Permette la modifica dei pesi associati ai parametri dei tronchi
     * @param formParams gli elementi inseriti nella form di modifica
     * @return la vista della pagina di gestione dei pesi, incluso l'elenco dei pesi
     */
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
        }
        Map<Integer, Map<String, Float>> model = gestionedb.mostraPesi();
        return new Viewable("/pesi", model);

    }

    /**
     * Permette l'aggiunta di un peso per i vari tronchi
     * @param formParams gli elementi inseriti nella form
     * @return la vista della pagina di gestione dei pesi, incluso l'elenco dei pesi
     */
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

    /**
     * Permette l'eliminazione di un peso già presente
     * @param idPeso l'id del peso che si vuole rimuovere
     */
    @DELETE
    @Path("/eliminapeso/{idPeso}")
    public void eliminaPeso(@PathParam("idPeso")int idPeso){

        gestionedb.eliminapeso(idPeso);

    }

}
