package org.teamids.gestionemappe.model.entity;

/**
 * Classe che modella l'entity tappa
 */
public class TappaEntity {

    private int id;
    private int percorsoId;
    private TroncoEntity tronco;
    private boolean direzione;

    /**
     * Costruttore
     * @param tronco tronco associato alla tappa
     * @param percorsoId id del percorso di cui fa parte la tappa
     * @param direzione direzione in cui prendere la tappa, cioè l'ordine dei beacon che formano il tronco associato,
     *                  se true l'ordine è lo stesso di come sono salvati nel database, se false il viceversa
     */
    public TappaEntity(TroncoEntity tronco, int percorsoId,boolean direzione) {
        this.id=0;
        this.percorsoId= percorsoId;
        this.tronco = tronco;
        this.direzione = direzione;
    }

    /**
     * Costruttore
     * @param id id della tappa
     * @param percorsoId id del percorso di cui fa parte la tappa
     * @param tronco tronco associato alla tappa
     * @param direzione direzione in cui prendere la tappa, cioè l'ordine dei beacon che formano il tronco associato,
     *                  se true l'ordine è lo stesso di come sono salvati nel database, se false il viceversa
     */
    public TappaEntity(int id, int percorsoId, TroncoEntity tronco, boolean direzione) {
        this.id=id;
        this.percorsoId= percorsoId;
        this.tronco = tronco;
        this.direzione = direzione;
    }

    /**
     * Costruttore
     * @param tronco tronco associato alla tappa
     * @param direzione direzione in cui prendere la tappa, cioè l'ordine dei beacon che formano il tronco associato,
     *                  se true l'ordine è lo stesso di come sono salvati nel database, se false il viceversa
     */
    public TappaEntity(TroncoEntity tronco, boolean direzione) {
        this.tronco = tronco;
        this.direzione = direzione;
    }

    /**
     * metodo getter per l'id della tappa
     * @return id della tappa
     */
    public int getId() {
        return id;
    }

    /**
     * metodo setter per l'id della tappa
     * @param id id della tappa
     */
    public void setId(int id) { this.id = id; }

    /**
     * metodo getter per il tronco associato alla tappa
     * @return il tronco
     */
    public TroncoEntity getTronco() {
        return tronco;
    }

    /**
     * metodo setter per il tronco associato alla tappa
     * @param tronco il tronco
     */
    public void setTronco(TroncoEntity tronco) {
        this.tronco = tronco;
    }

    /**
     * metodo getter per l'id del percorso
     * @return id del percorso
     */
    public int getPercorsoId() {
        return percorsoId;
    }

    /**
     * metodo getter per sapere la direzione della tappa
     * @return la direzione della tappa
     */
    public boolean isDirezione() {
        return direzione;
    }

    /**
     * metodo setter per l'id del percorso
     * @param percorsoId id del percorso
     */
    public void setPercorsoId(int percorsoId) {
        this.percorsoId = percorsoId;
    }

    /**
     * metodo setter per la direzione della tappa
     * @param direzione la direzione della tappa
     */
    public void setDirezione(boolean direzione) {
        this.direzione = direzione;
    }

}
