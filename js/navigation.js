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

if (getInternetExplorerVersion() < 8) {
	window.location.href = "outdated-browser.html";
} else {
	$(document).ready(function() {
		var SCROLL_TOP_OFFSET = 20;
	
		var $pageMain = $("#page-main");
		var $pageMenu = $("#page-menu");
		var $root = $("html,body");
		var $body = $("body");
		var $window = $(window);
		var $document = $(document);
		var $scrollToTop = $("#scroll-to-top");
		var scrollToTopVisible = false;
		
		var tableOfContentItems;
		var currentTableOfContentsSelectedItem = null;
		
		function _scrollToAnchor(anchor) {
			if (anchor != null) {
				var targetOffset = $("#" + anchor).offset().top - SCROLL_TOP_OFFSET;
				
				$root.animate({scrollTop: targetOffset}, "fast");
			}
		}
		
		function _pageIsScrolledToBottom() {
			return $(window).scrollTop() + $(window).height() == $(document).height();
		}
		
		function _markTableOfContentsSelectedItem(scrollPosition) {
			if (!tableOfContentItems) {
				return;
			}
			
			var newCurrentTableOfContentItem = null;
			
			if (_pageIsScrolledToBottom()) {
				// mark last element even if it not covers full page
				newCurrentTableOfContentItem = tableOfContentItems[tableOfContentItems.length - 1];
			} else {
				for (i = 0; i < tableOfContentItems.length; i++) {
					var iValue = tableOfContentItems[i];
					
					if (iValue.topFrom <= scrollPosition && scrollPosition <= iValue.topTo) {
						newCurrentTableOfContentItem = iValue;
						break;
					}
				}
			}
			
			if (newCurrentTableOfContentItem) {
				if (!currentTableOfContentsSelectedItem || newCurrentTableOfContentItem.id !== currentTableOfContentsSelectedItem.id) {
					$("section.table-of-contents li.selected").removeClass("selected");
					$("section.table-of-contents li[data-header-id=\"" + newCurrentTableOfContentItem.id + "\"]").addClass("selected");
					
					$("section.table-of-contents li.selected-first-level").removeClass("selected-first-level");
					$("section.table-of-contents li[data-header-id=\"" + newCurrentTableOfContentItem.selectedFirstLevelId + "\"]").addClass("selected-first-level");
				
					currentTableOfContentsSelectedItem = newCurrentTableOfContentItem;
				}
			}
		}
		
		function _setupDocumentScrollHandler() {
			$window.scroll(function() {
				var documentScrollTop = $document.scrollTop();
				
				if (documentScrollTop > 88) {
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
				
				//_markTableOfContentsSelectedItem(documentScrollTop);
			});
		}
		
		function _generateTableOfContentsItems() {
			var $tableOfContents = $("section.table-of-contents");
			tableOfContentItems = [];
			
			if ($tableOfContents.length > 0) {
				var previousItem = null;
				var previousItemOffsetTop = null;
				var documentHeight = $(document).height();
					
				$("h2,h3").each(function() {
					var $i = $(this);
					var iOffsetTop = $i.offset().top - SCROLL_TOP_OFFSET;
					
					var newItem = {
						id: $i.attr("id"),
						name: $i.text(),
						type: $i.prop('tagName'),
						topFrom: previousItemOffsetTop || 0,
						topTo: documentHeight	// temporary assuming that there is no next item
					};
					
					tableOfContentItems.push(newItem);
					
					if (previousItem) {
						previousItem.topTo = iOffsetTop - 1;	// previous item is not last item
					}
					
					previousItemOffsetTop = iOffsetTop;
					previousItem = newItem;
				});
				
				if (tableOfContentItems.length > 0) {
					var html = [];
					var lastItemType = "";
					var selectedFirstLevelId = null;
					
					html.push("<div class=\"table-of-contents\">\n");
					html.push("<ul>\n");
					
					for (i = 0; i < tableOfContentItems.length; i++) {
						var iValue = tableOfContentItems[i];
						
						if (lastItemType === iValue.type) {
							html.push("</li>\n");	// close previous item on the same level
						}
						
						if (lastItemType === "H2" && iValue.type === "H3") {
							html.push("\n<ul>\n");	// start second level
						}
						
						if (lastItemType === "H3" && iValue.type === "H2") {
							html.push("</ul>\n</li>\n");	// close second level
						}
						
						if (iValue.type === "H2") {
							selectedFirstLevelId = iValue.id;
						}
						
						iValue.selectedFirstLevelId = selectedFirstLevelId;
						
						html.push("<li data-header-id=\"" + iValue.id + "\"><a href=\"#" + iValue.id + "\">" + iValue.name + "</a>");
						
						lastItemType = iValue.type;
					}
					
					if (lastItemType === "H3") {
						// close second level
						html.push("</ul>\n");
						html.push("</li>\n");
					}
						
					html.push("</li>\n");	// close last item (on first level)
					
					html.push("</ul>\n");
					html.push("</div>\n");
					
					$tableOfContents.html(html.join(""));
				} else {
					$tableOfContents.css("display", "none");
				}
			}
		}
		
		function _setupTableOfContents() {
			_generateTableOfContentsItems();
			
			currentTableOfContentsSelectedItem = null;
			//_markTableOfContentsSelectedItem(window.pageYOffset);
		}
		
		function _setupScrollTopLink() {
			var rootCp = $root;
		
			$scrollToTop.click(function(event) {
				event.preventDefault();
				
				$root.animate({scrollTop: 0}, "fast");
			});
		}
		
		_setupScrollTopLink();
		_setupDocumentScrollHandler();
		_setupTableOfContents();
		
		SyntaxHighlighter.highlight();
	});
}
