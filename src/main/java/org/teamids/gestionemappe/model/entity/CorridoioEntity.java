package org.teamids.gestionemappe.model.entity;

public class CorridoioEntity {

    private int id;
    private PianoEntity piano;

    public CorridoioEntity(PianoEntity piano) {
        this.piano = piano;
    }

    public int getId() {
        return id;
    }

    public PianoEntity getPiano() {
        return piano;
    }

    public void setPiano(PianoEntity piano) {
        this.piano = piano;
    }
}
