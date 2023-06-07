
$(document).ready(function(){
    $("#myModal").on('shown.bs.modal', function(){
        $(this).find('input[type="text"]').focus();
    });
});


function select2me() {
	$('.select2me:not(.has-select2me)').select2();
	$('.select2me:not(.has-select2me)').addClass('has-select2me');

}

function openDailog() {
	$('#myModal').modal('toggle');
	
	var options = {
		valueNames : [ 'name' ]
	};

	var hackerList = new List('hacker-list', options);


}