package org.teamids.gestionemappe.model;

public class Percorso {

    private int id;
    private Tappa[] tappe;
    private Corridoio[] corridoi;

    public Percorso(Tappa[] tappe, Corridoio[] corridoi) {
        this.tappe = tappe;
        this.corridoi = corridoi;
    }

    public int getId() {
        return id;
    }

      public Tappa[] getTappe() {
        return tappe;
    }

    public void setTappe(Tappa[] tappe) {
        this.tappe = tappe;
    }

    public Corridoio[] getCorridoi() {
        return corridoi;
    }

    public void setCorridoi(Corridoio[] corridoi) {
        this.corridoi = corridoi;
    }
}
