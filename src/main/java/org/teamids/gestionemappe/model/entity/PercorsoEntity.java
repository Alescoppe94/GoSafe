package org.teamids.gestionemappe.model.entity;

public class PercorsoEntity {

    private int id;
    private TappaEntity[] tappe;
    private CorridoioEntity[] corridoi;

    public PercorsoEntity(TappaEntity[] tappe, CorridoioEntity[] corridoi) {
        this.tappe = tappe;
        this.corridoi = corridoi;
    }

    public int getId() {
        return id;
    }

      public TappaEntity[] getTappe() {
        return tappe;
    }

    public void setTappe(TappaEntity[] tappe) {
        this.tappe = tappe;
    }

    public CorridoioEntity[] getCorridoi() {
        return corridoi;
    }

    public void setCorridoi(CorridoioEntity[] corridoi) {
        this.corridoi = corridoi;
    }
}
