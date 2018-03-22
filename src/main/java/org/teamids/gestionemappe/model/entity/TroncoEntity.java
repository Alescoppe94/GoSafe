package org.teamids.gestionemappe.model.entity;

import org.teamids.gestionemappe.model.entity.BeaconEntity;

import java.util.HashSet;
import java.util.Set;

public class TroncoEntity {

    private int id;
    private boolean agibile;
    private float costo;
    private Set<BeaconEntity> beaconEstremi;
    private float lunghezza;

    public TroncoEntity(int id,boolean agibile, float costo, Set<BeaconEntity> beaconEstremi, float lunghezza) {
        this.id = id;
        this.agibile = agibile;
        this.costo = costo;
        this.beaconEstremi = beaconEstremi;
        this.lunghezza = lunghezza;
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

    public Set<BeaconEntity> getBeaconEstremi() {
        return beaconEstremi;
    }

    public void setBeaconEstremi(Set<BeaconEntity> beaconEstremi) {
        this.beaconEstremi = beaconEstremi;
    }

    public float getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(float lunghezza) {
        this.lunghezza = lunghezza;
    }
}
