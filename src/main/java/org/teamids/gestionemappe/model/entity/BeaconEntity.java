package org.teamids.gestionemappe.model.entity;

public class BeaconEntity {

    private String id;
    private boolean is_puntodiraccola;
    private PianoEntity piano;

    public BeaconEntity() {
    }

    public BeaconEntity(String id, boolean is_puntodiraccola, PianoEntity piano) {
        this.id = id;
        this.is_puntodiraccola = is_puntodiraccola;
        this.piano = piano;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

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
