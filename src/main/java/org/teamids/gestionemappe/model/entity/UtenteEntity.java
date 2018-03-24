package org.teamids.gestionemappe.model.entity;

public class UtenteEntity {

    private int id;
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private int beaconId;
    private PercorsoEntity percorso;
    private boolean is_autenticato;
    private String token;

    public UtenteEntity(){

    }

    public UtenteEntity(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public int getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(int beaconId) {
        this.beaconId = beaconId;
    }

    public PercorsoEntity getPercorso() {
        return percorso;
    }

    public void setPercorso(PercorsoEntity percorso) {
        this.percorso = percorso;
    }

    public boolean is_autenticato() {
        return is_autenticato;
    }

    public void setIs_autenticato(boolean is_autenticato) {
        this.is_autenticato = is_autenticato;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
