package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.DAO.*;
import org.teamids.gestionemappe.model.DbTable.Tronco;
import org.teamids.gestionemappe.model.entity.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.time.LocalDateTime;
import java.util.*;

@ApplicationPath("gestionemappe")
public class GestioneMappe extends Application implements Observer {

    private Set<TroncoEntity> allTronchiEdificio;
    private Set<BeaconEntity> pdr;
    private final UtenteDAO utenteDAO = new UtenteDAO();

    public GestioneMappe() {

        allTronchiEdificio = TroncoDAO.getAllTronchi();
        pdr = BeaconDAO.getAllPuntiDiRaccolta();
        utenteDAO.addObserver(this);
    }

    public void lanciaEmergenza(){
        generaPercorsiEvacuazione();
        ArrayList<String> tokensAttivi = UtenteDAO.getTokensAttivi();
        Communication communication = new Communication();
        for(String token : tokensAttivi){
            communication.inviaNotifica(token);
        }
        Runnable r = new Runnable() {
            public void run() {
                while(UtenteDAO.existUtenteInPericolo()){
                    generaPercorsiEvacuazione();
                }
            }
        };
        new Thread(r).start();
    }

    public void generaPercorsiEvacuazione(){
        ArrayList<Integer> beaconsDiPartenzaId = UtenteDAO.getBeaconsIdAttivi();
        for(int beaconId: beaconsDiPartenzaId){
            calcoloPercorsoEvacuazione(beaconId);
        }
    }

    public void calcoloPercorsoEvacuazione(int beaconPart) {
        BeaconEntity partenza = BeaconDAO.getBeaconById(beaconPart);
        if (partenza != null) {
            boolean emergenza = true;
            PercorsoEntity percorso;
            Map<LinkedList<BeaconEntity>, Float> percorsi_ottimi = new HashMap<>();
            Iterator<BeaconEntity> n = pdr.iterator();
            while (n.hasNext()) {
                BeaconEntity arrivo = n.next();

                Map<LinkedList<BeaconEntity>, Float> percorsoOttimo_costoOttimo =  calcoloDijkstra(partenza, arrivo, emergenza);

                Map.Entry<LinkedList<BeaconEntity>, Float> entry = percorsoOttimo_costoOttimo.entrySet().iterator().next();

                percorsi_ottimi.put(entry.getKey(), entry.getValue());
            }
            LinkedList<BeaconEntity> percorso_def = new LinkedList<>();
            float costo_percorso_def = Float.MAX_VALUE;
            Iterator<Map.Entry<LinkedList<BeaconEntity>, Float>> iter = percorsi_ottimi.entrySet().iterator();
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
            int idPercorso =  PercorsoDAO.insertPercorso(partenza); //TODO settare il percorso su utente

            for(int i = 0; i < percorso_def.size()-1; i++) {
                TroncoEntity troncoOttimo = TroncoDAO.getTroncoByBeacons(percorso_def.get(i), percorso_def.get(i+1));
                TappaEntity tappaOttima = new TappaEntity(troncoOttimo, idPercorso);
                tappeOttime.add(tappaOttima);
                TappaDAO.insertTappa(tappaOttima);
            }

            percorso = new PercorsoEntity(idPercorso, tappeOttime, partenza); //TODO settare il percorso su utente
        }
    }

    //2 parametro
    public PercorsoEntity calcoloPercorsoNoEmergenza(int beaconPart, int beaconArr){
        BeaconEntity partenza = BeaconDAO.getBeaconById(beaconPart);
        boolean emergenza = false;
        BeaconEntity arrivo = BeaconDAO.getBeaconById(beaconArr);
        PercorsoEntity percorso;

        if (partenza != null && arrivo != null) {

            Map<LinkedList<BeaconEntity>, Float> percorsoOttimo_costoOttimo =  calcoloDijkstra(partenza, arrivo, emergenza);

            LinkedList<TappaEntity> tappeOttime = new LinkedList<>();
            int idPercorso =  PercorsoDAO.insertPercorso(partenza); //TODO settare il percorso su utente

            Map.Entry<LinkedList<BeaconEntity>, Float> entry = percorsoOttimo_costoOttimo.entrySet().iterator().next();

            for(int i = 0; i < entry.getKey().size()-1; i++) {
                TroncoEntity troncoOttimo = TroncoDAO.getTroncoByBeacons(entry.getKey().get(i), entry.getKey().get(i+1));
                TappaEntity tappaOttima = new TappaEntity(troncoOttimo, idPercorso);
                tappeOttime.add(tappaOttima);
                TappaDAO.insertTappa(tappaOttima);
            }
            percorso = new PercorsoEntity(idPercorso, tappeOttime, partenza); //TODO settare il percorso su utente
        }else{
            percorso = null;
        }
        return percorso;
    }


