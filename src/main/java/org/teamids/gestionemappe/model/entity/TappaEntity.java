package org.teamids.gestionemappe.model.entity;

public class TappaEntity {

    private int id;
    private TroncoEntity tronco;

    public TappaEntity(TroncoEntity tronco) {
        this.tronco = tronco;
    }

    public int getId() {
        return id;
    }

    public TroncoEntity getTronco() {
        return tronco;
    }

    public void setTronco(TroncoEntity tronco) {
        this.tronco = tronco;
    }
}
