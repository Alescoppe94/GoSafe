package org.teamids.gestionemappe.control;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.entity.*;

import javax.ws.rs.ApplicationPath;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe che si occupa di implementare i metodi necessari per lanciare un'emergenza, per calcolare i percorsi sia in fase di
 * emergenza che non, di ripristinare la fase ordinaria
 */
@ApplicationPath("gestionemappe")
public class GestioneMappe extends ResourceConfig implements GestioneMappeInterface {


    private UtenteDAO utenteDAO;
    private TroncoDAOInterface troncoDAOInterface;
    private PercorsoDAOInterface percorsoDAOInterface;
    private TappaDAOInterface tappaDAOInterface;
    private NotificaDAOInterface notificaDAOInterface;
    private BeaconDAOInterface beaconDAOInterface;
    private PesiTroncoDAOInterface pesiTroncoDAOInterface;
    private static boolean emergenza=false;


    /**
     * Costruttore della classe GestioneMappe
     */
    public GestioneMappe() {
        this.utenteDAO = new UtenteDAO();
        this.troncoDAOInterface = new TroncoDAO();
        this.percorsoDAOInterface = new PercorsoDAO();
        this.tappaDAOInterface = new TappaDAO();
        this.notificaDAOInterface = new NotificaDAO();
        this.beaconDAOInterface = new BeaconDAO();
        this.pesiTroncoDAOInterface = new PesiTroncoDAO();
        utenteDAO.addObserver(this);
        packages("org.teamids.gestionemappe");
        register(JspMvcFeature.class);
        // it may be also jersey.config.server.mvc.templateBasePath
        property("jersey.config.server.mvc.templateBasePath.jsp", "/WEB-INF/jsp");
    }

    /**
     * Permette lanciare un'emergenza e notificarla a tutti gli utenti connessi
     */
    @Override
    public void lanciaEmergenza(){
        final ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        generaPercorsiEvacuazione(db);
        ArrayList<String> tokensAttivi = utenteDAO.getTokensAttivi(db);
        Communication communication = new Communication();
        for(String token : tokensAttivi){
            communication.inviaNotifica(token);
        }
        Runnable r = new Runnable() {
            public void run() {
                try{
                    emergenza = true;
                    while(emergenza && utenteDAO.existUtenteInPericolo(db)){
                        generaPercorsiEvacuazione(db);
                    }
                }
                finally{
                    connector.disconnect();
                }
            }
        };
        new Thread(r).start();
    }


    /**
     * Permette di generare, in fase di emergenza, i percorsi più sicuri per ogni beacon a cui sono collegati gli utenti
     * @param db parametro utilizzato per la connessione al database
     */
    private void generaPercorsiEvacuazione(Connection db){
        ArrayList<String> beaconsDiPartenzaId = utenteDAO.getBeaconsIdAttivi(db);
        for(String beaconId: beaconsDiPartenzaId){
            calcoloPercorsoEvacuazione(beaconId, db);
        }
    }

