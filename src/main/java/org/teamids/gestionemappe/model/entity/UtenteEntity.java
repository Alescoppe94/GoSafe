package org.teamids.gestionemappe.model.entity;

/**
 * Classe che modella l'entity utente
 */
public class UtenteEntity {

    private int id;
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String beaconId;
    private PercorsoEntity percorso;
    private boolean is_autenticato;
    private String token;
    private String idsessione;

    /**
     * Costruttore
     */
    public UtenteEntity(){
    }

    /**
     * metodo getter per l'username dell'utente
     * @return l'username dell'utente
     */
    public String getUsername() { return username; }

    /**
     * metodo setter per l'username dell'utente
     * @param username l'username dell'utente
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * metodo getter per la password dell'utente
     * @return password dell'utente
     */
    public String getPassword() { return password; }

    /**
     * metodo setter per la password dell'utente
     * @param password password dell'utente
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * metodo getter per l'id dell'utente
     * @return id dell'utente
     */
    public int getId() {
        return id;
    }

    /**
     * metodo setter per l'id dell'utente
     * @param id id dell'utente
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * metodo getter per il nome dell'utente
     * @return nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * metodo setter per il nome dell'utente
     * @param nome nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * metodo getter per il cognome dell'utente
     * @return cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * metodo setter per il cognome dell'utente
     * @param cognome cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * metodo getter per l'id del beacon a cui è connesso l'utente
     * @return id del beacon
     */
    public String getBeaconId() {
        return beaconId;
    }

    /**
     * metodo setter per stabilire se un utente è autenticato o meno
     * @param is_autenticato booleano che indica se un utente è autenticato o meno
     */
    public void setIs_autenticato(boolean is_autenticato) {
        this.is_autenticato = is_autenticato;
    }

    /**
     * metodo getter per il token dell'app dell'utente
     * @return il token dell'app dell'utente
     */
    public String getToken() {
        return token;
    }

    /**
     * metodo getter per il token di sessione dell'utente
     * @return token di sessione
     */
    public String getIdsessione() {
        return idsessione;
    }

    /**
     * metodo setter per il token di sessione dell'utente
     * @param idsessione token di sessione
     */
    public void setIdsessione(String idsessione) {
        this.idsessione = idsessione;
    }

    public void setBeaconId(String beaconId) {
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

    public void setToken(String token) {
        this.token = token;
    }
}
