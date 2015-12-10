var obj, lat, long;
var delay = 1000;


function visualizeSearch(map, latLongs) {
	console.log("in the JS");
	for( var i = 0; i<list.length; ++i) {
		displayMarker(list[i]);
	}
}

function displayMarker(map, latLong) {
	setTimeout(function() {
		var marker = new google.maps.Marker({
			map: map,
			position: new google.maps.LatLng(latLong.lat(), latLong.lng())
		})
	}, delay);
}