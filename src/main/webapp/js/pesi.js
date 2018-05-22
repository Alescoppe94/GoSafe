$(document).ready(function (){

    $('[id^="peso-"]').click(function() {
        // do something
        var name = $(this).attr('id');
        var id = name.split('-');
        var idPeso = id[1];
        var url = "http://localhost:8080/gestionemappe/db/eliminapeso/"+ idPeso;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                location.reload();
            }
        }
        xhr.open("DELETE", url, true);

        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send();



    });

});