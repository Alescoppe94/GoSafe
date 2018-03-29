package org.teamids.gestionemappe.model.entity;


import org.teamids.gestionemappe.model.DAO.UtenteDAO;

public class BeaconEntity {

    private int id;
    private float v;
    private float r;
    private float k;
    private float l;
    private boolean is_puntodiraccola;
    private PianoEntity piano;

    public BeaconEntity() {
    }

    public BeaconEntity(int id, float v, float r, float k, float l, boolean is_puntodiraccola, PianoEntity piano) {
        this.id = id;
        this.v = v;
        this.r = r;
        this.k = k;
        this.l = l;
        this.is_puntodiraccola = is_puntodiraccola;
        this.piano = piano;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }

    public boolean is_puntodiraccola() {
        return is_puntodiraccola;
    }

    public void setIs_puntodiraccola(boolean is_puntodiraccola) {
        this.is_puntodiraccola = is_puntodiraccola;
    }

    public PianoEntity getPiano() {
        return piano;
    }

    public void setPiano(PianoEntity piano) {
        this.piano = piano;
    }

}
