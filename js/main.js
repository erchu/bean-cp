$( document ).ready(function() {
	$("nav a").click(function(e) {
		if ($(this).attr("href") === "#") {
			e.preventDefault();
			alert("Not available for now");
		}
	});
});
