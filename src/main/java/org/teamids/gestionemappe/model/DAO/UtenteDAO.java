package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Utente;
import org.teamids.gestionemappe.model.entity.TroncoEntity;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.Connection;
import java.util.*;

/**
 * Classe che si occupa di implementare i metodi che interagiscono con la tabella Utente del database
 */
public class UtenteDAO extends Observable implements UtenteDAOInterface {

    protected Utente tabella;

    /**
     * Costruttore della classe UtenteDAO
     */
    public UtenteDAO() {

        this.tabella = new Utente();

    }

    /**
     * Permette di inserire un nuovo utente
     * @param utente oggetto di tipo UtenteEntity contenente le info dell'utente da inserire
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void insertUser(UtenteEntity utente, Connection db){
        String dati= String.valueOf(utente.getId());
        dati=dati+",'"+utente.getUsername()+"'";
        dati=dati+",'"+utente.getPassword()+"'";
        dati=dati+",'"+utente.getNome()+"'";
        dati=dati+",'"+utente.getCognome()+"'";
        dati=dati+",null";
        dati=dati+",null";
        dati=dati+",1";
        dati=dati+",'" + utente.getToken() + "'";
        dati=dati+",'" + utente.getIdsessione() + "'";
        tabella.insert(dati);
        int id_utente = tabella.executeForKey(db);
        utente.setId(id_utente);
    }

    /**
     * Permette di recuperare le info di un utente, a partire dal suo username
     * @param username username dell'utente
     * @param db parametro utilizzato per la connessione al database
     * @return oggetto di tipo UtenteEntity contenente le info dell'utente con quel username
     */
    @Override
    public UtenteEntity getUserByUsername(String username, Connection db){
        tabella.select();
        tabella.where("username = '" + username + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        UtenteEntity utente = null;
        if (rs.size() != 0) {
            utente = new UtenteEntity();
            utente.setId(Integer.parseInt(rs.get(0).get("id").toString()));
            utente.setUsername(rs.get(0).get("username").toString());
            utente.setNome(rs.get(0).get("nome").toString());
            utente.setCognome(rs.get(0).get("cognome").toString());
            utente.setPassword(rs.get(0).get("password").toString());
            //utente.setBeaconId(risultato.getInt("beaconId"));
            //utente.setPercorso(new PercorsoDAO().getPercorsoById(risultato.getInt("percorsoId")));
        }
        return utente;
    }

    /**
     * Permette di verificare se un certo utente, con un certo idsessione, è autenticato
     * @param idsessione identificatore di un certo utente
     * @param db parametro utilizzato per la connessione al database
     * @return True se l'utente è correttamente autenticato e l'idsessione dell'utente coincide con quello memorizzato nel db,
     * altrimenti False
     */
    @Override
    public boolean isAutenticato(String idsessione, Connection db){
        boolean success = false;
        tabella.select();
        tabella.where("idsessione ='" + idsessione + "' and is_autenticato = 1 ");
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    /**
     * Permette di verificare se esiste un utente con un determinato username
     * @param user username di cui si cui si vuole verificare la presenza
     * @param db parametro utilizzato per la connessione al database
     * @return True se esiste un utente con quel username, altrimenti False
     */
    @Override
    public boolean findUserByUsername(String user, Connection db){
        boolean success = false;
        tabella.select();
        tabella.where("username='" + user + "'");
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    /**
     * Permette di aggiornare la posizione di un utente, in fase emergenza
     * @param id identificatore dell'utente
     * @param beaconId identificatore del beacon a cui è connesso l'utente
     * @param tronco oggetto di tipo TroncoEntity che contiene le info del tronco in cui si trova l'utente
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void updatePositionInEmergency(int id, String beaconId, TroncoEntity tronco, Connection db){
        String dati = "beaconId = '" + beaconId + "'";
        tabella.update(dati);
        tabella.where("id ='" + id + "'");
        tabella.execute(db);
        setChanged();
        ArrayList<Object> parametri = new ArrayList<>();
        parametri.add(tronco);
        parametri.add(db);
        notifyObservers(parametri);
    }

    /**
     * Permette di aggiornare le info memorizzare nel db relative ad un utente
     * @param id identificatore dell'utente
     * @param campoutente HashMap che contiene un'insieme di coppie chiave-valore,
     *                    in cui la chiave è il campo che si deve modificare e il valore è il nuovo valore per quel campo
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void updateInfoUtente(int id, Map<String, Object> campoutente, Connection db){
        String dati = "";
        Iterator<Map.Entry<String, Object>> itr = campoutente.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> campo = itr.next();
            dati += campo.getKey()+ "='" + campo.getValue() + "'";
            if(itr.hasNext())
                dati+= ", ";
        }
        tabella.update(dati);
        tabella.where("id ='" + id + "'");
        tabella.execute(db);
    }

    /**
     * Permette di dis-autenticare un utente che ha deciso di effettuare il logout
     * @param username username dell'utente da dis-autenticare
     * @param db parametro utilizzato per la connessione al database
     */
    @Override
    public void logout(String username, Connection db){
        String dati = "percorsoId = NULL, beaconId = NULL, is_autenticato = 0, idsessione = NULL";
        tabella.update(dati);
        tabella.where("username='" + username + "'");
        tabella.execute(db);
    }

    /**
     * Permette di ottenere gli identificatori dei beacon associati ad utenti autenticati
     * @param db parametro utilizzato per la connessione al database
     * @return Arraylist di stringhe contenente gli identificatori dei beacon associati ad utenti autenticati
     */
    @Override
    public ArrayList<String> getBeaconsIdAttivi(Connection db) {
        tabella.select("beaconId");
        tabella.innerjoin("beacon","utente.beaconId = beacon.id");
        tabella.where("beacon.is_puntodiraccolta = 0 AND utente.is_autenticato = 1 ");
        tabella.groupby("beaconId");
        List<Map<String, Object>> rs = tabella.fetch(db);
        ArrayList<String> beaconsAttivi = new ArrayList<>();
        for (int i = 0; i<rs.size(); i++) {
            beaconsAttivi.add(rs.get(i).get("beaconId").toString());
        }
        return beaconsAttivi;
    }

    /**
     * Permette di ottenere i token associati ad utenti autenticati
     * @param db parametro utilizzato per la connessione al database
     * @return Arraylist di stringhe contenente i token associati ad utenti autenticati
     */
    @Override
    public ArrayList<String> getTokensAttivi(Connection db) {
        tabella.select("token");
        tabella.where("is_autenticato = 1 ");
        tabella.groupby("token");
        List<Map<String, Object>> rs = tabella.fetch(db);
        ArrayList<String> tokensAttivi = new ArrayList<>();
        for (int i = 0; i<rs.size(); i++) {
            tokensAttivi.add(rs.get(i).get("token").toString());
        }
        return tokensAttivi;
    }

    /**
     * Verifica se esiste almeno un utente in pericolo, in fase di emergenza
     * @param db parametro utilizzato per la connessione al database
     * @return True se in fase di emergenza esiste almeno un utente connesso ad un beacon che non sia un punto di raccolta,
     *          altrimenti False
     */
    @Override
    public boolean existUtenteInPericolo(Connection db) {
        boolean success = false;
        tabella.select("beaconId");
        tabella.innerjoin("beacon","utente.beaconId = beacon.id");
        tabella.where("beacon.is_puntodiraccolta = 0 ");
        if(tabella.fetch(db).size()>=1)
            success = true;
        else
            success=false;
        return success;
    }

    /**
     * Conta il numero totale di persone che hanno un percorso appartenente ad una lista di percorsi
     * @param percorsiId ArrayList di interi contenenti gli identificatori dei percorsi
     * @param db parametro utilizzato per la connessione al database
     * @return numero totale di persone
     */
    @Override
    public int countUsersPerTronco(ArrayList<Integer> percorsiId, Connection db) {
        int users = 0;
        for(int percorsoId: percorsiId){
            tabella.select();
            tabella.where("percorsoId = '" + percorsoId + "'");
            users += tabella.count(tabella.fetch(db));
        }
        return users;

    }

    /**
     * Conta il numero di persone collegate ad un beacon
     * @param beaconId identificatore del beacon
     * @param db parametro utilizzato per la connessione al database
     * @return il numero di persone che sono collegato a quel beacon
     */
    @Override
    public int countUsersPerBeacon(String beaconId, Connection db) {
        int users = 0;
        tabella.select();
        tabella.where("beaconId = '" + beaconId + "'");
        users += tabella.count(tabella.fetch(db));
        return users;

    }

    /**
     * Permette di verificare se esiste un utente con un certo identificatore ed username
     * @param username username dell'utente
     * @param id identificatore dell'utente
     * @param db parametro utilizzato per la connessione al database
     * @return True se è presente un utente con quel username ed identificatore, altrimenti False.
     */
    @Override
    public boolean isUsernameIdPresent(String username, int id, Connection db){
        boolean success = false;
        tabella.select();
        tabella.where("id='" + id + "' and username='"+ username +"'");
        int n = tabella.count(tabella.fetch(db));
        if(n==1)
            success = true;
        else
            success=false;
        return success;
    }

}
