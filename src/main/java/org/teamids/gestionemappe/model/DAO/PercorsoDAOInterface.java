package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.PercorsoEntity;

import java.sql.Connection;

public interface PercorsoDAOInterface {
    PercorsoEntity getPercorsoByBeaconId(String beaconId, Connection db);

    boolean findPercorsoByBeaconId(String beaconPart, Connection db);

    void removeAllPercorsi(Connection db);
}
