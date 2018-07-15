package org.teamids.gestionemappe.model.DAO;

import org.teamids.gestionemappe.model.entity.NotificaEntity;

import java.sql.Connection;

/**
 * Interfaccia della classe NotificaDAO,
 * include l'elenco delle funzionalità che la classe NotificaDAO implementa
 */
public interface NotificaDAOInterface {
    void insertNotifica(NotificaEntity notifica, Connection db);
}
