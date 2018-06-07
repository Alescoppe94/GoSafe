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
        <div>
            <div id="inserimentopianoblock">
                <div><h1>Form Inserimento Piano</h1></div>
                <div>
                    <div id="parenttarget">
                        <img id="target"/>
                    </div>
                    <div>
                        <form id="newpianoform" method="post">
                            <div>
                            <label>Immagine Piano: </label><input id="imageselect" type="file">
                            <label>Csv Beacons: </label><input id="csvbeacons" type="file">
                            <label>Csv Tronchi: </label><input id="csvtronchi" type="file">
                            <label>Numero Piano: </label><input id="name" type="text">
                            <input id="aggiungipiano" type="button" value="Aggiungi Piano">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div id="piani-container">
                <div>
                    <h1>Elenco Piani</h1>
                </div>
                <div>
                    <ul>
                        <%
                            Map<String, Integer> map = (Map)request.getAttribute("model");
                            for(Map.Entry<String, Integer> entry : map.entrySet()) {
                                out.println("<li><a href=\"http://localhost:8080/gestionemappe/db/piano/"+ entry.getKey() +"\">"+entry.getKey()+ " " + entry.getValue() +"</a></li><button id=\"piano-"+ entry.getKey() +"\" name=\""+ entry.getKey() +"-"+ entry.getValue() +"\" type=\"button\">Elimina Piano</button>");
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>
        <div>
            <div>
                <form action="http://localhost:8080/gestionemappe/db/mostraPesi" method="get">
                    <input id="aggiungipesi" type="submit" value="aggiungi Pesi">
                </form>
            </div>
            <div>
                <input id="lanciaemergenza" type="button" value="LanciaEmergenza">
                <input id="backtonormal" type="button"  value="Backtonormal">
                <a href="http://localhost:8080/gestionemappe/db/visualizzastatistiche"><input id="visualizzastatistiche" type="button"  value="VisualizzaStatistiche"></a>
            </div>
        </div>
    </div>
</body>
</html>