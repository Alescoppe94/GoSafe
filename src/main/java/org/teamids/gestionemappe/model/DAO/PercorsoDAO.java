package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Percorso;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PercorsoDAO {

    protected static Percorso tabella = new Percorso();

    public PercorsoDAO() {
    }

    public static int insertPercorso(BeaconEntity beaconPartenza){

        String dati= "0";
        dati=dati+",'"+ beaconPartenza.getId() +"'";
        tabella.insert(dati);
        return tabella.executeForKey();

    }

    public static PercorsoEntity getPercorsoByBeaconId(int beaconId){ //TODO: sistemare direzione tappe
        tabella.select();
        tabella.where("beaconPartenzaId = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        try {
            int percorsoId = Integer.parseInt(rs.get(0).get("id").toString());
            LinkedList<TappaEntity> tappe = TappaDAO.getTappeByPercorsoId(percorsoId);
            PercorsoEntity percorso = new PercorsoEntity(percorsoId, tappe, BeaconDAO.getBeaconById(beaconId));
            return percorso;
        } catch (Exception e){
            return null;
        }
    }

    public static boolean findPercorsoByBeaconId(int beaconPart) {
        boolean success = false;
        tabella.select();
        tabella.where("beaconPartenzaId='" + beaconPart + "'");
        if(tabella.fetch().size()==1)
            success = true;
        else
            success=false;
        return success;
    }

    public static void removeAllPercorsi() {
        tabella.delete();
        tabella.execute();
    }
}
