/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcHelpTipsPanelTab', [ '$log', 'common.moduleService', function($log, moduleService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcHelpTipsPanelTab/wcHelpTipsPanelTab.directive.html',
        link: function($scope) {
            var intygsModule = moduleService.getModule($scope.config.intygContext.type);
            if (intygsModule) {
                $scope.vm = {
                    title: intygsModule.label,
                    issuerTypeId: intygsModule.issuerTypeId,
                    moduleDescription: intygsModule.detailedDescription,
                    intygTypeVersion: $scope.config.intygContext.intygTypeVersion,
                    aboutMsgKey: $scope.config.intygContext.aboutMsgKey

                };
            } else {
                $log.error('ModuleService returned null for "' + $scope.config.intygContext.type + '"');
            }

        }
    };
} ]);
