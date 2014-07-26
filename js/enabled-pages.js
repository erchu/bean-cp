var enabledPages = (function() {
	var enabledPageList = [
		"home",
		"download",
		"user-manual",
		"user-manual/get-started",
		"support"
	];

	function isEnabled(pageName) {
		return ($.inArray(pageName, enabledPageList) != -1);
	}
	
	return {
		isEnabled: isEnabled
	}
})();
