package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Utente;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UtenteDAO {

    protected Utente tabella;

    public UtenteDAO() {
        tabella= new Utente();
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
        dati=dati+",null";
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

    public boolean findUserByCredential(String user, String pass){
        boolean success = false;
        tabella.select();
        tabella.where("username='" + user + "' and password='" + pass+"'");
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

    public void logout(String username){
        String dati = "percorsoId = NULL, beaconId = NULL";
        tabella.update(dati);
        tabella.where("username='" + username + "'");
        tabella.execute();
    }
}
