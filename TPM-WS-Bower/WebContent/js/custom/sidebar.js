/* ========================================================================
 * Sidebar.js v1.0.0
 * A simple jQuery sidebar!
 * https://github.com/Ult-UX/sidebar.js
 * ======================================================================== */
 $('.sidebar-nav').ready(function(){
    $('.sidebar-nav li').click(function(){
        $('.sidebar-nav li').removeClass("active");
        $(this).addClass("active");
    });
  });
(function ($) {
    jQuery.fn.extend({
        sidebar: function(options) {
            // Options Valid
            if (!isValid(options)) {
                return this;
            }
            // jQuery.extend Default Options
            var opts = $.extend({}, defaults, options);
            return this.each(function () {
                var Wrapper = $(this);
                var windowWidth = $(window).width();
                if (windowWidth < opts.breakpoint && !(Wrapper.hasClass(opts.minimized))) {
                    Wrapper.addClass(opts.minimized);
                }
                else if (windowWidth >= opts.breakpoint && Wrapper.hasClass(opts.minimized)) {
                    Wrapper.removeClass(opts.minimized);
                }
                var Toggler = $(opts.toggler)
                Toggler.on('click', function(event) {
                    event.preventDefault();
                    if (Wrapper.hasClass(opts.minimized)) {
                        Wrapper.addClass(opts.animating);
                        setTimeout(function() {
                            Wrapper.removeClass(opts.minimized + ' ' + opts.animating);
                        }, opts.duration);
                    } else {
                        Wrapper.addClass(opts.animating);
                        setTimeout(function() {
                            Wrapper.addClass(opts.minimized);
                            Wrapper.removeClass(opts.animating);
                        }, opts.duration);
                    }
                });
            });
        }
    });
    // Default Options
    var defaults = {
        toggler: '[data-toggle="sidebar"]',
        minimized: 'minimized',
        animating: 'animating',
        duration: 250,
        breakpoint: 768
    };
    // Options Valid
    function isValid(options) {
        return !options || (options && typeof options === "object") ? true : false;
    }
})(jQuery);
