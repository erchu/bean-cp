var enabledPages = (function() {
	var enabledPageList = [
		"home",
		"download",
		"user-manual",
		"support"
	];

	function isEnabled(pageName) {
		return ($.inArray(pageName, enabledPageList) != -1);
	}
	
	return {
		isEnabled: isEnabled
	}
})();
