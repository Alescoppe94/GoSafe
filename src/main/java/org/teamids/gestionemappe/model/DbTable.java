package org.teamids.gestionemappe.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbTable {

    protected String name;
    protected String sql;

    public void insert(String campi){ sql="insert into " + name + " values("+ campi +")"; }

    public void update(String info){ sql="update "+ name + " set " + info; }

    public void delete(){
        sql="delete from "+name;
    }

    public void select(){
        sql="SELECT * FROM " + name;
    }

    public void select(String campo) { sql="SELECT " +campo+ " FROM " + name;}

    public void where(String clausola){
        sql=sql + " WHERE " + clausola;
    }

    public void order(String clausola){
        sql = sql + "ORDER BY " + clausola;
    }

    /*public boolean execute(){
        Connector connector= new Connector();
        Connection db = connector.connect();
        boolean check = false;
        sql = sql + ";";
        try {
            PreparedStatement query = db.prepareStatement(sql);
            query.executeUpdate();
            check=true;
        } catch (Exception e) {
            System.out.println(e);
            check=false;
        }
        connector.disconnect();
        return check;
    }

    public ResultSet fetch(){

        ResultSet risultato=null;
        Connector connector= new Connector();
        Connection db = connector.connect();
        sql = sql + ";";
        try {
            Statement query = db.createStatement();
            risultato=query.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
        return risultato;
    }*/
}
