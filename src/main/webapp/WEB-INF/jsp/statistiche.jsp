<!DOCTYPE html>
<html>
<head>

    <link rel="stylesheet" href="../../css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/index.js"></script>

</head>
<%@ page import="java.util.Map" %>
<%@ page import="org.teamids.gestionemappe.model.entity.BeaconEntity" %>
<body>
    <ul>
        <%
            Map<BeaconEntity, Integer> map = (Map)request.getAttribute("model");
            for(Map.Entry<BeaconEntity, Integer> entry : map.entrySet()) {
                if(entry.getKey().isIs_puntodiraccolta()) {
                    out.println("<li>BeaconId: " + entry.getKey().getId() + " Punto di Raccolta: True NumeroPersone: " + entry.getValue() + "</li>");
                }
            }

            out.println("<br>");

            for(Map.Entry<BeaconEntity, Integer> entry : map.entrySet()) {
                if(!entry.getKey().isIs_puntodiraccolta()) {
                    out.println("<li>BeaconId: " + entry.getKey().getId() + " Punto di Raccolta: False NumeroPersone: " + entry.getValue() + "</li>");
                }
            }
        %>
    </ul>
</body>
</html>
