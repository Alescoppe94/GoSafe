package org.teamids.gestionemappe.model.entity;

public class BeaconEntity {

    private String id;
    private boolean is_puntodiraccolta;
    private PianoEntity piano;

    public BeaconEntity() {
    }

    public BeaconEntity(String id, boolean is_puntodiraccolta, PianoEntity piano) {
        this.id = id;
        this.is_puntodiraccolta = is_puntodiraccolta;
        this.piano = piano;
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

}