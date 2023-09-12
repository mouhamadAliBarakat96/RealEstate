(function($) {
	"use strict";

	// Spinner
	var spinner = function() {
		setTimeout(function() {
			if ($('#spinner').length > 0) {
				$('#spinner').removeClass('show');
			}
		}, 1);
	};
	spinner();

	// Initiate the wowjs
	new WOW().init();

	// Sticky Navbar
	$(window).scroll(function() {
		if ($(this).scrollTop() > 45) {
			$('.nav-bar').addClass('sticky-top');
		} else {
			$('.nav-bar').removeClass('sticky-top');
		}
	});

	// Back to top button
	$(window).scroll(function() {
		if ($(this).scrollTop() > 300) {
			$('.back-to-top').fadeIn('slow');
		} else {
			$('.back-to-top').fadeOut('slow');
		}
	});
	$('.back-to-top').click(function() {
		$('html, body').animate({
			scrollTop : 0
		}, 1500, 'easeInOutExpo');
		return false;
	});

	// Header carousel
	$(".header-carousel").owlCarousel(
			{
				autoplay : true,
				smartSpeed : 1500,
				items : 1,
				dots : true,
				loop : true,
				nav : true,
				navText : [ '<i class="bi bi-chevron-left"></i>',
						'<i class="bi bi-chevron-right"></i>' ]
			});

	// Testimonials carousel
	$(".testimonial-carousel").owlCarousel(
			{
				autoplay : true,
				smartSpeed : 1000,
				margin : 24,
				dots : false,
				loop : true,
				nav : true,
				navText : [ '<i class="bi bi-arrow-left"></i>',
						'<i class="bi bi-arrow-right"></i>' ],
				responsive : {
					0 : {
						items : 1
					},
					992 : {
						items : 2
					}
				}
			});

})(jQuery);

function isNumberKey(evt) {

	var charCode = (evt.which) ? evt.which : event.keyCode
	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

	return true;
}

function generate(text, detail) {
	console.log('text :' + text + ' detail' + detail);

	toastr.options.timeOut = 5000;
	displyLocation = (document.documentElement.getAttribute("dir") == 'LTR' ? "toast-top-right"
			: "toast-top-left");
	toastr.options.positionClass = displyLocation;
	toastr.options.showDuration = 800;
	toastr.options.hideDuration = 1000;
	toastr.options.showMethod = 'slideDown';
	toastr.options.hideMethod = 'slideUp';
	toastr.options.progressBar = true;
	toastr.options.closeButton = true;
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

}

function updateActiveMenu() {
	var links = $(".navbar-nav > a");
	for (var i = 0; i < links.length; i++) {
		var link = links[i];
		if (link.href === location.href) {
			link.classList.add("active");
		} else {
			link.classList.remove("active");
		}
	}
}

function changeLang(lang) {
	var page = document.documentElement;
	// Switch the direction attribute based on the current value
	page.setAttribute('dir', lang);

	var path = window.location.pathname;
	var pageName = path.split("/").pop();

	if (pageName === 'user-login.xhtml' || pageName === 'user-login.xhtml') {
		window.location.reload();
	}

}


$(document).ready(function() {
	// Get the current URL
	var currentURL = window.location.href;

	// Get all the navigation links
	var navLinks = document.querySelectorAll('.navbar-nav .nav-link');

	// Loop through each navigation link
	for (var i = 0; i < navLinks.length; i++) {
		var linkURL = navLinks[i].href;

		// Compare the link URL with the current URL
		if (currentURL === linkURL) {
			// Add the active class to the matching link
			navLinks[i].classList.add('active');
		}
	}
	
	 
});


// Use noConflict to release control of the $ variable
jQuery.noConflict();