    /**
     * Permette di calcolare un percorso in fase di emergenza
     * @param beaconPart stringa contenente l'identificativo del beacon di partenza
     * @param db parametro utilizzato per la connessione al database
     */
    private void calcoloPercorsoEvacuazione(String beaconPart, Connection db) {
        Set<BeaconEntity> pdr = beaconDAOInterface.getAllPuntiDiRaccolta(db);
        BeaconEntity partenza = beaconDAOInterface.getBeaconById(beaconPart, db);
        //Per calcolare correttamente un percorso, il beacon di partenza non può essere nullo
        if (partenza != null) {
            boolean emergenza = true;
            Map<LinkedList<BeaconEntity>, Float> percorsi_ottimi = new HashMap<>();
            Iterator<BeaconEntity> n = pdr.iterator();
            //Individiaumo tutti i possibili percorsi che partono dal beacon di partenza ed arriva ad uno dei possibili punti di raccolta
            while (n.hasNext()) {
                BeaconEntity arrivo = n.next();

                Map<LinkedList<BeaconEntity>, Float> percorsoOttimo_costoOttimo =  calcoloDijkstra(partenza, arrivo, emergenza, db);

                Map.Entry<LinkedList<BeaconEntity>, Float> entry = percorsoOttimo_costoOttimo.entrySet().iterator().next();

                percorsi_ottimi.put(entry.getKey(), entry.getValue());
            }
            LinkedList<BeaconEntity> percorso_def = new LinkedList<>();
            float costo_percorso_def = Float.MAX_VALUE;
            Iterator<Map.Entry<LinkedList<BeaconEntity>, Float>> iter = percorsi_ottimi.entrySet().iterator();
            //Per ogni possibile percorso individuato, il percorso ottimo sarà quello con costo minore
            while (iter.hasNext()) {
                //Log.d("scelta percorso", "entrato");
                Map.Entry<LinkedList<BeaconEntity>, Float> percorso_costo = iter.next();
                float costo_valore = percorso_costo.getValue();
                if (costo_valore < costo_percorso_def) {
                    percorso_def = percorso_costo.getKey();
                    costo_percorso_def = costo_valore;
                }
            }
            if (percorsi_ottimi.isEmpty()) {
                percorso_def.add(partenza);
            }
            LinkedList<TappaEntity> tappeOttime = new LinkedList<>();
            boolean existPercorso = percorsoDAOInterface.findPercorsoByBeaconId(beaconPart, db);
            int idPercorso;
            //Se un percorso che parte da quel beacon di partenza aggiorniamo solamente le tappe che compongono il percorso
            if(existPercorso) {
                idPercorso = percorsoDAOInterface.getPercorsoByBeaconId(beaconPart, db).getId();
                for(int i = 0; i < percorso_def.size()-1; i++) {
                    TroncoEntity troncoOttimo = troncoDAOInterface.getTroncoByBeacons(percorso_def.get(i), percorso_def.get(i+1), db);
                    boolean direzione = troncoDAOInterface.checkDirezioneTronco(troncoOttimo, db);
                    TappaEntity tappaOttima = new TappaEntity(troncoOttimo, idPercorso, direzione);
                    tappeOttime.add(tappaOttima);
                }
                tappaDAOInterface.aggiornaTappe(idPercorso, tappeOttime, db);
            }
            //Altrimenti se un percorso che parte da quel beacon non esiste, lo creiamo insieme alle tappe
            else {
                for(int i = 0; i < percorso_def.size()-1; i++) {
                    TroncoEntity troncoOttimo = troncoDAOInterface.getTroncoByBeacons(percorso_def.get(i), percorso_def.get(i+1), db);
                    boolean direzione = troncoDAOInterface.checkDirezioneTronco(troncoOttimo, db);
                    TappaEntity tappaOttima = new TappaEntity(troncoOttimo, direzione);
                    tappeOttime.add(tappaOttima);
                }
                tappaDAOInterface.creaPercorsoConTappe(partenza, tappeOttime, db);
            }
        }
    }

    /**
     * Permette di calcolare un percorso in condizioni ordinarie
     * @param beaconPart stringa contenente l'identificativo del beacon di partenza
     * @param beaconArr stringa contenente l'identificativo del beacon di arrivo
     * @return percorso che consente di andare dal beacon di partenza a quello di destinazione
     */
    @Override
    public PercorsoEntity calcoloPercorsoNoEmergenza(String beaconPart, String beaconArr){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        BeaconEntity partenza = beaconDAOInterface.getBeaconById(beaconPart, db);
        boolean emergenza = false;
        BeaconEntity arrivo = beaconDAOInterface.getBeaconById(beaconArr, db);
        PercorsoEntity percorso;

        //Per calcolare correttamente un percorso, il beacon di partenza e quello di arrivo non possono essere nulli
        if (partenza != null && arrivo != null) {

            Map<LinkedList<BeaconEntity>, Float> percorsoOttimo_costoOttimo =  calcoloDijkstra(partenza, arrivo, emergenza, db);

            LinkedList<TappaEntity> tappeOttime = new LinkedList<>();
            //int idPercorso =  percorsoDAO.insertPercorso(partenza);

            Map.Entry<LinkedList<BeaconEntity>, Float> entry = percorsoOttimo_costoOttimo.entrySet().iterator().next();

            //Creo il percorso come insieme di tappe, a partire dalla lista di beacon
            for(int i = 0; i < entry.getKey().size()-1; i++) {
                TroncoEntity troncoOttimo = troncoDAOInterface.getTroncoByBeacons(entry.getKey().get(i), entry.getKey().get(i+1), db);
                boolean direzione = troncoDAOInterface.checkDirezioneTronco(troncoOttimo, db);
                TappaEntity tappaOttima = new TappaEntity(troncoOttimo, direzione);
                tappeOttime.add(tappaOttima);
                //tappaDAO.insertTappa(tappaOttima);
            }
            percorso = new PercorsoEntity(tappeOttime, partenza);
        }
        //Se il beacon di partenza o di arrivo è nullo allora il percorso restituito sarà nullo.
        else{
            percorso = null;
        }
        connector.disconnect();
        return percorso;
    }

