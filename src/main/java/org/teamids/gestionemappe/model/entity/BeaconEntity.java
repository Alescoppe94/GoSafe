package org.teamids.gestionemappe.model.entity;

public class BeaconEntity {

    private String id;
    private boolean is_puntodiraccolta;
    private PianoEntity piano;
    private float coordx;
    private float coordy;

    public BeaconEntity() {
    }

    public BeaconEntity(String id){
        this.id = id;
    }

    public BeaconEntity(String id, boolean is_puntodiraccolta, PianoEntity piano, float coordx, float coordy) {
        this.id = id;
        this.is_puntodiraccolta = is_puntodiraccolta;
        this.piano = piano;
        this.coordx = coordx;
        this.coordy = coordy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public boolean isIs_puntodiraccolta() {
        return is_puntodiraccolta;
    }

    public void setIs_puntodiraccolta(boolean is_puntodiraccolta) {
        this.is_puntodiraccolta = is_puntodiraccolta;
    }

    public PianoEntity getPiano() {
        return piano;
    }

    public void setPiano(PianoEntity piano) {
        this.piano = piano;
    }

    public float getCoordx() {
        return coordx;
    }

    public void setCoordx(float coordx) {
        this.coordx = coordx;
    }

    public float getCoordy() {
        return coordy;
    }

    public void setCoordy(float coordy) {
        this.coordy = coordy;
    }

}