package org.teamids.gestionemappe.model.entity;

/**
 * Classe che modella l'entity piano
 */
public class PianoEntity {

    private int id;
    private String immagine;
    private int piano;
    private TroncoEntity[] tronchi;

    /**
     * Costruttore
     */
    public PianoEntity(){
    }

    /**
     * Costruttore
     * @param id id del piano
     * @param immagine immagine della mappa del piano
     * @param piano numero del piano
     */
    public PianoEntity(int id, String immagine, int piano) {
        this.id = id;
        this.immagine = immagine;
        this.piano = piano;
    }

    /**
     * Costruttore
     * @param immagine immagine della mappa del piano
     * @param piano numero del piano
     */
    public PianoEntity(String immagine, int piano) {
        this.immagine = immagine;
        this.piano = piano;
    }

    /**
     * metodo getter per l'id del piano
     * @return id del piano
     */
    public int getId() {
        return id;
    }

    /**
     * metodo setter per l'id del piano
     * @param id id del piano
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * metodo getter per l'immagine della mappa del piano
     * @return immagine sotto forma di stringa base 64
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * metodo setter per l'immagine della mappa del piano
     * @param immagine immagine sotto forma di stringa base 64
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * metodo getter per il numero del piano
     * @return numero del piano
     */
    public int getPiano() {
        return piano;
    }

    /**
     * metodo setter per il numero del piano
     * @param piano numero del piano
     */
    public void setPiano(int piano) {
        this.piano = piano;
    }

    public TroncoEntity[] getTronchi() {
        return tronchi;
    }

    public void setTronchi(TroncoEntity[] tronchi) {
        this.tronchi = tronchi;
    }

}
