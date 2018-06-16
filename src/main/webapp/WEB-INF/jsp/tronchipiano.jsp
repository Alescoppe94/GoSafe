<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>

<%@ page import = "java.util.*" %>
<%@ page import="org.teamids.gestionemappe.model.entity.TroncoEntity" %>

<div style="background-color:#366666">
    <h1 style="color: #FFFFFF">Gestisci i valori dei tronchi del piano</h1>
</div>
<ul>
    <%
        TreeMap<TroncoEntity, HashMap<String, Float>> map = (TreeMap)request.getAttribute("model");
        out.println("<form action=\"http://localhost:8080/gestionemappe/db/aggiornapesitronco\" method=\"post\">");
        for(Map.Entry<TroncoEntity, HashMap<String, Float>> entry : map.entrySet()) {
            out.println("<li style=\"margin-top:20px\"> idTronco: "+entry.getKey().getId()+" BeaconA: "+entry.getKey().getBeaconEstremi().get(0).getId() +" BeaconB: "+ entry.getKey().getBeaconEstremi().get(1).getId() +" Area: " + entry.getKey().getArea() +" mq. </li>");
            for(Map.Entry<String, Float> subentry : entry.getValue().entrySet()){

                if(subentry.getValue() != null) {
                    out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey().getId()+"\" type=\"text\" value=\"" + subentry.getValue() + "\">");
                }else{
                    out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey().getId()+"\" type=\"text\">");
                }
            }
        }
        out.println("<input value=\"Submit\" type=\"submit\" class=\"coolbutton\">");
        out.println("</form>");
    %>
</ul>

</body>


</html>
