package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DbTable.Tappa;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TappaDAO {

    protected Tappa tabella;

    public TappaDAO() {
        this.tabella = new Tappa();
    }

    public void insertTappa(TappaEntity tappa){
        String dati= "0";
        dati=dati+",'"+ tappa.getPercorsoId()+"'";
        dati=dati+",'"+ tappa.getTronco().getId()+"'";
        dati=dati+","+ tappa.isDirezione()+"";//vedere se va bene
        tabella.insert(dati);
        int id_tappa = tabella.executeForKey();
        tappa.setId(id_tappa);

    }

    public LinkedList<TappaEntity> getTappeByPercorsoId(int percorsoId) {
        LinkedList<TappaEntity> tappe = new LinkedList<>();
        tabella.select();
        tabella.where("percorsoId = '" + percorsoId + "'");
        List<Map<String, Object>> rs = tabella.fetch();
        TroncoDAO troncoDAO = new TroncoDAO();
        for (int i = 0; i<rs.size(); i++) {
            boolean direzione = Boolean.parseBoolean(rs.get(i).get("direzione").toString());
            TroncoEntity tronco = troncoDAO.getTroncoById(rs.get(i).get("troncoId").toString(), direzione);
            TappaEntity tappa = new TappaEntity(
                    Integer.parseInt(rs.get(i).get("id").toString()),
                    Integer.parseInt(rs.get(i).get("percorsoId").toString()),
                    tronco,
                    direzione
            );
            tappe.add(tappa);
        }
        return tappe;
    }

    public void removeTappeByPercorsoId(int percorsoId) {
        tabella.delete();
        tabella.where("percorsoId = '" + percorsoId + "'");
        tabella.execute();
    }

    public void removeAllTappe() {
        tabella.delete();
        tabella.execute();
    }

    public void aggiornaTappe(int percorsoId, LinkedList<TappaEntity> tappeOttime) {
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        try {
            db.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String delete = "DELETE FROM tappa WHERE percorsoId =" + percorsoId + ";";
        PreparedStatement query = null;
        try {
            query = db.prepareStatement(delete);
            query.executeUpdate();

            for(TappaEntity tappa: tappeOttime){
                String insert = "INSERT INTO tappa VALUES(0,"+tappa.getPercorsoId()+","+tappa.getTronco().getId()+","+ tappa.isDirezione() +");";
                query = db.prepareStatement(insert);
                query.executeUpdate();
            }
            db.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (db != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    db.rollback();
                } catch(SQLException excep) {
                    e.printStackTrace();
                }
            }
        }
        try {
            db.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.disconnect();
    }

    public void creaPercorsoConTappe(BeaconEntity partenza, LinkedList<TappaEntity> tappeOttime) {
        ConnectorHelpers connector= new ConnectorHelpers();
        Connection db = connector.connect();
        try {
            db.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement query = null;
        String insertPercorso = "INSERT INTO percorso VALUES(0,"+ partenza.getId() + ");";
        String selectPercorsoId = "SELECT @ID:=id FROM percorso WHERE beaconPartenzaId="+ partenza.getId()+";";
        try {
            query = db.prepareStatement(insertPercorso);
            query.executeUpdate();
            query = db.prepareStatement(selectPercorsoId);
            query.executeQuery();
            for(TappaEntity tappa: tappeOttime){
                String insertTappa = "INSERT INTO tappa VALUES(0,@ID,"+tappa.getTronco().getId()+","+ tappa.isDirezione() +");";
                query = db.prepareStatement(insertTappa);
                query.executeUpdate();
            }
            db.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (db != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    db.rollback();
                } catch(SQLException excep) {
                    e.printStackTrace();
                }
            }
        }
        try {
            db.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.disconnect();
    }
}
