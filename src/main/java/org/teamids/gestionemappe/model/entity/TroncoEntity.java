package org.teamids.gestionemappe.model.entity;

import org.teamids.gestionemappe.model.DAO.PesiTroncoDAO;
import org.teamids.gestionemappe.model.DAO.PesiTroncoDAOInterface;

import java.sql.Connection;
import java.util.*;

/**
 * Classe che modella l'entity tronco
 */
public class TroncoEntity implements TroncoEntityInterface {

    private int id;
    private boolean agibile;
    private ArrayList<BeaconEntity> beaconEstremi;
    private float area;

    /**
     * Costruttore
     * @param id id del tronco
     */
    public TroncoEntity(int id){
        this.id = id;
    }

    /**
     * Costruttore
     * @param agibile booleano che dice se il tronco è agibile o meno
     * @param beaconEstremi beacon che si trovano agli estremi del tronco
     * @param area area del tronco
     */
    public TroncoEntity(boolean agibile, ArrayList<BeaconEntity> beaconEstremi, float area){

        this.agibile = agibile;
        this.beaconEstremi = beaconEstremi;
        this.area = area;

    }

    /**
     * Costruttore
     * @param id id del tronco
     * @param agibile booleano che dice se il tronco è agibile o meno
     * @param beaconEstremi beacon che si trovano agli estremi del tronco
     * @param area area del tronco
     */
    public TroncoEntity(int id,boolean agibile, ArrayList<BeaconEntity> beaconEstremi, float area) {
        this.id = id;
        this.agibile = agibile;
        this.beaconEstremi = beaconEstremi;
        this.area = area;
    }

    /**
     * metodo setter per l'id del tronco
     * @param id id del tronco
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * metodo getter per l'id del tronco
     * @return id del tronco
     */
    public int getId() {
        return id;
    }

    /**
     * metodo getter per sapere se il tronco è agibile o no
     * @return booleano che indica se il tronco è agibile o no
     */
    public boolean isAgibile() {
        return agibile;
    }

    /**
     * metodo getter per ottenere i beacon estremi del tronco
     * @return i due beacon estremi del tronco
     */
    public ArrayList<BeaconEntity> getBeaconEstremi() {
        return beaconEstremi;
    }

    /**
     * metodo getter per l'area del tronco
     * @return l'area del tronco
     */
    public float getArea() {
        return area;
    }

    /**
     * calcola il costo di un tronco, utile nel calcolo del percorso più breve
     * @param db la connessione al database
     * @return il costo del tronco
     */
    @Override
    public float calcolaCosto(Connection db){
        PesiTroncoDAOInterface pesiTroncoDAOInterface = new PesiTroncoDAO();
        HashMap<Float, Float> coeffVal = pesiTroncoDAOInterface.getPesiTronco(this.id, db);
        Iterator<Map.Entry<Float, Float>> it = coeffVal.entrySet().iterator();
        float costo = 0;
        /* il costo del tronco è la somma dei prodotti tra i pesi(coeff) e i parametri(val) del tronco,
         * qui si iterano i vari parametri del tronco */
        while (it.hasNext()) {
            Map.Entry<Float, Float> coeff_val = it.next();
            costo += (coeff_val.getKey()*coeff_val.getValue());
        }
        return costo;

    }

    /**
     * Confronta gli id di due tronchi
     * @param o tronco passato
     * @return un intero, -1 se l'id del tronco è minore di quello del tronco passato, 1 altrimenti
     */
    @Override
    public int compareTo(TroncoEntity o) {
        if(id<o.getId()){
            return -1;
        }
        return 1;
    }
}
