jQuery(document).ready(function($) {

	select2me();

});

function select2me() {
	$('.select2me:not(.has-select2me)').select2({
	

	});
	$('.select2me:not(.has-select2me)').addClass('has-select2me');

}

function onRenderPage() {
	$('a[href^="#"]').on('click', function(event) {
		$('.resume').hide()
		$('.resume').remove("active");
		var target = $(this).attr('href');

		$('.resume' + target).toggle();
		$('.resume' + target).addClass("active");
	});
}

$(function() {
	$('#teacherUsername').on('keypress', function(e) {
		if (e.which == 32) {
			return false;
		}
	});
});
$(function() {
	$('#username').on('keypress', function(e) {
		if (e.which == 32) {
			return false;
		}
	});
});
