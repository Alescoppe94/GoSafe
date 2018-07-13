package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.entity.UtenteEntity;

/**
 * Interfaccia della classe GestioneUtente,
 * include l'elenco delle funzionalità che la classe GestioneUtente implementa
 */
public interface GestioneUtenteInterface {
    String loginUtente(UtenteEntity utente);

    String registrazioneUtente(UtenteEntity utente);

    String updateUtente(UtenteEntity utente);

    String logoutUtente(UtenteEntity utente);

    void updateUserPosition(UtenteEntity utente);
}
