/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * FocusMe directive. Used to set focus to an element via model value
 */
angular.module('common').directive('wcFocusOn', ['common.anchorScrollService',
    function(anchorScrollService) {
        'use strict';
        return function(scope, elem, attr) {
            return scope.$on('wcFocusOn', function(e, name) {
                if (name === attr.wcFocusOn) {
                    elem[0].select();
                    if (name !== 'focusFirstInput') {
                        anchorScrollService.scrollIntygContainerTo(elem[0].id);
                    }
                }
            });
        };
    }]);

angular.module('common').factory('common.wcFocus', [
    '$rootScope', '$timeout', function($rootScope, $timeout) {
        'use strict';
        return function(name) {
            return $timeout(function() {
                    return $rootScope.$broadcast('wcFocusOn', name);
                });
        };
    }
]);
