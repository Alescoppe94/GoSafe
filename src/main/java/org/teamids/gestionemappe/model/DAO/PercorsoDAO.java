package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Percorso;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PercorsoDAO {

    protected Percorso tabella;

    public PercorsoDAO() {
        this.tabella = new Percorso();
    }

    public int insertPercorso(BeaconEntity beaconPartenza){

        String dati= "0";
        dati=dati+",'"+ beaconPartenza.getId() +"'";
        tabella.insert(dati);
        return tabella.executeForKey();

    }

    public PercorsoEntity getPercorsoByBeaconId(String beaconId){ //TODO: sistemare direzione tappe
        tabella.select();
        tabella.where("beaconPartenzaId = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        PercorsoEntity percorso = null;
        BeaconDAO beaconDAO = new BeaconDAO();
        TappaDAO tappaDAO = new TappaDAO();
        if(rs.size() != 0) {
            try {
                int percorsoId = Integer.parseInt(rs.get(0).get("id").toString());
                LinkedList<TappaEntity> tappe = tappaDAO.getTappeByPercorsoId(percorsoId);
                percorso = new PercorsoEntity(percorsoId, tappe, beaconDAO.getBeaconById(beaconId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return percorso;
    }

    public boolean findPercorsoByBeaconId(String beaconPart) {
        boolean success = false;
        tabella.select();
        tabella.where("beaconPartenzaId='" + beaconPart + "'");
        if(tabella.fetch().size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    public void removeAllPercorsi() {
        tabella.delete();
        tabella.execute();
    }
}
