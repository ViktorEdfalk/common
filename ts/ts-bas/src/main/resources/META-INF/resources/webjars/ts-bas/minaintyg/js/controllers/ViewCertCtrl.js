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

angular.module('ts-bas').controller('ts-bas.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'common.moduleService', 'ts-bas.viewConfigFactory',
        function($location, $log, $rootScope, $stateParams, $scope, IntygListService, IntygService,
            dialogService, messageService, moduleService, viewConfigFactory) {
            'use strict';

            $scope.certificateId = $stateParams.certificateId;
            $scope.cert = undefined;
            $scope.certMeta = null;
            $scope.messageService = messageService;

            $scope.send = function() {
                $location.path('/send/ts-bas/' + $stateParams.certificateId + '/TRANSP');
            };

            $scope.errorMessage = null;
            $scope.doneLoading = false;
            IntygService.getCertificate('ts-bas', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                    $scope.errorMessage = null;
                } else {
                    // show error view
                    $scope.errorMessage = 'error.certnotfound';
                }
            }, function(errorMsgKey) {
                $scope.doneLoading = true;
                $log.debug('getCertificate got error ' + errorMsgKey);
                $scope.errorMessage = errorMsgKey;
            });

            $scope.uvConfig = viewConfigFactory.getViewConfig();

        }]);
