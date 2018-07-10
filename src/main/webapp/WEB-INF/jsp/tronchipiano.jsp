<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="/../../css/style.css">
</head>
<body style="display: block;">

<%@ page import = "java.util.*" %>
<%@ page import="org.teamids.gestionemappe.model.entity.TroncoEntity" %>

<div style="background-color:#366666">
    <h1 style="color: #FFFFFF; padding: 20px 0px 20px 20px; margin-top: 0px; margin-bottom: 0px">Gestisci i valori dei tronchi del piano</h1>
</div>
<div style="padding-bottom: 10px; padding-left: 40px">
    <h3>Modifica i valori dei pesi dei tronchi</h3>
</div>
<div>
    <ul>
        <%
            TreeMap<TroncoEntity, HashMap<String, Float>> map = (TreeMap)request.getAttribute("model");
            out.println("<form action=\"http://localhost:8080/gestionemappe/db/aggiornapesitronco\" method=\"post\">");
            for(Map.Entry<TroncoEntity, HashMap<String, Float>> entry : map.entrySet()) {
                out.println("<li style=\"margin-top:20px\"><strong>Id Tronco:</strong> "+entry.getKey().getId()+", BeaconA: "+entry.getKey().getBeaconEstremi().get(0).getId() +", BeaconB: "+ entry.getKey().getBeaconEstremi().get(1).getId() +", Area: " + entry.getKey().getArea() +" mq. </li><br>");
                for(Map.Entry<String, Float> subentry : entry.getValue().entrySet()){

                    if(subentry.getValue() != null) {
                        out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey().getId()+"\" type=\"text\" value=\"" + subentry.getValue() + "\" style=\"margin-right:30px;\">");
                    }else{
                        out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey().getId()+"\" type=\"text\">");
                    }
                }
            }
            out.println("<br><input value=\"Submit\" type=\"submit\" class=\"coolbutton\" style=\"margin-top:30px; margin-bottom:30px;padding: 10px 20px 10px 20px;\" style=\"margin-right:30px;\">");
            out.println("</form>");
        %>
    </ul>
</div>

</body>


</html>
