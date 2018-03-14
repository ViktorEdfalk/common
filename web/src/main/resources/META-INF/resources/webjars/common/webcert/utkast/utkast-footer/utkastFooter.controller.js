/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').controller('common.UtkastFooter',
    ['$scope', '$rootScope', '$timeout',
        function($scope, $rootScope, $timeout) {
            'use strict';

            /**
             * Handle the problem of jumping /scolling of content in regard to clicking sign/visa fel.
             * We need to store the buttons position asap (mousedown) because validation triggered onblur will
             * change the DOM before the ng-click (mouse-down+ some time + mouseup = click) event happens.
             */
            var savedElementTop  = 0;
            var scrollToElement = document.getElementById('utkast-footer');
            var containerElement = $('#certificate-content-container');
            var buttonClicked = false;

            $scope.initValidationSequence = function() {
                var offset = scrollToElement.offsetTop;
                var scrollTop = containerElement.scrollTop();

                savedElementTop = offset - scrollTop;
                buttonClicked = true;
            };


            /**
             * Whenever a validation round is completed, either directly by clicking a button or by bluring a validated field -
             * scroll (back) to where we were before 'content-changed-above' scrolling occurred.
             */
            var unbindFastEvent = $rootScope.$on('validation.messages-updated', function () {
                if(buttonClicked) {
                    //Need a timeout here so that the focused button has appeared in it's new position
                    $timeout(function() {
                        if (savedElementTop > 0) {
                            //restore scroll position
                            var top = scrollToElement.offsetTop;
                            containerElement.scrollTop(top - savedElementTop);
                        }
                    });

                    buttonClicked = false;
                }
            });

            $scope.$on('$destroy', unbindFastEvent);
        }
    ]
);
