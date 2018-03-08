package org.teamids.gestionemappe.model.entity;

public class Tappa {

    private int id;
    private Tronco tronco;

    public Tappa(Tronco tronco) {
        this.tronco = tronco;
    }

    public int getId() {
        return id;
    }

    public Tronco getTronco() {
        return tronco;
    }

    public void setTronco(Tronco tronco) {
        this.tronco = tronco;
    }
}
