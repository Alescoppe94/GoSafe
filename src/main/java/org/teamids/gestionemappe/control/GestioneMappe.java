package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.DAO.BeaconDAO;
import org.teamids.gestionemappe.model.DAO.PercorsoDAO;
import org.teamids.gestionemappe.model.DAO.TappaDAO;
import org.teamids.gestionemappe.model.DAO.TroncoDAO;
import org.teamids.gestionemappe.model.DbTable.Tronco;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.*;

@ApplicationPath("gestionemappe")
public class GestioneMappe extends Application {

    public GestioneMappe() {

    }

    //2 parametro
    public PercorsoEntity calcoloPercorsoNoEmergenza(int beaconPart, int beaconArr){
        BeaconDAO beaconDAO = new BeaconDAO();
        BeaconEntity partenza = beaconDAO.getBeaconById(beaconPart);
        BeaconEntity arrivo = beaconDAO.getBeaconById(beaconArr);
        TroncoDAO troncoDAO = new TroncoDAO();
        PercorsoEntity percorso;

        if (partenza != null && arrivo != null) {
            Map<LinkedList<BeaconEntity>, Float> costi_percorsi = new HashMap<>();
            //Log.d("size0 costi_percorsi", String.valueOf(costi_percorsi.size()));
            //percorso_ottimo.add(partenza);
            BeaconEntity beacon_controllato = partenza;
            Set<BeaconEntity> beacon_visitati = new HashSet<>();
            LinkedList<BeaconEntity> percorso_ottimo_parziale = new LinkedList<>();
            percorso_ottimo_parziale.add(beacon_controllato);
            float costo_percorso_ottimo_parziale = 0;
            costi_percorsi.put(percorso_ottimo_parziale, costo_percorso_ottimo_parziale);
            //Log.d("size1 costi_percorsi", String.valueOf(costi_percorsi.size()));
            //Log.d("inizio ciclo principale", "si");
            while (!beacon_controllato.equals(arrivo)) {
                //Log.d("nodo controllato", beacon_controllato.getNome()+", id:  "+beacon_controllato.getId_nodo());
                Set<TroncoEntity> tronchi_collegati = new HashSet<>();
                Set<TroncoEntity> allTronchiEdificio = troncoDAO.getAllTronchi(); //TODO: ragionare se metterlo nel costruttore
                Iterator<TroncoEntity> i = allTronchiEdificio.iterator();
                while (i.hasNext()) {
                    TroncoEntity tronco = i.next();
                    //Log.d("tronco", String.valueOf(tronco.getId_arco()));
                    Set<BeaconEntity> beacons = tronco.getBeaconEstremi();
                    if (beacons.contains(beacon_controllato)) {
                        boolean tronco_visitato = false;
                        Iterator<BeaconEntity> j = beacons.iterator();
                        while (j.hasNext()) {
                            BeaconEntity beacon = j.next();
                            if (beacon_visitati.contains(beacon))
                                tronco_visitato = true;
                        }
                        if (!tronco_visitato)
                            tronchi_collegati.add(tronco);
                    }
                }
                //Log.d("num. archi collegati", String.valueOf(tronchi_collegati.size()));
                Iterator<TroncoEntity> k = tronchi_collegati.iterator();
                while (k.hasNext()) {
                    TroncoEntity tronco = k.next();
                    //Log.d("id_arco_collegato", String.valueOf(tronco.getId_arco()));
                    LinkedList<BeaconEntity> percorso_parziale = new LinkedList<>();
                    //Log.d("size percorso_parziale", String.valueOf(percorso_parziale.size()));
                    percorso_parziale.addAll(percorso_ottimo_parziale);
                    //Log.d("size percorso_parziale", String.valueOf(percorso_parziale.size()));
                    float costo_percorso_parziale = costo_percorso_ottimo_parziale;
                    Set<BeaconEntity> beacons = tronco.getBeaconEstremi();
                    Iterator<BeaconEntity> j = beacons.iterator();
                    while (j.hasNext()) {
                        BeaconEntity beacon = j.next();
                        if (!beacon.equals(beacon_controllato)) {
                            percorso_parziale.add(beacon);
                        }
                        //Log.d("size percorso_parziale", String.valueOf(percorso_parziale.size()));
                    }
                    Float costo = tronco.getLunghezza();
                    //Log.d("costo", String.valueOf(costo));
                    costo_percorso_parziale += costo;
                    //Log.d("costo_percorso_parziale", String.valueOf(costo_percorso_parziale));
                    BeaconEntity beacon_finale = percorso_parziale.getLast();
                    //Log.d("beacon_finale", String.valueOf(beacon_finale.getId_nodo()));
                    boolean inserito = false;
                    //Log.d("clono costi_percorsi", "inizio");
                    //Log.d("size costi_percorsi", String.valueOf(costi_percorsi.size()));
                    Map<LinkedList<BeaconEntity>, Float> costi_percorsi_old = new HashMap<>();
                    //Log.d("size costi_percorsi_old", String.valueOf(costi_percorsi_old.size()));
                    costi_percorsi_old.putAll(costi_percorsi);
                    //Log.d("size costi_percorsi_old", String.valueOf(costi_percorsi_old.size()));
                    //Log.d("scorro costi_perc._old", "inizio");
                    Iterator<Map.Entry<LinkedList<BeaconEntity>, Float>> it = costi_percorsi_old.entrySet().iterator();
                    //int bo = 1;
                    while (it.hasNext()) {
                        //Log.d("scorro", String.valueOf(bo));
                        //bo++;
                        Map.Entry<LinkedList<BeaconEntity>, Float> percorso_costo = it.next();
                        LinkedList<BeaconEntity> percorso_esistente = percorso_costo.getKey();
                        //Log.d("fine percorso_esistente", String.valueOf(percorso_esistente.getLast().getId_nodo()));
                        if (percorso_esistente.getLast().equals(beacon_finale)) {
                            //Log.d("fine=beacon_finale", "si");
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
                    //Log.d("inserito", String.valueOf(inserito));
                    //Log.d("size costi_percorsi", String.valueOf(costi_percorsi.size()));
                }
                //Log.d("costi_percorsi pre del", String.valueOf(costi_percorsi.size()));
                costi_percorsi.remove(percorso_ottimo_parziale);
                //Log.d("costi_percorsi post del", String.valueOf(costi_percorsi.size()));

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

            LinkedList<TappaEntity> tappeOttime = new LinkedList<>();
            TappaDAO tappaDAO = new TappaDAO();
            PercorsoDAO percorsoDAO = new PercorsoDAO(); //TODO settare il percorso su utente
            int idPercorso =  percorsoDAO.insertPercorso(partenza);

            for(int i = 0; i < percorso_ottimo_parziale.size()-1; i++) {
                TroncoEntity troncoOttimo = troncoDAO.getTroncoByBeacons(percorso_ottimo_parziale.get(i), percorso_ottimo_parziale.get(i+1));
                TappaEntity tappaOttima = new TappaEntity(troncoOttimo, idPercorso);
                tappeOttime.add(tappaOttima);
                tappaDAO.insertTappa(tappaOttima);
            }
            percorso = new PercorsoEntity(idPercorso, tappeOttime, partenza); //TODO settare il percorso su utente
        }else{
            percorso = null;
        }
        return percorso;
    }

    //2 parametri


    private void calcoloDijkstra(){

    }

    public void generaNotifica(){

    }

    public void lanciaEmergenza(){

    }


}
