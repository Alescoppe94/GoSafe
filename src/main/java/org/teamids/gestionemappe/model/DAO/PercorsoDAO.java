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

    public static PercorsoEntity getPercorsoByBeaconId(int beaconId){
        tabella.select();
        tabella.where("beaconPartenzaId = '" + beaconId + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        int percorsoId = Integer.parseInt(rs.get(0).get("id").toString());
        LinkedList<TappaEntity> tappe = TappaDAO.getTappeByPercorsoId(percorsoId);
        PercorsoEntity percorso = new PercorsoEntity(percorsoId, tappe, BeaconDAO.getBeaconById(beaconId));
        return percorso;
    }
}
