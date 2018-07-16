package org.teamids.gestionemappe.model.entity;

/**
 * Classe che modella l'entity beacon
 */
public class BeaconEntity {

    private String id;
    private boolean is_puntodiraccolta;
    private int piano;
    private int coordx;
    private int coordy;

    /**
     * Costruttore
     */
    public BeaconEntity() {
    }

    /**
     * Costruttore
     * @param id id del beacon
     */
    public BeaconEntity(String id){
        this.id = id;
    }

    /**
     * Costruttore
     * @param id id del beacon
     * @param is_puntodiraccolta se il beacon è un punto di raccolta
     * @param piano numero del piano in cui si trova il beacon
     * @param coordx coordinata x del beacon sulla mappa
     * @param coordy coordinata y del beacon sulla mappa
     */
    public BeaconEntity(String id, boolean is_puntodiraccolta, int piano, int coordx, int coordy) {
        this.id = id;
        this.is_puntodiraccolta = is_puntodiraccolta;
        this.piano = piano;
        this.coordx = coordx;
        this.coordy = coordy;
    }

    /**
     * metodo getter per l'id del beacon
     * @return id del beacon
     */
    public String getId() {
        return id;
    }

    /**
     * metodo setter per l'id del beacon
     * @param id id del beacon
     */
    public void setId(String id) { this.id = id; }

    /**
     * metodo getter per sapere se il beacon è un punto di raccolta
     * @return booleano che indica se il beacon è di raccolta o no
     */
    public boolean isIs_puntodiraccolta() {
        return is_puntodiraccolta;
    }

    /**
     * metodo setter per definire se un beacon è un punto di raccolta o no
     * @param is_puntodiraccolta booleano che indica se il beacon è di raccolta o no
     */
    public void setIs_puntodiraccolta(boolean is_puntodiraccolta) {
        this.is_puntodiraccolta = is_puntodiraccolta;
    }

    /**
     * metodo setter per il numero del piano del beacon
     * @param piano numero del piano
     */
    public void setPiano(int piano) {
        this.piano = piano;
    }

    /**
     * metodo getter per la coordinata x del beacon
     * @return coordinata x del beacon
     */
    public int getCoordx() {
        return coordx;
    }

    /**
     * metodo setter per la coordinata x del beacon
     * @param coordx coordinata x del beacon
     */
    public void setCoordx(int coordx) {
        this.coordx = coordx;
    }

    /**
     * metodo getter per la coordinata y del beacon
     * @return coordinata y del beacon
     */
    public int getCoordy() {
        return coordy;
    }

    /**
     * metodo setter per la coordinata y del beacon
     * @param coordy coordinata y del beacon
     */
    public void setCoordy(int coordy) {
        this.coordy = coordy;
    }

    /**
     * metodo getter per il piano del beacon
     * @return il piano del beacon
     */
    public int getPiano() {
        return piano;
    }



}