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
                <div style="background-color: #366666; color: #FFFFFF"><h1>Inserisci un nuovo Piano</h1></div>
                <div>

                    <div style="margin-top: 30px">
                        <form id="newpianoform" method="post">
                            <div>
                            <label>Immagine Piano: </label><input id="imageselect" type="file"><br>
                            <label>Csv Beacons: </label><input id="csvbeacons" type="file"><br>
                            <label>Csv Tronchi: </label><input id="csvtronchi" type="file"><br>
                            <label>Numero Piano: </label><input id="name" type="text"><br>
                            <input id="aggiungipiano" type="button" value="Aggiungi Piano" class="coolbutton">
                            </div>
                        </form>
                    </div>
                    <div id="parenttarget" style="padding-top: 30px; margin-bottom: 30px;">
                        <img id="target"/>
                    </div>
                </div>
            </div>
            <div id="piani-container">
                <div style="background-color: #366666">
                    <h1 style="margin-left: 30px; color: #FFFFFF">Elenco Piani</h1>
                </div>
                <div style="margin-top: 16px">
                    <ul>
                        <%
                            ArrayList<Integer> piani = new ArrayList<>();
                            Map<String, Integer> map = (Map)request.getAttribute("model");
                            for(Map.Entry<String, Integer> entry : map.entrySet()) {
                                out.println("<li> Numero piano: "+ entry.getValue() +"</li><a href=\"http://localhost:8080/gestionemappe/db/piano/"+entry.getKey()+"\" ><button type=\"button\" class=\"coolbutton\">Modifica Piano</button></a><button id=\"piano-"+ entry.getKey() +"\" name=\""+ entry.getKey() +"-"+ entry.getValue() +"\" type=\"button\" class=\"coolbutton\">Elimina Piano</button>");
                                piani.add(entry.getValue());
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>
        <div>
            <div style="display:block;margin: 20px 0px 50px 20px">
                <div>
                    <label>Visualizza e modifica i pesi utilizzati per il calcolo del costo dei tronchi</label>
                    <form action="http://localhost:8080/gestionemappe/db/mostraPesi" method="get">
                        <input id="aggiungipesi" type="submit" value="Modifica Pesi" class="coolbutton">
                    </form>
                    <br>
                </div>
                <div>
                    <label>Lancia o blocca l'emergenza</label><br>
                    <input id="lanciaemergenza" type="button" value="Lancia Emergenza" class="coolbutton">
                    <input id="backtonormal" type="button"  value="Blocca Emergenza" class="coolbutton">
                </div>
                <br>
                <div>
                    <label>Visualizza le persone collegate</label><br>
                    <a href="http://localhost:8080/gestionemappe/db/visualizzastatistiche"><input id="visualizzastatistiche" type="button"  value="Visualizza" class="coolbutton"></a>
                </div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    var value = "<%= piani %>";
</script>

</html>