    /**
     * Permette di implementare l'algoritmo di Dijkstra utilizzato per il calcolo del percorso,
     * sia in fase di emergenza che non
     * @param partenza oggetto di tipo BeaconEntity contenente tutte le informazioni del beacon di partenza
     * @param arrivo oggetto di tipo BeaconEntity contenente tutte le informazioni del beacon di arrivo
     * @param emergenza variabile che indica se il percorso deve essere calcolato per la fase di emergenza o per quella ordinaria
     * @param db parametro utilizzato per la connessione al database
     * @return Hashmap che contiene una coppia chiave-valore in cui la chiave è una lista ordinata di BeaconEntity e il valore è il costo complessivo del percorso
     */
    private Map<LinkedList<BeaconEntity>, Float> calcoloDijkstra(BeaconEntity partenza, BeaconEntity arrivo, boolean emergenza, Connection db){

        Set<TroncoEntity> allTronchiEdificio = troncoDAOInterface.getAllTronchi(db);
        Map<LinkedList<BeaconEntity>, Float> costi_percorsi = new HashMap<>();
        BeaconEntity beacon_controllato = partenza;
        ArrayList<BeaconEntity> beacon_visitati = new ArrayList<>();
        LinkedList<BeaconEntity> percorso_ottimo_parziale = new LinkedList<>();
        percorso_ottimo_parziale.add(beacon_controllato);
        float costo_percorso_ottimo_parziale = 0;
        costi_percorsi.put(percorso_ottimo_parziale, costo_percorso_ottimo_parziale);
        while (!compare(beacon_controllato,arrivo)) {
            Set<TroncoEntity> tronchi_collegati = new HashSet<>();
            Iterator<TroncoEntity> i = allTronchiEdificio.iterator();
            while (i.hasNext()) {
                TroncoEntity tronco = i.next();
                ArrayList<BeaconEntity> beacons = tronco.getBeaconEstremi();
                if (compare(beacons, beacon_controllato)) {
                    boolean tronco_visitato = false;
                    Iterator<BeaconEntity> j = beacons.iterator();
                    while (j.hasNext()) {
                        BeaconEntity beacon = j.next();
                        if (compare(beacon_visitati,beacon))
                            tronco_visitato = true;
                    }
                    if (!tronco_visitato)
                        tronchi_collegati.add(tronco);
                }
            }
            Iterator<TroncoEntity> k = tronchi_collegati.iterator();
            while (k.hasNext()) {
                TroncoEntity tronco = k.next();
                LinkedList<BeaconEntity> percorso_parziale = new LinkedList<>();
                percorso_parziale.addAll(percorso_ottimo_parziale);
                float costo_percorso_parziale = costo_percorso_ottimo_parziale;
                ArrayList<BeaconEntity> beacons = tronco.getBeaconEstremi();
                Iterator<BeaconEntity> j = beacons.iterator();
                while (j.hasNext()) {
                    BeaconEntity beacon = j.next();
                    if (!compare(beacon, beacon_controllato)) {
                        percorso_parziale.add(beacon);
                    }
                }
                Float costo;
                //Se il percorso deve essere calcolato per la fase d'emergenza, il costo di un tronco è determinato
                //valutando diversi criteri quali la lunghezza, il los, il rischio di vita, l’indice di fumo ecc.
                if (emergenza){
                    costo = tronco.calcolaCosto(db);
                }
                //Se il percorso deve essere calcolato per la fase di non emergenza, il costo di un tronco è determinato
                //esclusivamente dalla lunghezza
                else{
                    costo = pesiTroncoDAOInterface.geValoreByPesoId(tronco.getId(), "l", db);
                }
                costo_percorso_parziale += costo;
                BeaconEntity beacon_finale = percorso_parziale.getLast();
                boolean inserito = false;
                Map<LinkedList<BeaconEntity>, Float> costi_percorsi_old = new HashMap<>();
                costi_percorsi_old.putAll(costi_percorsi);
                Iterator<Map.Entry<LinkedList<BeaconEntity>, Float>> it = costi_percorsi_old.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<LinkedList<BeaconEntity>, Float> percorso_costo = it.next();
                    LinkedList<BeaconEntity> percorso_esistente = percorso_costo.getKey();
                    if (compare(percorso_esistente.getLast(),beacon_finale)) {
                        inserito = true;
                        float costo_valore = percorso_costo.getValue();
                        if (costo_percorso_parziale < costo_valore) {
                            costi_percorsi.remove(percorso_esistente);
                            costi_percorsi.put(percorso_parziale, costo_percorso_parziale);
                        }
                    }
                }
                if (!inserito) {
                    costi_percorsi.put(percorso_parziale, costo_percorso_parziale);
                }
            }
            costi_percorsi.remove(percorso_ottimo_parziale);

            LinkedList<BeaconEntity> percorso_scelto = new LinkedList<>();
            float costo_percorso_scelto = Float.MAX_VALUE;
            Iterator<Map.Entry<LinkedList<BeaconEntity>, Float>> it = costi_percorsi.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<LinkedList<BeaconEntity>, Float> percorso_costo = it.next();
                float costo_valore = percorso_costo.getValue();
                if (costo_valore < costo_percorso_scelto) {
                    percorso_scelto = percorso_costo.getKey();
                    costo_percorso_scelto = costo_valore;
                }
            }
            beacon_visitati.add(beacon_controllato);
            if (percorso_scelto.isEmpty())
                percorso_scelto.add(arrivo);
            beacon_controllato = percorso_scelto.getLast();
            percorso_ottimo_parziale = percorso_scelto;
            costo_percorso_ottimo_parziale = costo_percorso_scelto;
        }

        Map<LinkedList<BeaconEntity>, Float> percorsoOttimo_costoOttimo = new HashMap<>();
        percorsoOttimo_costoOttimo.put(percorso_ottimo_parziale, costo_percorso_ottimo_parziale);
        return percorsoOttimo_costoOttimo;

    }

    /**
     * Permette di verificare se un certo beacon è presente in un array di beacon
     * @param beacons Array di oggetti di tipo BeaconEntity
     * @param beacon oggetto di tipo BeaconEntity che contiene tutte le informazione del beacon che si intende cercare
     * @return True se l'identificatore del beacon è uguale all'identificatore di uno dei beacon presenti nell'ArrayList, altrimente False
     */
    private boolean compare(ArrayList<BeaconEntity> beacons, BeaconEntity beacon){

        boolean contenuto = false;

        for(BeaconEntity b : beacons){
            if(b.getId().equals(beacon.getId())){
                contenuto = true;
            }
        }

        return contenuto;

    }

    /**
     * Permette di verificare se due beacon sono uguali o meno
     * @param beacon1 oggetto di tipo BeaconEntity contente tutte le informazioni del primo beacon
     * @param beacon2 oggetto di tipo BeaconEntity contente tutte le informazioni del secondo beacon
     * @return True se l'identificatore del primo beacon è lo stesso del secondo, altrimenti False
     */
    private boolean compare(BeaconEntity beacon1, BeaconEntity beacon2){

        boolean uguali = false;

        if(beacon1.getId().equals(beacon2.getId())){
            uguali = true;
        }

        return uguali;

    }

    /**
     * Permette di notificare il percorso più sicuro ad un utente in condizioni di emergenza
     * @param utenteId identificatore dell'utente a cui sarà rivolta la notifica
     * @param beaconPart stringa contenente l'identificativo del beacon di partenza
     * @return la notifica per l'utente, incluso il percorso che dovrà seguire, l'orario ed un messaggio
     */
    @Override
    public NotificaEntity visualizzaPercorso(int utenteId, String beaconPart) {
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        PercorsoEntity percorso = percorsoDAOInterface.getPercorsoByBeaconId(beaconPart, db);
        //Se non esiste sul database un percorso che parte da quel beacon di partenza viene subito calcolato
        if(percorso == null){
            synchronized (this) {
                calcoloPercorsoEvacuazione(beaconPart, db);
                percorso = percorsoDAOInterface.getPercorsoByBeaconId(beaconPart, db);
            }
        }
        LocalDateTime ora = LocalDateTime.now();
        NotificaEntity notifica = new NotificaEntity(utenteId, percorso, ora,"Sii prudente!");
        notificaDAOInterface.insertNotifica(notifica, db);
        HashMap<String, Object> campo = new HashMap<>();
        campo.put("percorsoId", percorso.getId());
        utenteDAO.updateInfoUtente(utenteId, campo, db);
        TroncoEntity tronco = percorso.getTappe().getFirst().getTronco();
        utenteDAO.updatePositionInEmergency(utenteId,beaconPart,tronco, db);
        connector.disconnect();
        return notifica;
    }

    /**
     * Permette di aggiornare automaticamente, sfruttando il design pattern Observer,
     * il valore del los di un tronco in fase di emergenza
     * @param o l'oggetto osservabile nel design pattern Observer cioè UtenteDAO
     * @param arg argomento passato dall'oggetto osservabile
     */
    @Override
    public void update(Observable o, Object arg) {

        ArrayList<Object> parametri = (ArrayList<Object>) arg;
        TroncoEntity tronco = (TroncoEntity) parametri.get(0);
        Connection db = (Connection) parametri.get(1);
        ArrayList<BeaconEntity> estremi = tronco.getBeaconEstremi();
        /* Prendiamo tutti i percorsi (al massimo 2), che hanno come beacon di partenza uno dei due estremi e come
           secondo beacon l'altro.
           Di questi percorsi prendiamo l'id. */
        ArrayList<PercorsoEntity> percorsi = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            PercorsoEntity percorso = percorsoDAOInterface.getPercorsoByBeaconId(estremi.get(i).getId(), db);
            if(percorso != null) percorsi.add(percorso);
        }
        ArrayList<Integer> percorsiId = new ArrayList<>();
        for(PercorsoEntity percorso: percorsi){
            if(percorso.getTappe().getFirst().getTronco().getId() == tronco.getId()){
                percorsiId.add(percorso.getId());
            }
        }
        // Infine, sulla tabella utente, contiamo quante sono le persone che hanno uno tra quei due percosoId.
        int numUserInTronco = utenteDAO.countUsersPerTronco(percorsiId, db);
        float los = numUserInTronco/tronco.getArea();
        pesiTroncoDAOInterface.updateValorePeso(tronco.getId(), "los", los, db);
    }

    /**
     * Permette di cessare la fase di emergenza e ripristinare la fase ordinaria
     */
    @Override
    public void backToNormalMode(){

        emergenza = false;
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();

        pesiTroncoDAOInterface.losToDefault(db);
        tappaDAOInterface.removeAllTappe(db);
        percorsoDAOInterface.removeAllPercorsi(db);

        connector.disconnect();
    }

    /**
     * Metodo getter dell'attributo emergenza
     * @return True se la variabile emergenza è True(quindi l'emergenza è in corso), altrimenti False
     */
    static boolean isEmergenza(){
        return emergenza;
    }
}
