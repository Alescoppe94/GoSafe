package org.teamids.gestionemappe.model.entity;

import java.util.LinkedList;

public class PercorsoEntity {

    private int id;
    private LinkedList<TappaEntity> tappe;
    private BeaconEntity beaconPartenza;

    public PercorsoEntity(int id, LinkedList<TappaEntity> tappe, BeaconEntity beaconPartenza) {
        this.id = id;
        this.tappe = tappe;
        this.beaconPartenza = beaconPartenza;
    }

    public PercorsoEntity(LinkedList<TappaEntity> tappe, BeaconEntity beaconPartenza) {
        this.tappe = tappe;
        this.beaconPartenza = beaconPartenza;
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

    public BeaconEntity getBeaconPartenza() {
        return beaconPartenza;
    }

    public void setBeaconPartenza(BeaconEntity beaconPartenza) {
        this.beaconPartenza = beaconPartenza;
    }


}
