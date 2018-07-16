package org.teamids.gestionemappe.model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe che gestisce la connessione al database
 */
public class ConnectorHelpers {
    private Connection conn;
    private String user = "root";
    private String password = "";
    private String dbname = "gosafe";

    /**
     * Apre la connessione al database
     * @return un oggetto di tipo Connection che rappresenta la connessione al database
     */
    public Connection connect()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            this.conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+this.dbname,this.user,this.password);
            return conn;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Chiude la connessione al database
     */
    public void disconnect(){
        try {
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
