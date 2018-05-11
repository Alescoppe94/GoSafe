package org.teamids.gestionemappe.model.entity;

public class PianoEntity {

    private int id;
    private String immagine;
    private int piano;
    private TroncoEntity[] tronchi;

    public PianoEntity(){

    }

    public PianoEntity(String immagine, int piano) {
        this.immagine = immagine;
        this.piano = piano;
    }

    public PianoEntity(String immagine, int piano, TroncoEntity[] tronchi) {
        this.immagine = immagine;
        this.piano = piano;
        this.tronchi = tronchi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public TroncoEntity[] getTronchi() {
        return tronchi;
    }

    public void setTronchi(TroncoEntity[] tronchi) {
        this.tronchi = tronchi;
    }
}
