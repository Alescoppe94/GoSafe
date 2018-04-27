package org.teamids.gestionemappe.model.entity;

import java.time.LocalDateTime;

public class NotificaEntity {

    private int id;
    private int utenteId;
    private PercorsoEntity percorso;
    private LocalDateTime dataorario;
    private String messaggio;

    public NotificaEntity(int utenteId, PercorsoEntity percorso, LocalDateTime dataorario, String messaggio) {
        this.utenteId = utenteId;
        this.percorso = percorso;
        this.dataorario = dataorario;
        this.messaggio = messaggio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getDataorario() {
        return dataorario;
    }

    public void setDataorario(LocalDateTime dataorario) {
        this.dataorario = dataorario;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}
