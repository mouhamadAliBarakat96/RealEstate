

$(document).ready(function(){
    $("#myModal").on('shown.bs.modal', function(){
        $(this).find('input[type="text"]').focus();
    });
});

function openDailog() {
	$('#myModal').modal('toggle');
	var options = {
			valueNames : [ 'name' ]
		};

		var hackerList = new List('hacker-list', options);

}