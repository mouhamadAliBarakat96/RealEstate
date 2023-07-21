var currentMarker = null;


function handlePointClick(event) {
		document.getElementById('myForm:lat').value = event.latLng.lat();
		document.getElementById('myForm:lng').value = event.latLng.lng();
		$('#myForm\\:markerBtn').click();
}

function loadPointOnMap(){
	var lat = document.getElementById('myForm:lat').value;
	var lng = document.getElementById('myForm:lng').value;
	var title = document.getElementById('myForm:title').value;
	
	if(lat!=null && lat && lng!=null && lng){
		currentMarker = new google.maps.Marker({
			position : new google.maps.LatLng(lat, lng)
		});
		currentMarker.setTitle(title);
		PF('map').addOverlay(currentMarker);
	}
}

$(document).ready(function() {
    // Call your function or initialize your script here
	loadPointOnMap();
  });


// Use noConflict to release control of the $ variable
jQuery.noConflict();

// Use jQuery with the "jQuery" variable instead of "$"
jQuery(document).ready(function() {
	jQuery('p').click(function() {
		jQuery(this).hide();
	});
});

