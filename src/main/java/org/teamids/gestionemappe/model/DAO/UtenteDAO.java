package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Utente;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.ResultSet;

public class UtenteDAO {

    protected Utente tabella;

    public UtenteDAO() {
        tabella= new Utente();
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
}
