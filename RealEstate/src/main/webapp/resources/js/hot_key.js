document.addEventListener("keydown", function(e) {

	if (e.ctrlKey && e.which === 83) { // Check for the Ctrl key being pressed,
		event.preventDefault();
		// and if the key = [S] (83)
		$('.btn-save').click();
		return null;
	}
});

document.addEventListener("keydown", function(e) {

	if (e.ctrlKey && e.which === 90) { // Check for the Ctrl key being pressed,
		event.preventDefault();
		// and if the key = [Z] (90)
		$('.btn-new').click();
		return null;
	}
});

document.addEventListener("keydown", function(e) {

	if (e.ctrlKey && e.which === 88) { // Check for the Ctrl key being pressed,
		event.preventDefault();
		// and if the key = [X] (88)
		$('.btn-crud-page').click();
		return null;
	}
});

document.addEventListener("keydown", function(e) {

	if (e.ctrlKey && e.which === 68) { // Check for the Ctrl key being pressed,
		event.preventDefault();
		// and if the key = [D] (68)
		$('.btn-cancel').click();
		return null;
	}
});
document.addEventListener("keydown", function(e) {
	if (e.ctrlKey && e.keyCode == 69) {
		$(".btn-edit").click();
		e.preventDefault();
		return false;
	}
});

document.addEventListener("keydown", function(e) {

	if (e.ctrlKey && e.which === 77) { // Check for the Ctrl key being pressed,
		event.preventDefault();
		// and if the key = [M] (68)
		$('.hide-aside').click();
		return null;
	}
});

