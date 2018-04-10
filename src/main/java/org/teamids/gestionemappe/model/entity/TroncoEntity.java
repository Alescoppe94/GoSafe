package org.teamids.gestionemappe.model.entity;

import org.teamids.gestionemappe.model.DAO.PesiTroncoDAO;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;
import org.teamids.gestionemappe.model.entity.BeaconEntity;

import java.util.*;

public class TroncoEntity {

    private int id;
    private boolean agibile;
    private float costo;
    private ArrayList<BeaconEntity> beaconEstremi;
    private float lunghezza;
    private float los;
    private float area;

    public TroncoEntity(int id,boolean agibile, float costo, ArrayList<BeaconEntity> beaconEstremi, float lunghezza, float los, float area) {
        this.id = id;
        this.agibile = agibile;
        this.costo = costo;
        this.beaconEstremi = beaconEstremi;
        this.lunghezza = lunghezza;
        this.los = los;
        this.area = area;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isAgibile() {
        return agibile;
    }

    public void setAgibile(boolean agibile) {
        this.agibile = agibile;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public ArrayList<BeaconEntity> getBeaconEstremi() {
        return beaconEstremi;
    }

    public void setBeaconEstremi(ArrayList<BeaconEntity> beaconEstremi) {
        this.beaconEstremi = beaconEstremi;
    }

    public float getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(float lunghezza) {
        this.lunghezza = lunghezza;
    }

    public float getLos() {
        return los;
    }

    public void setLos(float los) {
        this.los = los;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float calcolaCosto(){
        PesiTroncoDAO pesiTroncoDAO = new PesiTroncoDAO();
        HashMap<Float, Float> coeffVal = pesiTroncoDAO.getPesiTronco(this.id);
        Iterator<Map.Entry<Float, Float>> it = coeffVal.entrySet().iterator();
        float costo = 0;
        while (it.hasNext()) {
            Map.Entry<Float, Float> coeff_val = it.next();
            costo += (coeff_val.getKey()*coeff_val.getValue()); //TODO: valutare se aggiungere los e lunghezza
        }
        setCosto(costo);
        return costo;

    }
}
