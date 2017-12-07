angular.module('common').animation('.fold-animation', ['$animateCss', '$log', function($animateCss, $log) {
    'use strict';
    return {
        enter: function(element, doneFn) {
            var height = element[0].offsetHeight;

            var current = element[0];
            var hidden = false;
            while (current.parentNode) {
                var display = angular.element(current).css('display');
                if (display === 'none') {
                    hidden = true;
                    break;
                }
                current = current.parentNode;
            }

            if (hidden) {
                return $animateCss(element, {
                    addClass: ''
                });
            }

            var animator = $animateCss(element, {
                addClass: 'fold-slide-fade-animation',
                easing: 'ease-out',
                from: { height:'0', padding:'0' },
                to: { height: height + 'px' },
                duration: 0.3,
                cleanupStyles: true
            });

            return animator;
        },
        leave: function(element, doneFn) {
            var height = element[0].offsetHeight;
            return $animateCss(element, {
                addClass: 'fold-slide-fade-animation-hidden',
                easing: 'ease-out',
                from: { height: height + 'px' },
                to: { height:'0' },
                duration: 0.3
            });
        }
    };
}]);


angular.module('common').animation('.slide-animation', ['$animateCss', '$log', function($animateCss, $log) {
    'use strict';
    return {
        enter: function(element, doneFn) {

            var current = element[0];
            var hidden = false;
            while (current.parentNode) {
                var display = angular.element(current).css('display');
                if (display === 'none') {
                    hidden = true;
                    break;
                }
                current = current.parentNode;
            }

            if (hidden) {
                return $animateCss(element, {
                    addClass: ''
                });
            }

            var animator = $animateCss(element, {
                addClass: 'slide-fade-animation',
                easing: 'ease-out',
                duration: 0.3,
                cleanupStyles: true
            });

            return animator;
        },
        leave: function(element, doneFn) {
            return $animateCss(element, {
                addClass: 'slide-fade-animation-hidden',
                easing: 'ease-out',
                duration: 0.3
            });
        }
    };
}]);
