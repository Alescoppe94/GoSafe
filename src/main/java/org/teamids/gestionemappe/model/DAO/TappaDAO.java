package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Tappa;
import org.teamids.gestionemappe.model.entity.TappaEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

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
        dati=dati+",'"+ tappa.getTronco().getId()+"'"; //vedere se va bene
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
            TroncoEntity tronco = troncoDAO.getTroncoById(rs.get(i).get("troncoId").toString());
            TappaEntity tappa = new TappaEntity(
                    Integer.parseInt(rs.get(i).get("id").toString()),
                    Integer.parseInt(rs.get(i).get("percorsoId").toString()),
                    tronco
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
}
