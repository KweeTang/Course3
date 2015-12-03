var map;
var showCoords = 0;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat:-34.397, lng:150.644},
    zoom: 8
  });
  console.log("In init Map");

  // onMouseMove -- used to see mouse coordinates in console
  if(showCoords) {
    google.maps.event.addListener(map, 'mousemove', function (event) {
      var pnt = event.latLng;
      var lat = pnt.lat();
      lat = lat.toFixed(4);
      var lng = pnt.lng();
      lng = lng.toFixed(4);
      console.log("Latitude: " + lat + "  Longitude: " + lng);
    });
  }
}

// function called when file loaded changes,
// will probably change as interface changes to have multiple routes

document.getElementById("route-file").onchange = function() {
  var file = this.files[0];
  var fReader = new FileReader();
  fReader.readAsText(file);
  fReader.onload = function(progressEvent) {
    var lines = [];
    lines = this.result.split('\n');
    var request = buildRequest(lines);
    displayRoute(request);
  };
};

// build request
function buildRequest(lines) {
  var latLon = [];
  var originVal = -1;
  var path = [];
  var destVal;

  for(var i = 0; i < lines.length; ++i) {
    latLon = lines[i].split(',');
    if(latLon.length != 2) {
      console.log("invalid line in file, or newline at EOF");
      continue;
    }
    if(originVal === -1) {
      originVal = latLon;
      continue;
    }
    path.push({
      location : {lat : parseFloat(latLon[0]), lng : parseFloat(latLon[1])},
      stopover : false
    });
  }

  destVal = path.pop();
  destVal = {
    lat : destVal.location.lat,
    lng : destVal.location.lng
  };
  console.log(destVal);
  originVal = {
    lat : parseFloat(originVal[0]),
    lng : parseFloat(originVal[1])
  };

  return {
    origin : originVal,
    waypoints : path,
    destination : destVal,
    travelMode : google.maps.TravelMode.DRIVING,
    optimizeWaypoints : true
  };


}

function displayRoute(request) {
  var directionsService = new google.maps.DirectionsService();
  var renderer = new google.maps.DirectionsRenderer();
  renderer.setPanel(document.getElementById('directions-panel'));
  renderer.setMap(map);

  var statusFunc = function(response, status) {
    console.log(response);
    if(status === google.maps.DirectionsStatus.OK) {
      renderer.setDirections(response);

    }
    else {
      alert("Error in displaying directions: " + status);
    }
  };

  directionsService.route(request, statusFunc);
  console.log(request);
}
