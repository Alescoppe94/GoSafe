package org.teamids.gestionemappe.model.entity;

import java.sql.Connection;

public interface TroncoEntityInterface extends Comparable<TroncoEntity> {
    float calcolaCosto(Connection db);

    @Override
    int compareTo(TroncoEntity o);
}