    private Map<LinkedList<BeaconEntity>, Float> calcoloDijkstra(BeaconEntity partenza, BeaconEntity arrivo, boolean emergenza){

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
                if (emergenza){
                    costo = tronco.getCosto();/*getCosto_dinamico()*/;
                }else{
                    costo = tronco.getLunghezza();
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
            if (percorso_scelto.isEmpty())//TODO controllare, messo per aggirare un bug
                percorso_scelto.add(arrivo);
            beacon_controllato = percorso_scelto.getLast();
            percorso_ottimo_parziale = percorso_scelto;
            costo_percorso_ottimo_parziale = costo_percorso_scelto;
        }

        Map<LinkedList<BeaconEntity>, Float> percorsoOttimo_costoOttimo = new HashMap<>();
        percorsoOttimo_costoOttimo.put(percorso_ottimo_parziale, costo_percorso_ottimo_parziale);
        return percorsoOttimo_costoOttimo;

    }

    private boolean compare(ArrayList<BeaconEntity> beacons, BeaconEntity beacon){

        boolean contenuto = false;

        for(BeaconEntity b : beacons){
            if(b.getId() == beacon.getId()){
                contenuto = true;
            }
        }

        return contenuto;

    }

    private boolean compare(BeaconEntity beacon1, BeaconEntity beacon2){

        boolean uguali = false;

        if(beacon1.getId() == beacon2.getId()){
            uguali = true;
        }

        return uguali;

    }

    public NotificaEntity visualizzaNotifica(int utenteId,int beaconPart) {
        PercorsoEntity percorso = PercorsoDAO.getPercorsoByBeaconId(beaconPart);
        LocalDateTime ora = LocalDateTime.now();
        NotificaEntity notifica = new NotificaEntity(utenteId, percorso, ora,"Sii prudente!");
        NotificaDAO.insertNotifica(notifica);
        HashMap<String, Object> campo = new HashMap<>();
        campo.put("percorsoId", percorso.getId());
        UtenteDAO.updateInfoUtente(utenteId, campo);
        TroncoEntity tronco = percorso.getTappe().getFirst().getTronco();
        utenteDAO.updatePositionInEmergency(utenteId,beaconPart,tronco);
        return notifica;
    }

    public PercorsoEntity aggiornaPercorso(int utenteId, int beaconPart) {
        PercorsoEntity percorso = PercorsoDAO.getPercorsoByBeaconId(beaconPart);
        HashMap<String, Object> campo = new HashMap<>();
        campo.put("percorsoId", percorso.getId());
        UtenteDAO.updateInfoUtente(utenteId, campo);
        TroncoEntity tronco = percorso.getTappe().getFirst().getTronco();
        utenteDAO.updatePositionInEmergency(utenteId,beaconPart,tronco);
        return percorso;
    }

    @Override
    public void update(Observable o, Object arg) {
        TroncoEntity tronco = (TroncoEntity) arg;
        ArrayList<BeaconEntity> estremi = tronco.getBeaconEstremi();
        /* Prendiamo tutti i percorsi (al massimo 2), che hanno come beacon di partenza uno dei due estremi e come
           secondo beacon l'altro.
           Di questi percorsi prendiamo l'id.
           Infine, sulla tabella utente, contiamo quante sono le persone che hanno uno tra quei due percosoId.
         */
    }
}
