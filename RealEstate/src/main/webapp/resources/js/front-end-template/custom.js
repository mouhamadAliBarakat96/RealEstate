(function($) {
	"use strict";

	$(".owl-carousel").owlCarousel({
		loop : true,
		margin : 30,
		nav : true,
		pagination : true,
		responsive : {
			0 : {
				items : 1
			},
			600 : {
				items : 2
			},
			1000 : {
				items : 3
			}
		}
	});

	// Mobile menu dropdown
	$(".submenu").on("click", function() {
		var width = $(window).width();
		if (width < 992) {
			$(".submenu ul").toggleClass("active");
		}
	});
	// Scroll animation init
	// Scroll animation init

	// Menu Dropdown Toggle
	if ($(".menu-trigger").length) {
		$(".menu-trigger").on("click", function() {
			$(this).toggleClass("active");
			$(".header-area .nav").slideToggle(200);
		});
	}

	const Accordion = {
		settings : {
			// Expand the first item by default
			first_expanded : false,
			// Allow items to be toggled independently
			toggle : false
		},

		openAccordion : function(toggle, content) {
			if (content.children.length) {
				toggle.classList.add("is-open");
				let final_height = Math.floor(content.children[0].offsetHeight);
				content.style.height = final_height + "px";
			}
		},

		closeAccordion : function(toggle, content) {
			toggle.classList.remove("is-open");
			content.style.height = 0;
		},

		init : function(el) {
			const _this = this;

			// Override default settings with classes
			let is_first_expanded = _this.settings.first_expanded;
			if (el.classList.contains("is-first-expanded"))
				is_first_expanded = true;
			let is_toggle = _this.settings.toggle;
			if (el.classList.contains("is-toggle"))
				is_toggle = true;

			// Loop through the accordion's sections and set up the click
			// behavior
			const sections = el.getElementsByClassName("accordion");
			const all_toggles = el.getElementsByClassName("accordion-head");
			const all_contents = el.getElementsByClassName("accordion-body");
			for (let i = 0; i < sections.length; i++) {
				const section = sections[i];
				const toggle = all_toggles[i];
				const content = all_contents[i];

				// Click behavior
				toggle.addEventListener("click", function(e) {
					if (!is_toggle) {
						// Hide all content areas first
						for (let a = 0; a < all_contents.length; a++) {
							_this.closeAccordion(all_toggles[a],
									all_contents[a]);
						}

						// Expand the clicked item
						_this.openAccordion(toggle, content);
					} else {
						// Toggle the clicked item
						if (toggle.classList.contains("is-open")) {
							_this.closeAccordion(toggle, content);
						} else {
							_this.openAccordion(toggle, content);
						}
					}
				});

				// Expand the first item
				if (i === 0 && is_first_expanded) {
					_this.openAccordion(toggle, content);
				}
			}
		}
	};

	(function() {
		// Initiate all instances on the page
		const accordions = document.getElementsByClassName("accordions");
		for (let i = 0; i < accordions.length; i++) {
			Accordion.init(accordions[i]);
		}
	})();

	// Home seperator
	if ($(".home-seperator").length) {
		$(".home-seperator .left-item, .home-seperator .right-item").imgfix();
	}

	// Home number counterup
	if ($(".count-item").length) {
		$(".count-item strong").counterUp({
			delay : 10,
			time : 1000
		});
	}

	// Page loading animation
	$(window).on("load", function() {
		if ($(".cover").length) {
			$(".cover").parallax({
				imageSrc : $(".cover").data("image"),
				zIndex : "1"
			});
		}

		$("#preloader").animate({
			opacity : "0"
		}, 600, function() {
			setTimeout(function() {
				$("#preloader").css("visibility", "hidden").fadeOut();
			}, 300);
		});
	});
})(window.jQuery);

function generate(text, detail) {
	toastr.options.timeOut = 5000;
	toastr.options.positionClass = "toast-top-right";
	toastr.options.showDuration = 600;
	toastr.options.hideDuration = 500;
	toastr.options.showMethod = 'slideDown';
	toastr.options.closeButton = 'true';

	toastr.options.hideMethod = 'slideUp';
	toastr.options.progressBar = true;
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
