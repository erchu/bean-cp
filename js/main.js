$( document ).ready(function() {
	$("nav a").click(function(e) {
		if ($(this).attr("href") === "#") {
			e.preventDefault();
			alert("Not available for now");
		}
	});
	
	function showApiSpecs() {
		var apiDialog = $("#dialog");
		var width = $(window).width();
		var height = $(window).height();

		width = width - 50;
		height = height - 120;

		$(apiDialog.children("iframe").get(0)).css("height", height + "px");

		apiDialog.dialog({
			modal: true,
			height: "auto", // Set the height to auto so that it grows along with the iframe.
			width: width
		});
	}
	
	$('#api-specs-link').click(function(e) {
		e.preventDefault();
		showApiSpecs();
	});
	
	$('#footer-api-specs-link').click(function(e) {
		e.preventDefault();
		showApiSpecs();
	});
});
