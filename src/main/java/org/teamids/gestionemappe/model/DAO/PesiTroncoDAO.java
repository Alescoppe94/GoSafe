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

    public void updateValorePeso(int troncoId, String peso, float los){
        String dati= "valore = " + los;
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set(dati);
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        tabella.execute();
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

    public Float geValoreByPesoId(int troncoId, String peso) {
        tabella.select("valore");
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.where("troncoId = '" + troncoId +"' AND peso.nome = '" + peso + "'");
        List<Map<String, Object>> rs = tabella.fetch();
         return Float.parseFloat(rs.get(0).get("valore").toString());
    }

    public void losToDefault() {
        tabella.update();
        tabella.innerjoin("peso", "pesoId = peso.id");
        tabella.set("valore = 0");
        tabella.where("peso.nome = 'los'");
        tabella.execute();
    }
}
