package org.teamids.gestionemappe.model;

public class Tronco {

    private int id;
    private int beaconId;
    private boolean agibile;
    private float costo;
    private Piano piano;

    public Tronco(int beaconId, boolean agibile, Piano piano) {
        this.beaconId = beaconId;
        this.agibile = agibile;
        this.piano = piano;
    }

    public int getId() {
        return id;
    }

    public int getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(int beaconId) {
        this.beaconId = beaconId;
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

    public Piano getPiano() {
        return piano;
    }

    public void setPiano(Piano piano) {
        this.piano = piano;
    }
}
