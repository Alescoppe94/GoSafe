<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/pesi.js"></script>
</head>
<body>

    <ul>
        <%
            Map<Integer, Map<String, Float>> map = (Map)request.getAttribute("model");
            out.println("<form action=\"http://localhost:8080/gestionemappe/db/modificaPesi\" method=\"post\">");
            for(Map.Entry<Integer, Map<String, Float>> entry : map.entrySet()) {

                Map.Entry<String, Float> subentry = entry.getValue().entrySet().iterator().next();
                out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey()+"\" type=\"text\" value=\"" + subentry.getValue() + "\"><button id=\"peso-"+ entry.getKey() +"\">Elimina</button>");

            }
            out.println("<input value=\"Submit\" type=\"submit\">");
            out.println("</form>");
        %>
    </ul>

    <form action="http://localhost:8080/gestionemappe/db/aggiungiPeso" method="post">

        Nome:<input name="nome" type="text">
        Valore:<input name="coefficiente" type="text">
        <input type="submit" value="AggiungiPeso">


    </form>
</body>
</html>
