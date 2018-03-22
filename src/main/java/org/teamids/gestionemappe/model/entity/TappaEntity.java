package org.teamids.gestionemappe.model.entity;

public class TappaEntity {

    private int id;
    private int percorsoId;
    private TroncoEntity tronco;

    public TappaEntity(TroncoEntity tronco, int percorsoId) {
        this.tronco = tronco;
        this.percorsoId= percorsoId;
        this.id=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public TroncoEntity getTronco() {
        return tronco;
    }

    public void setTronco(TroncoEntity tronco) {
        this.tronco = tronco;
    }

    public int getPercorsoId() {
        return percorsoId;
    }

    public void setPercorsoId(int percorsoId) {
        this.percorsoId = percorsoId;
    }
}
