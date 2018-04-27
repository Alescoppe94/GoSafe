package org.teamids.gestionemappe.model.entity;

public class BeaconEntity {

    private String id;
    private boolean untodiraccolta;
    private PianoEntity piano;

    public BeaconEntity() {
    }

    public BeaconEntity(String id, boolean untodiraccolta, PianoEntity piano) {
        this.id = id;
        this.untodiraccolta = untodiraccolta;
        this.piano = piano;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public boolean is_puntodiraccolta() {
        return untodiraccolta;
    }

    public void setUntodiraccolta(boolean untodiraccolta) {
        this.untodiraccolta = untodiraccolta;
    }

    public PianoEntity getPiano() {
        return piano;
    }

    public void setPiano(PianoEntity piano) {
        this.piano = piano;
    }

}
