package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.TroncoEntity;
import org.teamids.gestionemappe.model.entity.UtenteEntity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

public interface UtenteDAOInterface {
    void insertUser(UtenteEntity utente, Connection db);

    UtenteEntity getUserByUsername(String username, Connection db);

    boolean isAutenticato(String idsessione, Connection db);

    boolean findUserByUsername(String user, Connection db);

    void updatePositionInEmergency(int id, String beaconId, TroncoEntity tronco, Connection db);

    void updateInfoUtente(int id, Map<String, Object> campoutente, Connection db);

    void logout(String username, Connection db);

    ArrayList<String> getBeaconsIdAttivi(Connection db);

    ArrayList<String> getTokensAttivi(Connection db);

    boolean existUtenteInPericolo(Connection db);

    int countUsersPerTronco(ArrayList<Integer> percorsiId, Connection db);

    int countUsersPerBeacon(String beaconId, Connection db);

    boolean isUsernameIdPresent(String username, int id, Connection db);
}
