package org.teamids.gestionemappe.model.entity;

public class BeaconEntity {

    private String id;
    private boolean puntodiraccolta;
    private PianoEntity piano;

    public BeaconEntity() {
    }

    public BeaconEntity(String id, boolean puntodiraccolta, PianoEntity piano) {
        this.id = id;
        this.puntodiraccolta = puntodiraccolta;
        this.piano = piano;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public boolean is_puntodiraccolta() {
        return puntodiraccolta;
    }

    public void setPuntodiraccolta(boolean puntodiraccolta) {
        this.puntodiraccolta = puntodiraccolta;
    }

    public PianoEntity getPiano() {
        return piano;
    }

    public void setPiano(PianoEntity piano) {
        this.piano = piano;
    }

}
