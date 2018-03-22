package org.teamids.gestionemappe.model.entity;

public class PercorsoEntity {

    private int id;
    private TappaEntity[] tappe;
    private BeaconEntity beaconPartenzaId;

    public PercorsoEntity(TappaEntity[] tappe, BeaconEntity beaconPartenzaId) {
        this.tappe = tappe;
        this.beaconPartenzaId = beaconPartenzaId;
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

    public BeaconEntity getBeaconPartenzaId() {
        return beaconPartenzaId;
    }

    public void setBeaconPartenzaId(BeaconEntity beaconPartenzaId) {
        this.beaconPartenzaId = beaconPartenzaId;
    }
}
