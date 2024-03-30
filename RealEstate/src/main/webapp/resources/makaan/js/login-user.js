window.fbAsyncInit = function() {
	FB.init({
		appId : '1504756280267313',
		status : true, // check login status
		cookie : true, // enable cookies to allow the server to access the
		// session
		xfbml : true
	// parse XFBML
	});

	// Additional initialization code here

	showMe = function(response) {
		if (response.status !== 'connected') {
			div.innerHTML = '<em>Not Connected</em>';
		} else {
			FB.api('/me', function(response) {
				var i = 0;
				var id = '';
				for ( var key in response) {

					i++;
					// /console.log(key);
					if (key === 'id') {
						id = response[key];
						console.log(response[key]);
					}

					// console.log(response[key]);

				}

				remoteCommandAction([ {
					name : 'fbId',
					value : id
				} ]);
			});
		}
	};

	FB.getLoginStatus(function(response) {

		FB.Event.subscribe('auth.authResponseChange', showMe);
		FB.Event.subscribe('auth.authResponseChange', console.log('change'));

	});

};

// Load the SDK Asynchronously
(function(d) {
	var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	if (d.getElementById(id)) {
		return;
	}
	js = d.createElement('script');
	js.id = id;
	js.async = true;
	js.src = "//connect.facebook.net/en_US/all.js";
	ref.parentNode.insertBefore(js, ref);

}(document));

