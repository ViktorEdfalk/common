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

fdescribe('fmbService', function() {
    'use strict';

    var $rootScope;
    var fmbService;
    var fmbViewState;
    var fmbProxy;
    var $q;

    beforeEach(angular.mock.module('common', function($provide) {
        /*        $provide.value('common.dialogService',
         jasmine.createSpyObj('common.dialogService', [ 'showErrorMessageDialog' ]));
         $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));*/
        //$provide.value('common.fmbViewState', { setState: {} });
    }));

    beforeEach(angular.mock.inject(['$rootScope', '$q', 'common.fmbService', 'common.fmbViewState', 'common.fmbProxy',
        function(_$rootScope_, _$q_, _fmbService_, _fmbViewState_, _fmbProxy_) {
            $q = _$q_;
            $rootScope = _$rootScope_;
            fmbService = _fmbService_;
            fmbViewState = _fmbViewState_;
            fmbProxy = _fmbProxy_;
        }]));

    describe('checkDiagnos', function() {
        it('should return false with invalid input', function() {
            var diagnos = null;
            expect(fmbService.checkDiagnos(diagnos)).toBeFalsy();

            diagnos = {};
            expect(fmbService.checkDiagnos(diagnos)).toBeFalsy();

            diagnos = {
                diagnosKod: ''
            };
            expect(fmbService.checkDiagnos(diagnos)).toBeFalsy();

            diagnos = {
                diagnosKod: 'J22',
                hasInfo: false
            };
            expect(fmbService.checkDiagnos(diagnos)).toBeFalsy();
        });

        it('should return true with valid input', function() {
            var diagnos = {
                diagnosKod: 'J22',
                diagnosBeskrivning: 'Akut bronkit är inte kul.',
                formData: {
                    DIAGNOS: {
                        heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                        text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                    }
                },
                hasInfo: true
            };
            expect(fmbService.checkDiagnos(diagnos)).toBeTruthy();
        });
    });

    describe('updateFmbTextsForAllDiagnoses negative', function() {

        beforeEach(function() {
            spyOn(fmbViewState, 'setState').and.callThrough();
            spyOn(fmbViewState, 'reset').and.callThrough();
            spyOn(fmbProxy, 'getFMBHelpTextsByCode').and.callFake(function() {
                var promise = $q.defer();
                promise.reject({});
                return promise.promise;
            });
        });

        it('should return false on invalid input', function() {
            var diagnoser = null;
            var result = fmbService.updateFmbTextsForAllDiagnoses(diagnoser);
            $rootScope.$apply();

            expect(fmbViewState.reset).toHaveBeenCalled();
            expect(result).toBeFalsy();
        });

        it('should return false on empty array', function() {
            var diagnoser = [];
            var result = fmbService.updateFmbTextsForAllDiagnoses(diagnoser);
            $rootScope.$apply();

            expect(result).toBeTruthy();
            expect(fmbViewState.setState).not.toHaveBeenCalled();
        });

        it('should call reset and not setState on empty server response', function() {
            var diagnoser = {
                0: {
                    diagnosKod: 'J22',
                    diagnosBeskrivning: 'Akut bronkit är inte kul.',
                    formData: {
                        DIAGNOS: {
                            heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                            text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                        }
                    },
                    hasInfo: true
                },
                1: {
                    diagnosKod: 'M118',
                    diagnosBeskrivning: 'Akut bronkit är inte kul.',
                    formData: {
                        DIAGNOS: {
                            heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                            text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                        }
                    },
                    hasInfo: true
                },
                2: {
                    diagnosKod: 'H27',
                    diagnosBeskrivning: 'Akut bronkit är inte kul.',
                    formData: {
                        DIAGNOS: {
                            heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                            text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                        }
                    },
                    hasInfo: true
                }
            };
            var result = fmbService.updateFmbTextsForAllDiagnoses(diagnoser);
            $rootScope.$apply();

            expect(result).toBeFalsy();
            expect(fmbViewState.setState).not.toHaveBeenCalled();
            expect(fmbViewState.reset).toHaveBeenCalled();
        });
    });

    describe('updateFmbTextsForAllDiagnoses positive', function() {
        beforeEach(function() {
            spyOn(fmbViewState, 'setState').and.callThrough();
            spyOn(fmbViewState, 'reset').and.callThrough();
            spyOn(fmbProxy, 'getFMBHelpTextsByCode').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve([{0:{}}, {1:{}}, {2:{}}]);
                return promise.promise;
            });
        });

        it('should set fmb state from available diagnoses', function() {

            var diagnoser = {
                0: {
                    diagnosKod: 'J22',
                    diagnosBeskrivning: 'Akut bronkit är inte kul.',
                    formData: {
                        DIAGNOS: {
                            heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                            text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                        }
                    },
                    hasInfo: true
                },
                1: {
                    diagnosKod: 'M118',
                    diagnosBeskrivning: 'Akut bronkit är inte kul.',
                    formData: {
                        DIAGNOS: {
                            heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                            text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                        }
                    },
                    hasInfo: true
                },
                2: {
                    diagnosKod: 'H27',
                    diagnosBeskrivning: 'Akut bronkit är inte kul.',
                    formData: {
                        DIAGNOS: {
                            heading: 'SYMPTOM_PROGNOS_BEHANDLING',
                            text: 'Akut bronkit orsakas vanligen av luftvägsinflammation och ...'
                        }
                    },
                    hasInfo: true
                }
            };

            var result = fmbService.updateFmbTextsForAllDiagnoses(diagnoser);

            $rootScope.$apply();

            expect(result).toBeFalsy();
            expect(fmbViewState.setState).toHaveBeenCalled();
        });
    });

});
