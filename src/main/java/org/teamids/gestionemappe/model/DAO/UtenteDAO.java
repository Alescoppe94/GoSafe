package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Utente;
import org.teamids.gestionemappe.model.entity.TroncoEntity;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.ResultSet;
import java.util.*;

public class UtenteDAO extends Observable {

    protected Utente tabella;

    public UtenteDAO() {

        this.tabella = new Utente();

    }

    public void insertUser(UtenteEntity utente){
        String dati= String.valueOf(utente.getId());
        dati=dati+",'"+utente.getUsername()+"'";
        dati=dati+",'"+utente.getPassword()+"'";
        dati=dati+",'"+utente.getNome()+"'";
        dati=dati+",'"+utente.getCognome()+"'";
        dati=dati+",null";
        dati=dati+",null";
        dati=dati+",1";
        dati=dati+",'" + utente.getToken() + "'";
        tabella.insert(dati);
        int id_utente = tabella.executeForKey();
        utente.setId(id_utente);
    }

    public UtenteEntity getUserByUsername(String username){
        tabella.select();
        tabella.where("username = '" + username + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        UtenteEntity utente = new UtenteEntity();
        utente.setId(Integer.parseInt(rs.get(0).get("id").toString()));
        utente.setUsername(rs.get(0).get("username").toString());
        utente.setNome(rs.get(0).get("nome").toString());
        utente.setCognome(rs.get(0).get("cognome").toString());
        utente.setPassword(rs.get(0).get("password").toString());
        //utente.setBeaconId(risultato.getInt("beaconId"));
        //utente.setPercorso(new PercorsoDAO().getPercorsoById(risultato.getInt("percorsoId")));
        return utente;
    }

    public boolean isAutenticato(String user, String pass){
        boolean success = false;
        tabella.select();
        tabella.where("username ='" + user + "' and password = '" + pass + "' and is_autenticato = 1 ");
        if(tabella.fetch().size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    public boolean findUserByUsername(String user){
        boolean success = false;
        tabella.select();
        tabella.where("username='" + user + "'");
        if(tabella.fetch().size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    public void updatePositionInEmergency(int id, int beaconId, TroncoEntity tronco){
        String dati = "beaconId = '" + beaconId + "'";
        tabella.update(dati);
        tabella.where("id ='" + id + "'");
        tabella.execute();
        setChanged();
        notifyObservers(tronco);
    }

    public void updateInfoUtente(int id, Map<String,Object> campoutente){
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
        tabella.execute();
    }

    public void logout(String username){
        String dati = "percorsoId = NULL, beaconId = NULL, is_autenticato = 0";
        tabella.update(dati);
        tabella.where("username='" + username + "'");
        tabella.execute();
    }

    public ArrayList<Integer> getBeaconsIdAttivi() {
        tabella.select("beaconId");
        tabella.innerjoin("beacon","utente.beaconId = beacon.id");
        tabella.where("beacon.is_puntodiraccolta = 0 AND utente.is_autenticato = 1 ");
        tabella.groupby("beaconId");
        List<Map<String, Object>> rs = tabella.fetch();
        ArrayList<Integer> beaconsAttivi = new ArrayList<>();
        for (int i = 0; i<rs.size(); i++) {
            beaconsAttivi.add(Integer.parseInt(rs.get(i).get("beaconId").toString()));
        }
        return beaconsAttivi;
    }

    public ArrayList<String> getTokensAttivi() {
        tabella.select("token");
        tabella.where("is_autenticato = 1 ");
        tabella.groupby("token");
        List<Map<String, Object>> rs = tabella.fetch();
        ArrayList<String> tokensAttivi = new ArrayList<>();
        for (int i = 0; i<rs.size(); i++) {
            tokensAttivi.add(rs.get(i).get("token").toString());
        }
        return tokensAttivi;
    }

    public boolean existUtenteInPericolo() {
        boolean success = false;
        tabella.select("beaconId");
        tabella.innerjoin("beacon","utente.beaconId = beacon.id");
        tabella.where("beacon.is_puntodiraccolta = 0 ");
        if(tabella.fetch().size()>=1)
            success = true;
        else
            success=false;
        return success;
    }

    public int countUsersPerTronco(ArrayList<Integer> percorsiId) {
        int users = 0;
        for(int percorsoId: percorsiId){
            tabella.select();
            tabella.where("percorsoId = '" + percorsoId + "'");
            users += tabella.count(tabella.fetch());
        }
        return users;

    }
}
