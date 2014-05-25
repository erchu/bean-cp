$( document ).ready(function() {
	$("#page-menu a").click(function(e) {
		if ($(this).attr("href") === "#") {
			e.preventDefault();
			alert("Not available for now");
		}
	});
});
