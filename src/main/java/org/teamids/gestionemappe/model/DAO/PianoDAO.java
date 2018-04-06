package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Piano;
import org.teamids.gestionemappe.model.entity.PianoEntity;

import java.util.List;
import java.util.Map;

public class PianoDAO {

    protected Piano tabella;

    public PianoDAO() {

         this.tabella = new Piano();

    }

    public PianoEntity getPianoById(int idpiano){
        tabella.select();
        tabella.where("id = '" + idpiano + "'" );
        List<Map<String, Object>> rs = tabella.fetch();
        PianoEntity piano = new PianoEntity();
        piano.setId(Integer.parseInt(rs.get(0).get("id").toString()));
        piano.setImmagine(rs.get(0).get("immagine").toString());
        piano.setPiano(Integer.parseInt(rs.get(0).get("piano").toString()));
        return piano;
    }
}
