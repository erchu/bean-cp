function getInternetExplorerVersion() {
	var undef,
		v = 3,
		div = document.createElement('div'),
		all = div.getElementsByTagName('i');

	while (
		div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->',
		all[0]
	);

	return v > 4 ? v : undef;

};
	
if (getInternetExplorerVersion() < 9) {
	window.location.href = "outdated-browser.html";
} else {
	$(document).ready(function() {
		var app = Sammy('#page-main', function() {
			var $pageMain = $("#page-main");
			var $pageMenu = $("#page-menu");
			var $root = $("html,body");
			var $body = $("body");
			var $window = $(window);
			var $document = $(document);
			var $scrollToTop = $("#scroll-to-top");
			var scrollToTopVisible = false;
		
			var currentPage = null;
			
			function _scrollToAnchor(address) {
				var anchor = pageList.getAnchorOnPage(address);
				
				if (anchor != null) {
					var targetOffset = $("#" + anchor).offset().top - 20;
					
					$root.animate({scrollTop: targetOffset}, "fast");
				}
			}
			
			function _setupTableOfContentsPosition() {
				$window.scroll(function() {
					if ($document.scrollTop() > 88) {
						$body.addClass("pin-table-of-contents");
						
						if (scrollToTopVisible == false) {
							scrollToTopVisible = true;
							$scrollToTop.fadeIn("fast");
						}
					} else {
						$body.removeClass("pin-table-of-contents");
						
						if (scrollToTopVisible) {
							scrollToTopVisible = false;
							$scrollToTop.fadeOut("fast");
						}
					}
				});
			}

			function _loadWithNoSecurityCheck(address) {
				var pagePath = pageList.getPagePath(address);
			
				if (currentPage != pagePath) {
					$pageMain.load("pages/" + pagePath + ".html #page-main-content", function() {
						_updateMenuStatus(pagePath);
						SyntaxHighlighter.highlight();
						_setupTableOfContentsPosition();
						
						_scrollToAnchor(address);
						currentPage = pagePath;
					});
				} else {
					_scrollToAnchor(address);
				}
			}
			
			function _updateMenuStatus(address) {
				$pageMenu.find("a.selected").each(function() {
					$(this).removeClass("selected");
				});
				
				$pageMenu.find("a[href=#" + pageList.getPagePath(address) + "]").addClass("selected");
			}

			function load(address) {
				if (pageList.isPageEnabled(address)) {
					_loadWithNoSecurityCheck(address);
				} else {
					_loadWithNoSecurityCheck("not-found");
				}
			}
			
			function setupScrollTopLink() {
				var rootCp = $root;
			
				$scrollToTop.click(function(event) {
					event.preventDefault();
					
					$root.animate({scrollTop: 0}, "fast");
				});
			}
			
			setupScrollTopLink();

			this.get('#:name', function() {
					load(this.params['name']);
				});
		});

		function loadModuleCssFile(pageName) {
			var cssLocation = "css/" + pageName + ".css";
		
			$.get(cssLocation, function(css) {
			   $('<style type="text/css"></style>')
				  .html(css)
				  .appendTo("head");
			});
		}

		function loadAllModuleCssFiles() {
			var pages = pageList.getPageList();
			
			for (i = 0; i < pages.length; i++) {
				loadModuleCssFile(pages[i]);
			}
		}
		
		loadAllModuleCssFiles();
		
		// start the application
		app.run('#home');
	});
}
