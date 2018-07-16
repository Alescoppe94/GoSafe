package org.teamids.gestionemappe.model.entity;

import java.time.LocalDateTime;

/**
 * Classe che modella l'entity notifica
 */
public class NotificaEntity {

    private int id;
    private int utenteId;
    private PercorsoEntity percorso;
    private LocalDateTime dataorario;
    private String messaggio;

    /**
     * Costruttore
     * @param utenteId id dell'utente a cui inviare la notifica
     * @param percorso oggetto Percorso
     * @param dataorario orario di invio notifica
     * @param messaggio messaggio di testo per l'utente
     */
    public NotificaEntity(int utenteId, PercorsoEntity percorso, LocalDateTime dataorario, String messaggio) {
        this.utenteId = utenteId;
        this.percorso = percorso;
        this.dataorario = dataorario;
        this.messaggio = messaggio;
    }

    /**
     * metodo getter per l'id della notifica
     * @return id della notifica
     */
    public int getId() {
        return id;
    }

    /**
     * metodo setter per l'id della notifica
     * @param id id della notifica
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * metodo getter per l'id dell'utente
     * @return id dell'utente
     */
    public int getUtenteId() {
        return utenteId;
    }

    /**
     * metodo getter per l'oggetto Percorso
     * @return il percorso per l'utente
     */
    public PercorsoEntity getPercorso() {
        return percorso;
    }

    /**
     * metodo getter per l'orario della notifica
     * @return orario della notifica
     */
    public LocalDateTime getDataorario() {
        return dataorario;
    }

    /**
     * metodo getter per il messaggio della notifica
     * @return messaggio della notifica
     */
    public String getMessaggio() {
        return messaggio;
    }

    /**
     * metodo setter per il Percorso.
     * @param percorso percorso da impostare nell'oggetto
     */
    public void setPercorso(PercorsoEntity percorso) {
        this.percorso = percorso;
    }

    /**
     * metodo setter per l'id dell'utente
     * @param utenteId id dell'utente
     */
    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    /**
     * metodo setter per l'orario della notifica
     * @param dataorario orario della notifica
     */
    public void setDataorario(LocalDateTime dataorario) {
        this.dataorario = dataorario;
    }

    /**
     * metodo setter per il messaggio della notifica
     * @param messaggio messaggio della notifica
     */
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}
