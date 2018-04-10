package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.PesiTronco;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesiTroncoDAO {

    protected PesiTronco tabella;

    public PesiTroncoDAO() {
        this.tabella = new PesiTronco();
    }

    public HashMap<Float,Float> getPesiTronco(int troncoId) {
        tabella.select("valore,coefficiente");
        tabella.innerjoin("peso", "peso.id = pesoId");
        tabella.where("troncoId = '" + troncoId +"'");
        List<Map<String, Object>> rs = tabella.fetch();
        HashMap<Float, Float> coeffVal = new HashMap<>();
        for (int i = 0; i<rs.size(); i++) {
            coeffVal.put(Float.parseFloat(rs.get(i).get("coefficiente").toString()), Float.parseFloat(rs.get(i).get("valore").toString()));
        }
        return coeffVal;
    }
}
