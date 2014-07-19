$(document).ready(function() {
	var app = Sammy('#page-main', function() {
	
		var $pageMain = $("#page-main");
		var $pageMenu = $("#page-menu");

		function _loadPageWithNoSecurityCheck(pageName) {
			$pageMain.load("pages/" + pageName + ".html #page-main-content", function() {
				_updateMenuStatus(pageName);
			});
		}
		
		function _updateMenuStatus(pageName) {
			$pageMenu.find("a.selected").each(function() {
				$(this).removeClass("selected");
			});
			
			$pageMenu.find("a[href=#" + pageName + "]").addClass("selected");
		}

		function loadPage(pageName) {
			if (enabledPages.isEnabled(pageName)) {
				_loadPageWithNoSecurityCheck(pageName);
			} else {
				_loadPageWithNoSecurityCheck("not-found");
			}
		}

		this.get('#:name', function() {
				loadPage(this.params['name']);
			});
		});

	// start the application
	app.run('#home');
});
