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

angular.module('common').controller(
        'wcChangeActiveUnitDialogCtrl',
        [ '$scope', '$uibModalInstance', '$window', '$state', '$location', '$cookies', 'common.User', 'common.UserModel', 'stats', 'vardgivare', 'valdEnhet',
                function($scope, $uibModalInstance, $window, $state, $location, $cookies, User, UserModel, stats, vardgivare, valdEnhet) {
                    'use strict';

                    //TODO: This controllers code was just extracted from the old monolithic wcHeader.controller.
                    //A lot of room for improvements, like extracting all "find" methods to a util class (not depending on scope variables),
                    // also maybe not necessary to provide a resolve of vardgivare, valdvardenhet, stats etc from calling code?
                    // (+ found a bug: if first round of stats havent been fetched when this dialog is opened, we get exceptions in the
                    // findStats(vg, id) method as it doesnt handle the case of not finding vg in stat.vardgivare array etc.)

                    $scope.user = UserModel.user;
                    $scope.stat = stats;
                    $scope.vardgivare = vardgivare;
                    //make sure path to any selected vardenhet is expanded
                    expandPath($scope.vardgivare, valdEnhet);

                    $scope.error = false;

                    $scope.close = function() {
                        $uibModalInstance.close();
                    };

                    function findVardgivareById(vg) {
                        for (var i = 0; i < $scope.stat.vardgivare.length; i++) {
                            if (vg === $scope.stat.vardgivare[i].id) {
                                return $scope.stat.vardgivare[i];
                            }
                        }
                        return null;
                    }

                    /**
                     * Finds the stat of the enhet or mottagning in the flat
                     * structure returned from server
                     */
                    function findStats(vg, id) {
                        var vardgivare = findVardgivareById(vg);
                        var vardenheter = vardgivare.vardenheter;
                        for (var j = 0; j < vardenheter.length; j++) {
                            var vardenhet = vardenheter[j];
                            if (vardenhet.id === id) {
                                return {
                                    intyg: vardenhet.intyg,
                                    fragaSvar: vardenhet.fragaSvar
                                };
                            }
                        }
                        return {
                            intyg: 0,
                            fragaSvar: 0
                        };
                    }

                    function findUserVardgivare(vgId) {
                        var vgs = $scope.user.vardgivare;
                        for (var i = 0; i < vgs.length; i++) {
                            if (vgs[i].id === vgId) {
                                return vgs[i];
                            }
                        }

                        return null;
                    }

                    /**
                     * Finds the mottagningar at the vardgivare with id `vg` and
                     * enhet with id `id`
                     */
                    function findMottagningar(vgId, id) {
                        var vardgivare = findUserVardgivare(vgId);
                        var enheter = vardgivare.vardenheter;
                        for (var j = 0; j < enheter.length; j++) {
                            if (enheter[j].id === id) {
                                return enheter[j].mottagningar;
                            }
                        }
                        return undefined;
                    }

                    /**
                     * Finds the total returned from `func` on all mottagningar
                     * at the enhet specified by vg and id
                     */
                    function findAll(vg, id, func) {
                        var total = 0;
                        var mottagningar = findMottagningar(vg, id) || [];

                        for (var i = 0; i < mottagningar.length; i++) {
                            total = total + func(vg, mottagningar[i].id);
                        }

                        return total;
                    }

                    function expandPath(vardgivare, valdEnhet) {
                        var currentVE = null;
                        if (valdEnhet) {
                            angular.forEach(vardgivare, function(vg) {
                                angular.forEach(vg.vardenheter, function(ve) {
                                    currentVE = ve;
                                    angular.forEach(ve.mottagningar, function(ue) {
                                        if (ue.id === valdEnhet.id) {
                                            //UE level selected, make sure it's parent VE is expanded
                                            // so that this UE is visible
                                            currentVE.showMottagning = true;
                                        }
                                    });

                                });

                            });
                        }
                    }

                    /**
                     * Toggles the value to show or hide the
                     * mottagningar connected to the vardenhet
                     */
                    $scope.toggle = function(enhet) {
                        enhet.showMottagning = !enhet.showMottagning;
                    };

                    $scope.findIntyg = function(vg, id) {
                        return findStats(vg, id).intyg;
                    };

                    $scope.findFragaSvar = function(vg, id) {
                        return findStats(vg, id).fragaSvar;
                    };

                    $scope.findAllFragaSvar = function(vg, id) {
                        return findAll(vg, id, $scope.findFragaSvar);
                    };

                    $scope.findAllIntyg = function(vg, id) {
                        return findAll(vg, id, $scope.findIntyg);
                    };

                    $scope.isCurrentlySelected = function(id) {
                        return (valdEnhet && valdEnhet.id === id);
                    };

                    /******************
                     * End of presentation functions
                     ******************/

                    $scope.selectVardenhet = function(enhet) {
                        $scope.error = false;
                        User.setValdVardenhet(enhet, function() {
                            // Remove stored cookie for selected filter. We want to choose a new
                            // filter after choosing another unit to work on
                            $cookies.remove('enhetsId');

                            $uibModalInstance.close();

                            // We updated the user context. Reroute to start page so as not to end
                            // up on a page we aren't welcome anymore. Maybe we should make these
                            // routes some kind of global configuration? No other choices are
                            // relevant today though.
                            if (UserModel.user.isLakareOrPrivat) {
                                $location.path('/');
                            } else {
                                $location.path('/unhandled-qa');
                            }
                            $state.reload();
                        }, function() {
                            $scope.error = true;
                        });

                    };
                } ]);
