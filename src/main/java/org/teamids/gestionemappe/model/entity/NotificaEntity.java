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
     * @param percorso
     */
    public void setPercorso(PercorsoEntity percorso) {
        this.percorso = percorso;
    }
}
