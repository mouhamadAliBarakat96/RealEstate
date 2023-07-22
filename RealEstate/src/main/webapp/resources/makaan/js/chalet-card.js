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
  
	loadPointOnMap();
  });