package org.teamids.gestionemappe.model.entity;

import java.util.LinkedList;

/**
 * Classe che modella l'entity percorso
 */
public class PercorsoEntity {

    private int id;
    private LinkedList<TappaEntity> tappe;
    private BeaconEntity beaconPartenza;

    /**
     * Costruttore
     * @param id id del percorso
     * @param tappe lista di tappe che formano un percorso
     * @param beaconPartenza il beacon da cui parte il percorso
     */
    public PercorsoEntity(int id, LinkedList<TappaEntity> tappe, BeaconEntity beaconPartenza) {
        this.id = id;
        this.tappe = tappe;
        this.beaconPartenza = beaconPartenza;
    }

    /**
     * Costruttore
     * @param tappe lista di tappe che formano un percorso
     * @param beaconPartenza il beacon da cui parte il percorso
     */
    public PercorsoEntity(LinkedList<TappaEntity> tappe, BeaconEntity beaconPartenza) {
        this.tappe = tappe;
        this.beaconPartenza = beaconPartenza;
    }

    /**
     * metodo getter per l'id del percorso
     * @return l'id del percorso
     */
    public int getId() {
        return id;
    }

    /**
     * metodo setter per l'id del percorso
     * @param id id del percorso
     */
    public void setId(int id) { this.id = id;}

    /**
     * metodo getter per la lista di tappe che formano il percorso
     * @return lista di tappe
     */
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
