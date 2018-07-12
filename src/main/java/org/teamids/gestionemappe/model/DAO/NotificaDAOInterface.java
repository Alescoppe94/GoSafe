package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.NotificaEntity;

import java.sql.Connection;

public interface NotificaDAOInterface {
    void insertNotifica(NotificaEntity notifica, Connection db);
}
