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

    //private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/gosafe";

    Connection conn;

    public GestioneDB() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(JDBC_URL, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(this.conn != null) {
            System.out.println("connected to database");
           // createTable();
        }

    }

    public void createTable(){
        try {
            conn.createStatement().execute("CREATE TABLE Utente(Nome VARCHAR(5))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean verificaAggiornamentiDB(){
        return true;
    }

    public void aggiornaDB(Object object){
    }

    public void tokenUpdater(String token){

        /*FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("E:/Download/gosafe-16ad0-firebase-adminsdk-uf0l7-cadd1e0ad5.json");


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://gosafe-16ad0.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        HttpPost post = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClientBuilder.create().build();
            post = new HttpPost("https://fcm.googleapis.com/fcm/send");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", "key=AAAALbEI9gs:APA91bHePfbWk8PS7wDiDG1maDYK5CezeMNL-mGQ3-Lv5FjiZ74Sv78RjbGsujVEee9htvriyv0wEy23biy80gJPnhgvlSpln1eEF0smn3IeNgOyi9ckEDpEAOzLFxlzSzZfNvOJZXFo"); // campo chiave server su impostazioni>cloudmessaging
            //System.out.println(getAccessToken().toString());

            //URL url = null;
            //try {
            //  url = new URL("https://fcm.googleapis.com/fcm/send");
            //HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //  httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            //httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
            //}catch(Exception e){
            //}


            JsonObject message = new JsonObject();
            message.addProperty("to", token); //è il token del dispositivo
            message.addProperty("priority", "high");

            JsonObject notification = new JsonObject();
            notification.addProperty("title", "Java");
            notification.addProperty("body", "Ciao bomber");

            message.add("notification", notification);

            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            System.out.println(response);
            System.out.println(message);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
