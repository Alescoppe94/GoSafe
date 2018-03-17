package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Utente;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.ResultSet;
import java.util.Iterator;
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
        tabella.insert(dati);
        int id_utente = tabella.executeForKey();
        utente.setId(id_utente);
    }

    public UtenteEntity getUserByUsername(String username){
        tabella.select();
        tabella.where("username = '" + username + "'" );
        ResultSet risultato = tabella.fetch();
        try{
            risultato.next();
            UtenteEntity utente = new UtenteEntity();
            utente.setId(risultato.getInt("id"));
            utente.setUsername(risultato.getString("username"));
            utente.setNome(risultato.getString("nome"));
            utente.setCognome(risultato.getString("cognome"));
            utente.setPassword(risultato.getString("password"));
            //utente.setBeaconId(risultato.getInt("beaconId"));
            //utente.setPercorso(new PercorsoDAO().getPercorsoById(risultato.getInt("percorsoId")));
            return utente;
        }
        catch (Exception e){
            System.out.println("Errore" + e);
            return null;
        }
    }

    public boolean findUserByCredential(String user, String pass){
        boolean success = false;
        tabella.select();
        tabella.where("username='" + user + "' and password='" + pass+"'");
        int n = tabella.count(tabella.fetch());
        if(n==1)
            success = true;
        else
            success=false;
        return success;
    }

    public boolean findUserByUsername(String user){
        boolean success = false;
        tabella.select();
        tabella.where("username='" + user + "'");
        int n = tabella.count(tabella.fetch());
        if(n==1)
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
