var file = this.files[0];
var directions = new google.maps.DirectionsRenderer();
var directionsServie = new google.maps.DirectionsService();
var request = [];
var path = [];
var value = 0;   // used for LatLng object


var fReader = new FileReader();
fReader.onload = function(progressEvent) {
  console.log("in progressEvent");

  // split file by lines
  var lines = [];
  lines = this.result.split('\n');

  // trim off bad entries
  /*for(int i = lines.length = 1; i > 0; --i) {
    if(lines[i].split(",").length != 2) {

    }
  }*/

  console.log(lines.length);

  // array for splitting lines into lat and lng
  var latLon = [];
  var dest;
  for(var i = 0; i < lines.length; ++i) {

    latLon = lines[i].split(',');
    // check if valid entry
    if(latLon.length != 2) {
      console.log("invalid position in file, or end file");
      continue;
    }
    dest = latLon;
    value = new google.maps.LatLng(parseFloat(latLon[0]), parseFloat(latLon[1]));

    if(i === 0) {
      request[__origin__] = value;
      continue;
    }

    path.push(value);
    //var options = {

    //};


    //var value = new google.maps.LatLng(latLon[0], LatLon[1]);
    //console.log(latLon.length);
    //console.log(latLon[1]);

  }


  var rendererOptions = {
    map: map,
    polylineOptions: path
  };

  directions.setOptions(rendererOptions);
  console.log(path);
};
fReader.readAsText(file);
