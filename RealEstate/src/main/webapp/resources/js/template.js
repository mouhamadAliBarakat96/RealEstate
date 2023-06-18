
 $(".filterNumbersOnly").keydown(function(evt) {
	    	var charCode = (evt.which) ? evt.which : event.keyCode ;
		if (charCode > 31 && (charCode < 48 || charCode > 57))
			return false;

		return true;
	    })

	$(".thousand-number").on('input', function(){
	    var n = parseInt($(this).val().replace(/\D/g,''),10);
	    $(this).val(n.toLocaleString());
	});

function numberWithCommas(x) {
	  x=String(x).toString();
	  var afterPoint = '';
	  if(x.indexOf('.') > 0)
	     afterPoint = x.substring(x.indexOf('.'),x.length);
	  x = Math.floor(x);
	  x=x.toString();
	  var lastThree = x.substring(x.length-3);
	  var otherNumbers = x.substring(0,x.length-3);
	  if(otherNumbers != '')
	      lastThree = ',' + lastThree;
	  return otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree + afterPoint;
	}
function generate(text, detail) {
	toastr.options.timeOut = 5000;
	toastr.options.positionClass = "toast-top-right";
	toastr.options.showDuration = 600;
	toastr.options.hideDuration = 500;
	toastr.options.showMethod = 'slideDown';
	toastr.options.closeButton = 'true';

	toastr.options.hideMethod = 'slideUp';
	toastr.options.progressBar= true ;
	if (detail == "error") {
		toastr.error(text);
	} else if (detail == "info") {
		toastr.info(text);
	} else if (detail == "warn") {
		toastr.warning(text);
	} else if (detail == "success") {
		toastr.success(text);
	} else {
		toastr.warning(text);
	}

	initActiveMenu();
	initMainIconMenu();

}
function inputNumberPicker() {
	$('.input-number-picker').inputSpinner();
}

function isNumberKey(evt) {

	var charCode = (evt.which) ? evt.which : event.keyCode
	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

	return true;
}

function select2me() {
	$('.select2me:not(.has-select2me)').select2();
	$('.select2me:not(.has-select2me)').addClass('has-select2me');

}

function inputNumberPicker() {
	$('.input-number-picker:not(.has-input-number-picker)').inputSpinner();
	$('.input-number-picker:not(.has-input-number-picker)').addClass(
			'has-input-number-picker');

}

function initDatePicker() {
	$('.datepicker').datepicker({
		todayHighlight : true,
		format : 'dd/mm/yyyy',
		language : "ar",
		autoclose : true,

	});
	$('.datetimepicker').datetimepicker({
		todayHighlight : true,
		format : 'dd/mm/yyyy hh:ii',
		language : "ar",
		autoclose : true
	});
	$('.timepicker').timepicker({
		autoclose : true,
		format : 'hh:mm',
		minuteStep : 1,
		showMeridian : false,
		defaultTime : false
	});

}

function showHideToogle() {
	var x = document.getElementsByClassName("dateTemplate");
	if (x[0].style.display === "none") {
		x[0].style.display = "block";
	} else {
		x[0].style.display = "none";
	}
}

function initActiveMenu() {
	// === following js will activate the menu in left side bar based on url
	// ====
	$(".left-sidenav a").each(
			function() {
				var pageUrl = window.location.href.split(/[?#]/)[0];
				if (this.href == pageUrl) {
					$(this).addClass("active");
					$(this).parent().parent().addClass("in");
					$(this).parent().parent().addClass("mm-show");
					$(this).parent().parent().prev().addClass("active");
					$(this).parent().parent().parent().addClass("active");
					$(this).parent().parent().parent().addClass("mm-active");
					$(this).parent().parent().parent().parent().addClass("in");
					$(this).parent().parent().parent().parent().parent()
							.addClass("active");
					$(this).parent().parent().parent().parent().parent()
							.parent().addClass("active");
					var menu = $(this).closest('.main-icon-menu-pane').attr(
							'id');
					$("a[href='#" + menu + "']").addClass('active');

				}
			});
}

function initMainIconMenu() {
	$('.main-icon-menu .nav-link').on('click', function(e) {
		e.preventDefault();
		$(this).addClass('active');
		$(this).siblings().removeClass('active');
		$('.main-menu-inner').addClass('active');
		var targ = $(this).attr('href');
		$(targ).addClass('active');
		$(targ).siblings().removeClass('active');
	});
}