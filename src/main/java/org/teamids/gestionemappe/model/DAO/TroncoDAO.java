package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.DbTable.Tronco;
import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class TroncoDAO {

    protected Tronco tabella;

    public TroncoDAO() {
        tabella= new Tronco();
    }

    public Set<TroncoEntity> getAllTronchi(){
        Set<TroncoEntity> allTronchiEdificio = new HashSet<>();
        tabella.select();
        ResultSet rs = tabella.fetch();
        BeaconDAO beaconDAO = new BeaconDAO();
        try{
            while (rs.next()){
                Set<BeaconEntity> estremiTronco = new HashSet<>();
                estremiTronco.add(beaconDAO.getBeaconById(rs.getInt("beaconAId")));
                estremiTronco.add(beaconDAO.getBeaconById(rs.getInt("beaconBId")));
                TroncoEntity tronco = new TroncoEntity(
                        rs.getInt("id"),
                        rs.getBoolean("agibile"),
                        rs.getFloat("costo"),
                        estremiTronco,
                        rs.getFloat("lunghezza")
                );
                allTronchiEdificio.add(tronco);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            allTronchiEdificio = null;
        }
        return allTronchiEdificio;
    }

    public TroncoEntity getTroncoByBeacons(BeaconEntity beaconA, BeaconEntity beaconB){

        tabella.select();
        tabella.where("beaconAId = '" + beaconA.getId() + "' and beaconBId = '" + beaconB.getId() + "'" );
        ResultSet rs = tabella.fetch();
        try{
            rs.next();
            Set<BeaconEntity> estremiTronco = new HashSet<>();
            estremiTronco.add(beaconB);
            estremiTronco.add(beaconA);
            TroncoEntity tronco = new TroncoEntity(
                    rs.getInt("id"),
                    rs.getBoolean("agibile"),
                    rs.getFloat("costo"),
                    estremiTronco,
                    rs.getFloat("lunghezza")
            );
            return tronco;
        }
        catch (Exception e){
            System.out.println("Errore" + e);
            return null;
        }

    }

}
