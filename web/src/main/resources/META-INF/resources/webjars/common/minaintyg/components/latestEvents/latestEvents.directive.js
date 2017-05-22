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

angular.module('common').directive('latestEvents', ['$filter', 'common.messageService', 'common.recipientsFactory',
    function($filter, messageService, recipientsFactory) {
        'use strict';

        function _getEventText(msgProperty, params) {
            var text = messageService.getProperty(msgProperty, params);
            return text.length === 0 ? '' : text;
        }


        return {
            restrict: 'E',
            replace: true,
            scope: {
                certId: '@',
                statuses: '=',
                hideHeader: '@',
                maxStatuses: '@'
            },
            templateUrl: '/web/webjars/common/minaintyg/components/latestEvents/latestEvents.directive.html',
            link: function(scope, element, attrs) {

                function _updateStatusModel() {
                    scope.filteredStatuses = $filter('miRelevantStatusFilter')(scope.statuses);
                }
                scope.$watch('statuses', function(){
                    _updateStatusModel();
                });

                _updateStatusModel();


                scope.messageService = messageService;
                scope.isCollapsedArchive = true;

                // Default hideHeader attribute to false if not explicitly set to true
                scope.hideHeader = attrs.hideHeader === 'true';

                // Compile event status message info (date and text)
                scope.getEventInfo = function(status) {
                    var timestamp = status.timestamp ?
                        moment(status.timestamp).format('YYYY-MM-DD HH:mm') :
                        messageService.getProperty('certificates.status.unknowndatetime');
                    var params = [recipientsFactory.getNameForId(status.target)];
                    var msgProperty = 'certificates.status.' + status.type.toLowerCase(); //received [sic] or sent
                    var text = _getEventText(msgProperty, params);
                    return {timestamp: timestamp, text: text};
                };

                scope.statusesShown = function(statuses, statusViewCollapsed) {
                    var nrOfStats = statuses ? statuses.length : 0;
                    var shown = Math.min(nrOfStats, scope.maxStatusRows(statusViewCollapsed));
                    return messageService.getProperty('certificates.status.statusesshown', [shown, nrOfStats]);
                };

                scope.maxStatusRows = function(isCollapsedArchive) {
                    return scope.maxStatuses ? scope.maxStatuses : (isCollapsedArchive ? 2 : 4);
                };

                scope.expandClicked = function() {
                    if (scope.filteredStatuses.length > 4) {
                        if (!scope.showModal) {
                            scope.showModal = {};
                        }
                        scope.showModal.value = !scope.showModal.value;
                    } else {
                        scope.isCollapsedArchive = !scope.isCollapsedArchive;
                    }
                };

            }

        };

    }]
);


