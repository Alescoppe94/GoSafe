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
        xhr.open("PUT", url, true);

        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-type", "application/json");

        xhr.send();
        //xhr.send(params);
    });
    $('#aggiungipiano').click(  function () {
        var url = "http://localhost:8080/gestionemappe/db/aggiungipiano";
        //var params = "lorem=ipsum&name=alpha";
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);

        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-type", "application/json");

        var name = document.getElementById("name");
        nameencoded = encodeURIComponent(name.value);
        var image = document.getElementById("target");

        var canvas = document.createElement('canvas');
        canvas.width = image.width;
        canvas.height = image.height;
        var cc = canvas.getContext('2d');
        cc.drawImage(image, 0, 0);
        var dataURL = canvas.toDataURL('image/jpeg', 0.6);

        var count = 2;
        var file = document.querySelector('#csvbeacons').files[0];
        var file2 = document.querySelector('#csvtronchi').files[0];
        var files = {};
        files["beaconcsv"] = file;
        files["tronchicsv"] = file2;
        var fileList = {};

        for(var i = 0; i < count; i++) {       // invoke readers
            getBase64(Object.values(files)[i], Object.keys(files)[i]);
        }

        function onDone(fileList){
            console.log('key', Object.keys(files)[0]);
            if(Object.keys(fileList)[0] === "beaconcsv"){
                xhr.send(JSON.stringify({piano:nameencoded, immagine:dataURL, beaconcsv:Object.values(fileList)[0], troncocsv:Object.values(fileList)[1]}));
            }else{
                xhr.send(JSON.stringify({piano:nameencoded, immagine:dataURL, beaconcsv:Object.values(fileList)[1], troncocsv:Object.values(fileList)[0]}));
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

        //xhr.send(params);
    });


});