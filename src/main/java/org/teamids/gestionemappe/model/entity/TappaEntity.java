package org.teamids.gestionemappe.model.entity;

public class TappaEntity {

    private int id;
    private int percorsoId;
    private TroncoEntity tronco;
    private boolean direzione;

    public TappaEntity(TroncoEntity tronco, int percorsoId,boolean direzione) {
        this.id=0;
        this.percorsoId= percorsoId;
        this.tronco = tronco;
        this.direzione = direzione;
    }

    public TappaEntity(int id, int percorsoId, TroncoEntity tronco, boolean direzione) {
        this.id=id;
        this.percorsoId= percorsoId;
        this.tronco = tronco;

    }

    public TappaEntity(TroncoEntity tronco, boolean direzione) {
        this.tronco = tronco;
        this.direzione = direzione;
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

    public boolean isDirezione() {
        return direzione;
    }

    public void setDirezione(boolean direzione) {
        this.direzione = direzione;
    }
}
