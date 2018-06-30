window.twttr = (function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0], t = window.twttr || {};
	if (d.getElementById(id))
		return t;
	js = d.createElement(s);
	js.id = id;
	js.src = "https://platform.twitter.com/widgets.js";
	fjs.parentNode.insertBefore(js, fjs);

	t._e = [];
	t.ready = function(f) {
		t._e.push(f);
	};

	return t;
}(document, "script", "twitter-wjs"));

//https://developer.twitter.com/en/docs/twitter-for-websites/javascript-api/guides/javascript-api
twttr.ready(function (twttr) {
	
	twttr.events.bind('follow', function(event){
		 var followedUserId = event.data.user_id;
		 var followedScreenName = event.data.screen_name;
		 console.log(followedUserId, followedScreenName);
	});
});