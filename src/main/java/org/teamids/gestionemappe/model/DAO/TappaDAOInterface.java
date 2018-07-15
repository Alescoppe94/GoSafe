package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TappaEntity;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Interfaccia della classe TappaDAO,
 * include l'elenco delle funzionalità che la classe TappaDAO implementa
 */
public interface TappaDAOInterface {
    LinkedList<TappaEntity> getTappeByPercorsoId(int percorsoId, Connection db);

    void removeAllTappe(Connection db);

    void aggiornaTappe(int percorsoId, LinkedList<TappaEntity> tappeOttime, Connection db);

    void creaPercorsoConTappe(BeaconEntity partenza, LinkedList<TappaEntity> tappeOttime, Connection db);
}
