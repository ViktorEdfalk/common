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

angular
    .module('common')
    .directive('wcUtkastFooterVidarebefodra',
        ['common.dynamicLabelService', 'common.UtkastNotifyService', 'common.UtkastViewStateService', 'common.UtkastValidationViewState', 'common.UtkastFooterService',
            function(dynamicLabelService, UtkastNotifyService, CommonViewState, utkastValidationViewState, UtkastFooterService) {
                'use strict';

                return {
                    restrict: 'E',
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/wcUtkastFooterVidarebefodra/wcUtkastFooterVidarebefodra.directive.html',
                    scope: {
                        viewState: '=',
                        certForm: '<'
                    },
                    controller: function($scope) {
                        var viewState = $scope.viewState;

                        $scope.forwardBtnText = dynamicLabelService.getProperty('draft.notify.button');
                        $scope.forwardBtnTooltip = dynamicLabelService.getProperty('draft.notify.button.tooltip');

                        $scope.showMissing = {
                            value: false
                        };

                        /**
                         * Handle vidarebefordra dialog
                         */
                        $scope.vidarebefordraUtkast = function() {
                            UtkastNotifyService.notifyUtkast(viewState.intygModel.id, viewState.common.intyg.type,
                                viewState.draftModel, viewState.common);
                        };

                        $scope.onVidarebefordradChange = function() {
                            UtkastNotifyService.onNotifyChange(viewState.intygModel.id, viewState.common.intyg.type,
                                viewState.draftModel, viewState.common);
                        };

                        $scope.$watch('showMissing', function(newValue, oldValue) {
                            console.log('running showMissing');
                            if (newValue.value !== oldValue.value) {
                                if (newValue.value) {
                                    UtkastFooterService.checkMissing(viewState, $scope.certForm);
                                } else {
                                    CommonViewState.setShowComplete(false);
                                    utkastValidationViewState.reset();
                                }
                            }
                        }, true);

                        $scope.$watch(function() {
                            return utkastValidationViewState.sections && utkastValidationViewState.sections.length > 0;
                        }, function(newValue, oldValue) {
                            if (CommonViewState.showComplete && newValue !== oldValue) {
                                $scope.showMissing.value = newValue;
                            }
                        });

                        $scope.$on('$destroy', function() {
                           console.log('scope destroy');
                        });
                    }
                };
            } ]);
