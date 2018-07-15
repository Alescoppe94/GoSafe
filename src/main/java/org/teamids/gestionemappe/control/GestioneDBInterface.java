package org.teamids.gestionemappe.control;

import org.teamids.gestionemappe.model.entity.BeaconEntity;
import org.teamids.gestionemappe.model.entity.TroncoEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Interfaccia della classe GestioneDB, 
 * include l'elenco delle funzionalità che la classe GestioneDB implementa
 */
public interface GestioneDBInterface {
    Map<String, Integer> getAllPiani();

    Map<BeaconEntity, Integer> getPeoplePerBeacon();

    HashMap<TroncoEntity, HashMap<String, Float>> getTronchiPiano(int pianoId);

    void aggiornaPesiTronco(String peso, int troncoId, float valore);

    String aggiornaDB(Timestamp timestamp_client);

    ArrayList<String> aggiungiPiano(String path, com.google.gson.JsonObject jsonRequest);

    void eliminaPiano(int idPiano);

    void aggiornaPesi(int id, Float valore);

    void inserisciPeso(ArrayList<String> peso);

    String downloadDb();

    void eliminapeso(int idPeso);

    Map<Integer, Map<String,Float>> mostraPesi();
}
