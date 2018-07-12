package org.teamids.gestionemappe.model.entity;

import org.teamids.gestionemappe.model.DAO.PesiTroncoDAO;

import java.sql.Connection;
import java.util.*;

public class TroncoEntity implements TroncoEntityInterface {

    private int id;
    private boolean agibile;
    private ArrayList<BeaconEntity> beaconEstremi;
    private float area;

    public TroncoEntity(int id){
        this.id = id;
    }

    public TroncoEntity(boolean agibile, ArrayList<BeaconEntity> beaconEstremi, float area){

        this.agibile = agibile;
        this.beaconEstremi = beaconEstremi;
        this.area = area;

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

    @Override
    public float calcolaCosto(Connection db){
        PesiTroncoDAO pesiTroncoDAO = new PesiTroncoDAO();
        HashMap<Float, Float> coeffVal = pesiTroncoDAO.getPesiTronco(this.id, db);
        Iterator<Map.Entry<Float, Float>> it = coeffVal.entrySet().iterator();
        float costo = 0;
        while (it.hasNext()) {
            Map.Entry<Float, Float> coeff_val = it.next();
            costo += (coeff_val.getKey()*coeff_val.getValue());
        }
        return costo;

    }

    @Override
    public int compareTo(TroncoEntity o) {
        if(id<o.getId()){
            return -1;
        }
        return 1;
    }
}
