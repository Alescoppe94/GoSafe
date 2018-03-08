package org.teamids.gestionemappe.model.entity;

public class Piano {

    private int id;
    private String immagine;
    private int piano;
    private Tronco[] tronchi;

    public Piano(String immagine, int piano, Tronco[] tronchi) {
        this.immagine = immagine;
        this.piano = piano;
        this.tronchi = tronchi;
    }

    public int getId() {
        return id;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public Tronco[] getTronchi() {
        return tronchi;
    }

    public void setTronchi(Tronco[] tronchi) {
        this.tronchi = tronchi;
    }
}
