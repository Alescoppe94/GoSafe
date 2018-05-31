package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Utente;
import org.teamids.gestionemappe.model.entity.TroncoEntity;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.Connection;
import java.util.*;

public class UtenteDAO extends Observable {

    protected Utente tabella;

    public UtenteDAO() {

        this.tabella = new Utente();

    }

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

    public void updateInfoUtente(int id, Map<String,Object> campoutente, Connection db){
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

    public void logout(String username, Connection db){
        String dati = "percorsoId = NULL, beaconId = NULL, is_autenticato = 0, idsessione = NULL";
        tabella.update(dati);
        tabella.where("username='" + username + "'");
        tabella.execute(db);
    }

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

    public int countUsersPerTronco(ArrayList<Integer> percorsiId, Connection db) {
        int users = 0;
        for(int percorsoId: percorsiId){
            tabella.select();
            tabella.where("percorsoId = '" + percorsoId + "'");
            users += tabella.count(tabella.fetch(db));
        }
        return users;

    }

    public int countUsersPerBeacon(String beaconId, Connection db) {
        int users = 0;
        tabella.select();
        tabella.where("beaconId = '" + beaconId + "'");
        users += tabella.count(tabella.fetch(db));
        return users;

    }

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
