<!DOCTYPE html>
<html>
<head>

    <link rel="stylesheet" href="../../css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/index.js"></script>

</head>
<body>
<%@ page import = "java.util.*" %>
    <div id="page">
        <div id="inserimentopianoblock">
            <img height="500" width="500" id="target"/>
            <form id="newpianoform" method="post">
                <input id="imageselect" type="file">
                <input id="csvbeacons" type="file">
                <input id="csvtronchi" type="file">
                <input id="name" type="text">
                <input id="aggiungipiano" type="button" value="Aggiungi Piano">
            </form>
        </div>
        <div>
            <input id="lanciaemergenza" type="button" value="LanciaEmergenza">
            <input id="backtonormal" type="button"  value="Backtonormal">
        </div>

        <ul>
            <%
            Map<String, Integer> map = (Map)request.getAttribute("model");
            for(Map.Entry<String, Integer> entry : map.entrySet()) {
                out.println("<li><a href=\"http://localhost:8080/gestionemappe/db/piano/"+ entry.getKey() +"\">"+entry.getKey()+ " " + entry.getValue() +"</a></li>");
            } %>
        </ul>
    </div>
</body>
</html>