package org.teamids.gestionemappe.model.entity;

import java.util.LinkedList;

public class PercorsoEntity {

    private int id;
    private LinkedList<TappaEntity> tappe;
    private BeaconEntity beaconPartenzaId;

    public PercorsoEntity(int id, LinkedList<TappaEntity> tappe, BeaconEntity beaconPartenzaId) {
        this.id = id;
        this.tappe = tappe;
        this.beaconPartenzaId = beaconPartenzaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public LinkedList<TappaEntity> getTappe() {
        return tappe;
    }

    public void setTappe(LinkedList<TappaEntity> tappe) {
        this.tappe = tappe;
    }

    public BeaconEntity getBeaconPartenzaId() {
        return beaconPartenzaId;
    }

    public void setBeaconPartenzaId(BeaconEntity beaconPartenzaId) {
        this.beaconPartenzaId = beaconPartenzaId;
    }


}
