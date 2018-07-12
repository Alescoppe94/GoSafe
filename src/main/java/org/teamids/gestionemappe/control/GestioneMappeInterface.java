package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.entity.NotificaEntity;
import org.teamids.gestionemappe.model.entity.PercorsoEntity;

import java.util.Observable;
import java.util.Observer;

public interface GestioneMappeInterface extends Observer {
    void lanciaEmergenza();

    PercorsoEntity calcoloPercorsoNoEmergenza(String beaconPart, String beaconArr);

    NotificaEntity visualizzaPercorso(int utenteId, String beaconPart);

    @Override
    void update(Observable o, Object arg);

    void backToNormalMode();
}
