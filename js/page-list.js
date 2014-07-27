var pageList = (function() {
	var enabledPageList = [
		"home",
		"download",
		"user-manual",
		"support"
	];
		
	function getPagePath(address) {
		var indexOfFolderSeparator = address.indexOf(",");
		
		if (indexOfFolderSeparator != -1) {
			return address.substring(0, indexOfFolderSeparator);
		} else {
			return address;
		}
	}
	
	function getAnchorOnPage(address) {
		var indexOfFolderSeparator = address.indexOf(",");
		
		if (indexOfFolderSeparator != -1) {
			return address.substring(indexOfFolderSeparator + 1);
		} else {
			return null;
		}
	}

	function isPageEnabled(address) {
		return ($.inArray(getPagePath(address), enabledPageList) != -1);
	}
	
	function getPageList() {
		var result = [];
		
		for (i = 0; i < enabledPageList.length; i++) {
			result.push(enabledPageList[i]);
		}
		
		return result;
	}
	
	return {
		getPagePath: getPagePath,
		isPageEnabled: isPageEnabled,
		getPageList: getPageList,
		getAnchorOnPage: getAnchorOnPage
	}
})();
