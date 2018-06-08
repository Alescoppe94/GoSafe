package org.teamids.gestionemappe.control;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.json.Json;

public class Communication {

    public Communication() {
    }

    public void inviaNotifica(String token){

        HttpPost post = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClientBuilder.create().build();
            post = new HttpPost("https://fcm.googleapis.com/fcm/send");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", "key=AAAALbEI9gs:APA91bHePfbWk8PS7wDiDG1maDYK5CezeMNL-mGQ3-Lv5FjiZ74Sv78RjbGsujVEee9htvriyv0wEy23biy80gJPnhgvlSpln1eEF0smn3IeNgOyi9ckEDpEAOzLFxlzSzZfNvOJZXFo"); // campo chiave server su impostazioni>cloudmessaging

            JsonObject message = new JsonObject();
            message.addProperty("to", token);
            message.addProperty("priority", "high");

            JsonObject data = new JsonObject();
            data.addProperty("title", "Incendio nell'edificio!");
            data.addProperty("body", "Segui il percorso verso la via di fuga più vicina");

            message.add("data", data);


            /*JsonObject notification = new JsonObject();
            notification.addProperty("title", "Incendio nell'edificio!");
            notification.addProperty("body", "Segui il percorso verso la via di fuga più vicina");

            message.add("notification", notification);*/

            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            System.out.println(response);
            System.out.println(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
