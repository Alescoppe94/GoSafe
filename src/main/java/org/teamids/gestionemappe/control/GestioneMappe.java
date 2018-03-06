package org.teamids.gestionemappe.control;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("gestionemappe")
public class GestioneMappe extends Application {

    public GestioneMappe() {

    }

    //2 parametro
    public String calcoloPercorso(int beaconPart, int beaconArr){
        //metodo: calcoloDijkstra
        return "Ciao"+beaconArr+beaconPart;
    }

    //2 parametri


    private void calcoloDijkstra(){

    }

    public void generaNotifica(){

    }

    public void lanciaEmergenza(){

    }


}
