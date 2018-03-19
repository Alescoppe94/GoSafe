package org.teamids.gestionemappe.model.entity;

import java.util.Date;

public class NotificaEntity {

    private int id;
    private int utenteId;
    private PercorsoEntity percorso;
    private Date orario;
    private String messaggio;

    public NotificaEntity(int utenteId, PercorsoEntity percorso, String messaggio) {
        this.utenteId = utenteId;
        this.percorso = percorso;
        this.messaggio = messaggio;
    }

    public int getId() {
        return id;
    }

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public PercorsoEntity getPercorso() {
        return percorso;
    }

    public void setPercorso(PercorsoEntity percorso) {
        this.percorso = percorso;
    }

    public Date getOrario() {
        return orario;
    }

    public void setOrario(Date orario) {
        this.orario = orario;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}
