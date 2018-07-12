package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Percorso;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PercorsoDAO implements PercorsoDAOInterface {

    protected Percorso tabella;

    public PercorsoDAO() {
        this.tabella = new Percorso();
    }

    @Override
    public PercorsoEntity getPercorsoByBeaconId(String beaconId, Connection db){
        tabella.select();
        tabella.where("beaconPartenzaId = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch(db);
        PercorsoEntity percorso = null;
        BeaconDAO beaconDAO = new BeaconDAO();
        TappaDAO tappaDAO = new TappaDAO();
        if(rs.size() != 0) {
            try {
                int percorsoId = Integer.parseInt(rs.get(0).get("id").toString());
                LinkedList<TappaEntity> tappe = tappaDAO.getTappeByPercorsoId(percorsoId, db);
                percorso = new PercorsoEntity(percorsoId, tappe, beaconDAO.getBeaconById(beaconId, db));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return percorso;
    }

    @Override
    public boolean findPercorsoByBeaconId(String beaconPart, Connection db) {
        boolean success = false;
        tabella.select();
        tabella.where("beaconPartenzaId='" + beaconPart + "'");
        if(tabella.fetch(db).size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    @Override
    public void removeAllPercorsi(Connection db) {
        tabella.delete();
        tabella.execute(db);
    }
}
