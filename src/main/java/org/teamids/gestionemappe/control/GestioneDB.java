package org.teamids.gestionemappe.control;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestioneDB {

    public GestioneDB(){

    }
    
    private boolean verificaAggiornamentiDB(){
        return true;
    }

    public void aggiornaDB(Object object){
    }
}
