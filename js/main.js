$(document).ready(function() {
	var app = Sammy('#page-main', function() {
	
		var $pageMain = $("#page-main");
		var $pageMenu = $("#page-menu");

		function _loadPageWithNoSecurityCheck(pagePath) {
			$pageMain.load("pages/" + pagePath + ".html #page-main-content", function() {
				_updateMenuStatus(pagePath);
				SyntaxHighlighter.highlight();
			});
		}
		
		function _updateMenuStatus(pagePath) {
			$pageMenu.find("a.selected").each(function() {
				$(this).removeClass("selected");
			});
			
			var indexOfFolderSeparator = pagePath.indexOf("/");
			var modulePath;
			
			if (indexOfFolderSeparator != -1) {
				modulePath = pagePath.substring(0, indexOfFolderSeparator);
			} else {
				modulePath = pagePath;
			}
			
			$pageMenu.find("a[href=#" + modulePath + "]").addClass("selected");
		}

		function loadPage(pageName) {
			var pagePath = pageName.replace(",", "/");
			
			if (enabledPages.isEnabled(pagePath)) {
				_loadPageWithNoSecurityCheck(pagePath);
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
