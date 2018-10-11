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
angular.module('lisjp').controller('lisjp.CustomizePdfCtrl',
    [ '$window', '$location', '$log', '$rootScope', '$state', '$stateParams', '$scope','lisjp.customizeViewstate', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService',
        function($window, $location, $log, $rootScope, $state, $stateParams, $scope, customizeViewstate, listCertService, certificateService, dialogService, messageService) {
            'use strict';

            $scope.messageService = messageService;
            $scope.customizeViewstate = customizeViewstate;
            $scope.downloadAsPdfLink = '/moduleapi/certificate/lisjp/' + $stateParams.intygTypeVersion + '/' + $stateParams.certificateId + '/pdf/arbetsgivarutskrift';

            certificateService.getCertificate('lisjp', $stateParams.intygTypeVersion, $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                    //Tillaggsfragor are dynamic, so we create field config for any such things after cert has been loaded.
                    customizeViewstate.addTillaggsFragor($scope.cert.tillaggsfragor);
                } else {

                    // show error view
                    $location.path('/lisjp/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/lisjp/visafel/certnotfound');
            });

            var dialogInstance;
            var leaveToState, leaveToParams = null;
            var leaveConfirmed = false;
            // Cleanup ---------------
            $scope.$on('$destroy', function() {
                if (dialogInstance) {
                    dialogInstance.close();
                    dialogInstance = undefined;
                }
            });
            function _showLeaveConfirmationDialog() {
                dialogInstance = dialogService.showDialog($scope, {
                    dialogId: 'confirm-leave-customize-pdf-dialog',
                    titleId: 'modules.customize.summary.leave-dialog.header',
                    bodyTextId: 'modules.customize.summary.leave-dialog.body',
                    button1click: function() {
                        //perform navigation
                        dialogInstance.close();
                        leaveConfirmed = true;
                        $state.go(leaveToState, leaveToParams);
                    },
                    button1id: 'leave-button',
                    button1text: 'modules.customize.summary.leave-dialog.button.confirm',
                    button1icon: 'icon-ok',
                    button2text: 'modules.customize.summary.leave-dialog.button.cancel',
                    button2icon: 'icon-cancel',
                    button2class: 'btn-secondary',
                    autoClose: false
                });
            }
            $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams, options) {
                if (toState.name.indexOf('lisjp-customize')!==-1 || leaveConfirmed) {
                    return;
                }

               // defer this navigation until confirmed in dialog
                event.preventDefault();
                leaveToState = toState;
                leaveToParams = toParams;
                _showLeaveConfirmationDialog();
                //state history gets mixed when when preventing default. Avoid this by going to the current state.
                $state.go(fromState, fromParams);
            });


            $scope.backToViewCertificate = function() {
                customizeViewstate.resetModel();
                $location.path('/lisjp/view/' + $stateParams.certificateId);
            };




            //Fishbone state --------------------------------------
            $scope.stepModel =[
                {labelKey:'modules.customize.fishbone.step1', active: true},
                {labelKey:'modules.customize.fishbone.step2'},
                {labelKey:'modules.customize.fishbone.step3'}
            ];
            $scope.$on('$stateChangeSuccess', function(event, toState) {
                angular.forEach($scope.stepModel, function(step, index) {
                    step.active = (toState.data.index === index);
                });

            });


            //Download handling ------------------------
            function _executeDownload() {
                var inputs = '';
                var fields = customizeViewstate.getSendModel();

                angular.forEach(fields, function(item) {
                    inputs += _addInput('selectedOptionalFields', item);
                });

                //send request via temporary added form and remove from dom directly
                $window.jQuery('<form action="' + $scope.downloadAsPdfLink + '" target="_blank" method="post">' + inputs + '</form>')
                    .appendTo('body').submit().remove();

                $scope.downloadSuccess = true;
            }

            function _addInput(name, item) {
                return '<input type="hidden" name="' + name + '" value="' + item + '" />';
            }

            $scope.submit = function() {
                dialogService.showDialog($scope, {
                    dialogId: 'mi-downloadpdf-sekretess-dialog',
                    titleId: 'pdf.sekretessmarkeringmodal.header',
                    bodyTextId: 'pdf.sekretessmarkeringmodal.body',
                    button1click: function() {
                        _executeDownload();
                    },
                    button2click: function() {
                    },
                    button1id: 'close-fkdialog-logout-button',
                    button1text: 'pdf.sekretessmarkeringmodal.button1',
                    button2text: 'pdf.sekretessmarkeringmodal.button2',
                    button2visible: true,
                    autoClose: true
                });
            };

        }]);
