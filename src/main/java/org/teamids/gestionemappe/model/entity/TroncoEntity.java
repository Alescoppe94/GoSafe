package org.teamids.gestionemappe.model.entity;

import org.teamids.gestionemappe.model.DAO.PesiTroncoDAO;

import java.util.*;

public class TroncoEntity {

    private int id;
    private boolean agibile;
    private ArrayList<BeaconEntity> beaconEstremi;
    private float area;

    public TroncoEntity(int id){
        this.id = id;
    }

    public TroncoEntity(int id,boolean agibile, ArrayList<BeaconEntity> beaconEstremi, float area) {
        this.id = id;
        this.agibile = agibile;
        this.beaconEstremi = beaconEstremi;
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

    public ArrayList<BeaconEntity> getBeaconEstremi() {
        return beaconEstremi;
    }

    public void setBeaconEstremi(ArrayList<BeaconEntity> beaconEstremi) {
        this.beaconEstremi = beaconEstremi;
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
            costo += (coeff_val.getKey()*coeff_val.getValue());
        }
        return costo;

    }
}
