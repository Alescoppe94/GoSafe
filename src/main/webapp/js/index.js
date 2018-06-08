$(document).ready(function() {
    var src = document.getElementById("imageselect");
    var target = document.getElementById("target");
    function showImage(src,target) {
        var fr=new FileReader();
        // when image is loaded, set the src of the image where you want to display it
        fr.onload = function(e) { target.src = this.result; };
        src.addEventListener("change",function() {
            // fill fr with image data
            fr.readAsDataURL(src.files[0]);
        });
    }
    showImage(src,target);
    $('#backtonormal').click(  function () {
        var url = "http://localhost:8080/gestionemappe/mappe/backtonormalmode";
        //var params = "lorem=ipsum&name=alpha";
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                location.reload();
            }
        }
        xhr.open("PUT", url, true);

        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-type", "application/json");

        xhr.send();
        //xhr.send(params);
    });
    $('#lanciaemergenza').click(  function () {
        var url = "http://localhost:8080/gestionemappe/mappe/lanciaemergenza";
        //var params = "lorem=ipsum&name=alpha";
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                location.reload();
            }
        }
        xhr.open("POST", url, true);

        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-type", "application/json");

        xhr.send();
        //xhr.send(params);
    });
    $('#aggiungipiano').click(  function () {
        var name = document.getElementById("name");
        var file = document.querySelector('#csvbeacons').files[0];
        var file2 = document.querySelector('#csvtronchi').files[0];
        if(checkForm(name, file, file2)){
            var url = "http://localhost:8080/gestionemappe/db/aggiungipiano";
            //var params = "lorem=ipsum&name=alpha";
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState == XMLHttpRequest.DONE) {
                    var result = JSON.parse(xhr.response);
                    if (result.length > 0) {
                        var message = "";
                        for (var key in result) {
                            if (result.hasOwnProperty(key)) {
                                message += result[key] + ",\n";
                            }
                        }
                        alert("Questi beacon sono gia presenti nel database:\n" + message);
                    }
                    location.reload();
                }
            }
            xhr.open("POST", url, true);

            //Send the proper header information along with the request
            xhr.setRequestHeader("Content-type", "application/json");


            nameencoded = encodeURIComponent(name.value);
            var image = document.getElementById("target");

            var canvas = document.createElement('canvas');
            canvas.width = image.width;
            canvas.height = image.height;
            var cc = canvas.getContext('2d');
            cc.drawImage(image, 0, 0);
            var dataURL = canvas.toDataURL('image/jpeg', 0.6);

            var count = 2;

            var files = {};
            files["beaconcsv"] = file;
            files["tronchicsv"] = file2;
            var fileList = {};

            for (var i = 0; i < count; i++) {       // invoke readers
                getBase64(Object.values(files)[i], Object.keys(files)[i]);
            }

            function onDone(fileList) {
                console.log('key', Object.keys(files)[0]);
                if (Object.keys(fileList)[0] === "beaconcsv") {
                    xhr.send(JSON.stringify({
                        piano: nameencoded,
                        immagine: dataURL,
                        beaconcsv: Object.values(fileList)[0],
                        troncocsv: Object.values(fileList)[1]
                    }));
                } else {
                    xhr.send(JSON.stringify({
                        piano: nameencoded,
                        immagine: dataURL,
                        beaconcsv: Object.values(fileList)[1],
                        troncocsv: Object.values(fileList)[0]
                    }));
                }
            }

            function getBase64(file, name) {
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function () {
                    console.log('name', name);
                    fileList[name] = reader.result;
                    if (!--count) onDone(fileList);
                };
                reader.onerror = function (error) {
                    console.log('Error: ', error);
                };
            }

            var base = getBase64(file); // prints the base64 string

        } else {
            alert("Completa la form per aggiungere il piano");
        }

        function checkForm(nome, file, file2) {
            if(nome.value.length>0 && typeof file !== "undefined" && typeof file2 !== "undefined" && src.value.length>0){
                return true;
            } else{
                return false
            }
        }
        //xhr.send(params);
    });

    $('[id^="piano-"]').click(function() {
        // do something
        var name = $(this).attr('name');
        console.log(name);
        var id = name.split('-');
        var idPiano = id[0];
        var url = "http://localhost:8080/gestionemappe/db/eliminapiano/"+ idPiano;
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