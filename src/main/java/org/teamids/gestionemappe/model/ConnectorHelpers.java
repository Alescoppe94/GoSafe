package org.teamids.gestionemappe.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectorHelpers {
    private Connection conn;
    private String user = "root";
    private String password = "";
    private String dbname = "gosafe";

    public Connection connect()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            this.conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+this.dbname,this.user,this.password);
            return conn;
        }
        catch(Exception e){
            System.out.println("Errore di connessione al Db");
        }
        return conn;
    }

    public void disconnect(){
        try {
            conn.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return;
    }
}
