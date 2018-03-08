package org.teamids.gestionemappe.model.entity;

public class Corridoio {

    private int id;
    private Piano piano;

    public Corridoio(Piano piano) {
        this.piano = piano;
    }

    public int getId() {
        return id;
    }

    public Piano getPiano() {
        return piano;
    }

    public void setPiano(Piano piano) {
        this.piano = piano;
    }
}
