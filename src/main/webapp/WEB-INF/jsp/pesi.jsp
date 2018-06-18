<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/../../css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/pesi.js"></script>
</head>
<body style="display: block;">

    <div style="background-color:#366666">
        <h1 style="color: #FFFFFF; padding: 20px 0px 20px 20px; margin-top: 0px; margin-bottom: 0px">Gestisci i valori dei Pesi</h1>
    </div>
    <div style="padding-top: 20px">
        <div style="padding-bottom: 10px; padding-left: 40px">
            <h3>Modifica i valori dei coefficienti dei pesi già presenti</h3>
        </div>
        <ul>
            <%
                Map<Integer, Map<String, Float>> map = (Map)request.getAttribute("model");
                out.println("<form action=\"http://localhost:8080/gestionemappe/db/modificaPesi\" method=\"post\">");
                for(Map.Entry<Integer, Map<String, Float>> entry : map.entrySet()) {

                    Map.Entry<String, Float> subentry = entry.getValue().entrySet().iterator().next();
                    out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey()+"\" type=\"text\" value=\"" + subentry.getValue() + "\"><button style=\"margin-right:40px; margin-left:10px;\" class=\"coolbutton\" id=\"peso-"+ entry.getKey() +"\">Elimina</button>");

                }
                out.println("<br><br><input value=\"Submit\" type=\"submit\" class=\"coolbutton\" style=\"padding: 10px 20px 10px 20px;\">");
                out.println("</form>");
            %>
        </ul>
    </div>

    <div style="padding-left: 40px; padding-top: 40px;">
        <h3>Inserisci un nuovo peso</h3>
    </div>
    <div style="padding-left: 40px; padding-top: 20px;">
        <form action="http://localhost:8080/gestionemappe/db/aggiungiPeso" method="post">
            Nome:<input name="nome" type="text">
            Valore:<input name="coefficiente" type="text"><br><br>
            <input type="submit" value="AggiungiPeso" class="coolbutton" style="padding: 10px 20px 10px 20px;">
        </form>
    </div>
</body>
</html>
