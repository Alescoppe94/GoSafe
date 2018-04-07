package org.teamids.gestionemappe.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbTableHelpers {

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

    public int count(List<Map<String, Object>> tuple){
        return tuple.size();
    }

    public void groupby(String campo){
        sql = sql + "GROUP BY " + campo;
    }

    public void innerjoin(String tabella, String uguaglianza){
        sql = sql + " INNER JOIN " + tabella + " ON " + uguaglianza;
    }

    public boolean execute(){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        boolean check = false;
        sql = sql + ";";
        try {
            PreparedStatement query = db.prepareStatement(sql);
            query.executeUpdate();
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            check=false;
        }
        connector.disconnect();
        return check;
    }

    public int executeForKey(){
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        sql = sql + ";";
        int generated_key = 0;
        try {
            Statement query = db.createStatement();
            query.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = query.getGeneratedKeys();
            try {
                rs.next();
                generated_key = rs.getInt("GENERATED_KEY");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connector.disconnect();
        return generated_key;
    }

    public List<Map<String, Object>> fetch(){

        ResultSet risultato= null;
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        List<Map<String, Object>> lista;
        sql = sql + ";";
        try {
            Statement query = db.createStatement();
            risultato=query.executeQuery(sql);
            lista=resultSetToList(risultato);
        } catch (Exception e) {
            e.printStackTrace();
            lista = null;
        }
        connector.disconnect();
        return lista;
    }

    private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (rs.next()){
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for(int i = 1; i <= columns; ++i){
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

}
