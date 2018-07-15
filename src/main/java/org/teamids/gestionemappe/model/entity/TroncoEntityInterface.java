package org.teamids.gestionemappe.model.entity;

import java.sql.Connection;

/**
 * Interfaccia della classe TroncoEntity,
 * include l'elenco delle funzionalità che la classe TroncoEntity implementa
 */
public interface TroncoEntityInterface extends Comparable<TroncoEntity> {

    float calcolaCosto(Connection db);

    @Override
    int compareTo(TroncoEntity o);
}
