package org.teamids.gestionemappe.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestioneDB {

    //private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/grp_50_db";

    Connection conn;

    public GestioneDB() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(JDBC_URL, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(this.conn != null) {
            System.out.println("connected to database");
           // createTable();
        }

    }

    public void createTable(){
        try {
            conn.createStatement().execute("CREATE TABLE Utente(Nome VARCHAR(5))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean verificaAggiornamentiDB(){
        return true;
    }

    public void aggiornaDB(Object object){

    }

}
