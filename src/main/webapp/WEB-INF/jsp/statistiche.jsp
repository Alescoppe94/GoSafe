<!DOCTYPE html>
<html>
<head>

    <link rel="stylesheet" href="/../../css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/index.js"></script>

</head>
<%@ page import="java.util.Map" %>
<%@ page import="org.teamids.gestionemappe.model.entity.BeaconEntity" %>
<body style="display: block;">
    <div style="background-color:#366666">
        <h1 style="color: #FFFFFF; padding: 20px 0px 20px 20px; margin-top: 0px; margin-bottom: 0px">Visualizza le Persone nell'Edificio</h1>
    </div>
    <div>
        <div style="padding: 30px 0px 10px 40px">
            <h3>Elenco di beacons di raccolta a cui sono collegate delle Persone: </h3>
        </div>
        <ul style="padding-left: 70px">
            <%
                Map<BeaconEntity, Integer> map = (Map)request.getAttribute("model");
                for(Map.Entry<BeaconEntity, Integer> entry : map.entrySet()) {
                    if(entry.getKey().isIs_puntodiraccolta()) {
                        out.println("<li><strong>Beacon:</strong> " + entry.getKey().getId() + ", <strong>Numero di Persone:</strong> " + entry.getValue() + "</li>");
                    }
                }

            %>
        </ul>
        <div style="padding-bottom: 10px; padding-left: 40px; padding-top: 30px;">
            <h3>Elenco di beacons all'interno dell'Edificio a cui sono collegate delle Persone:</h3>
        </div>
        <ul style="padding-left: 70px">
            <%
                for(Map.Entry<BeaconEntity, Integer> entry : map.entrySet()) {
                    if(!entry.getKey().isIs_puntodiraccolta()) {
                        out.println("<li><strong>Beacon:</strong> " + entry.getKey().getId() + ", <strong>Numero di Persone:</strong> " + entry.getValue() + "</li>");
                    }
                }
            %>
        </ul>
    </div>
</body>
</html>
