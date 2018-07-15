package org.teamids.gestionemappe.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe che gestisce le richieste al database, occupandosi anche di formulare le query sql,
 * viene poi specializzata dalle classi presenti nel package DbTable
 */
public class DbTableHelpers {

    protected String name;
    protected String sql;

    /**
     * query di inserimento
     * @param campi valori da inserire
     */
    public void insert(String campi){ sql= "insert into " + name + " values("+ campi +")"; }

    /**
     * quey di update
     * @param info valori aggiornati
     */
    public void update(String info){ sql="update "+ name + " set " + info; }

    /**
     * query di update
     */
    public void update(){ sql = "update " + name ;}

    /**
     * query che setta i valori
     * @param info valori aggiornati
     */
    public void set(String info){ sql = sql + " set " + info;}

    /**
     * query di eliminazione
     */
    public void delete(){
        sql = "delete from "+name;
    }

    /**
     * query di eliminazione
     */
    public void delete2(){
        sql = "delete "+name+" from "+name;
    }

    /**
     * query di selezione
     */
    public void select(){
        sql="SELECT * FROM " + name;
    }

    /**
     * query di selezione
     * @param campo campo della tabella da estrarre
     */
    public void select(String campo) { sql="SELECT " +campo+ " FROM " + name;}

    /**
     * implementa la clausola where di una query
     * @param clausola condizione da usare
     */
    public void where(String clausola){
        sql=sql + " WHERE " + clausola;
    }

    /**
     * implementa la clausola order by
     * @param clausola attributo secondo il quale ordinare
     */
    public void order(String clausola){
        sql = sql + "ORDER BY " + clausola + " asc";
    }

    /**
     * conta il numero di tuple estratte
     * @param tuple tuple estratte dalla query
     * @return numero di tuple
     */
    public int count(List<Map<String, Object>> tuple){
        return tuple.size();
    }

    /**
     * implementa la clausola group by
     * @param campo attributo secondo il quale raggruppare
     */
    public void groupby(String campo){
        sql = sql + "GROUP BY " + campo;
    }

    /**
     * implementa l'inner join tra due tabelle
     * @param tabella tabella con la quale fare il join
     * @param uguaglianza condizione per il join
     */
    public void innerjoin(String tabella, String uguaglianza){
        sql = sql + " INNER JOIN " + tabella + " ON " + uguaglianza;
    }

    /**
     * implementa un doppio inner join
     * @param tabella1 prima tabella con la quale fare il join
     * @param uguaglianza1 condizione per il join con la prima tabella
     * @param tabella2 seconda tabella con la quale fare il join
     * @param uguaglianza2 condizione per il join con la seconda tabella
     */
    public void doubleinnerjoin(String tabella1, String uguaglianza1, String tabella2, String uguaglianza2){
        sql = sql + " INNER JOIN " + tabella1 + " ON " + uguaglianza1;
        sql = sql + " INNER JOIN " + tabella2 + " ON " + uguaglianza2;
    }

    /**
     * Esegue le query di inserimento, modifica e cancellazione
     * @param db connessione al database
     * @return booleano per sapere se la query è stata eseguita correttamente
     */
    public boolean execute(Connection db){
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
        return check;
    }

    /**
     * Esegue la query di inserimento restituendo l'id autoincrementale che il database ha generato per la nuova riga
     * @param db connessione al database
     * @return l'id generato
     */
    public int executeForKey(Connection db){
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
        return generated_key;
    }

    /**
     * Esegue le query di selezione
     * @param db connessione al database
     * @return la lista delle tuple estratte
     */
    public List<Map<String, Object>> fetch(Connection db){

        ResultSet risultato= null;
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
        return lista;
    }

    /**
     * Trasforma un oggetto di tipo result set in una lista
     * in cui ogni elemento rappresenta una riga, ed è formato da un insieme di coppie chiave (nome colonna) valore
     * @param rs result set, restituito da una query sql di selezione
     * @return lista in cui ogni elemento rappresenta una riga
     * @throws SQLException  
     */
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
