package org.teamids.gestionemappe.model.entity;

public class BeaconEntity {

    private String id;
    private boolean is_puntodiraccolta;
    private int piano;
    private int coordx;
    private int coordy;

    public BeaconEntity() {
    }

    public BeaconEntity(String id){
        this.id = id;
    }

    public BeaconEntity(String id, boolean is_puntodiraccolta, int piano, int coordx, int coordy) {
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

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public int getCoordx() {
        return coordx;
    }

    public void setCoordx(int coordx) {
        this.coordx = coordx;
    }

    public int getCoordy() {
        return coordy;
    }

    public void setCoordy(int coordy) {
        this.coordy = coordy;
    }

}