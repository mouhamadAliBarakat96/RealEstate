/** open layer map */
var map = null;
var marker = null;

function initMap() {
	map = new ol.Map({
		target : 'map',
		layers : [ new ol.layer.Tile({
			source : new ol.source.OSM()
		}) ],
		view : new ol.View({
			center : ol.proj.fromLonLat([ 35.5097, 33.8938 ]), // Beirut //
			// coordinates
			zoom : 12
		})
	});

	marker = new ol.Overlay({
		element : document.getElementById('marker'),
		positioning : 'bottom-center'
	});

	map.addOverlay(marker);

	// Add a click event listener to the map
	map.on('click', function(event) {
		// Get the clicked coordinates in the map projection
		var clickedCoords = event.coordinate;

		// Set the hidden input values to the clicked coordinates

		$('#myForm\\:lng').val(clickedCoords[0]);
		$('#myForm\\:lat').val(clickedCoords[1]);

		// Set the marker overlay position to the clicked coordinates
		marker.setPosition(clickedCoords);
		// Show the marker overlay
		marker.getElement().style.display = 'block';
		$('#myForm\\:markerBtn').click();
	});
}
function loadCoordinates() {
	var lng = $('#myForm\\:lng').val();
	var lat = $('#myForm\\:lat').val();

	if (isCoordinatesNotEmpty(lat, lng)) {
		var clickedCoords = new Array(2);
		// Set the hidden input values to the clicked coordinates
		clickedCoords[0] = lng;
		clickedCoords[1] = lat;
		// Set the marker overlay position to the clicked coordinates
		marker.setPosition(clickedCoords);
		// Show the marker overlay
		marker.getElement().style.display = 'block';
	}
}

function isCoordinatesNotEmpty(latitude, longitude) {
	return latitude != null && longitude != null && latitude !== ''
			&& longitude !== '';
}

function showTheMap() {
	var postType = $('#myForm\\:postType').val();
	var kinEnum = $('#myForm\\:kind').val();

	if (postType == '' && kinEnum == '')
		$('#map').hide();
	else
		$('#map').show();

}


$(document).ready(function() {
	map = initMap();
	loadCoordinates();
	//showTheMap();
});
/** END */

// Use noConflict to release control of the $ variable
jQuery.noConflict();
