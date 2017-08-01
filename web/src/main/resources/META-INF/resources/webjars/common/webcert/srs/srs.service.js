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

angular.module('common').factory('common.srsService', [
    '$http' , '$q', '$log',
    'common.fmbViewState', 'common.srsProxy', 'common.ObjectHelper',
    function($http, $q, $log, fmbViewState, srsProxy, ObjectHelper) {
        'use strict';

        function _checkDiagnos(diagnos) {
            if (angular.isObject(diagnos) && !ObjectHelper.isEmpty(diagnos.diagnosKod) &&
                diagnos.hasInfo) {
                return true;
            }
            return false;
        }

        function _isAnySRSDataAvailable(srsStates) {
            if (angular.isObject(srsStates) && angular.isObject(srsStates.diagnoses)) {
                if (_checkDiagnos(srsStates.diagnoses[0]) ||
                    _checkDiagnos(srsStates.diagnoses[1]) ||
                    _checkDiagnos(srsStates.diagnoses[2])) {
                    return true;
                }
            }
            return false;
        }

        function _updateFmbTextsForAllDiagnoses(diagnoser) {
            fmbViewState.reset(0);
            fmbViewState.reset(1);
            fmbViewState.reset(2);

            if (!angular.isArray(diagnoser)) {
                $log.error('_updateFmbTextsForAllDiagnoses called with invalid parameter - array required');
                return false;
            }

            var diagnosTypes = [0, 1, 2];
            var fmbDiagnosRequest = [];
            var promises = [];

            // Request FMB texts for all entered diagnoses
            var i;
            for (i = 0; i < diagnoser.length; i++){
                if (diagnoser[i].diagnosKod) {
                    fmbDiagnosRequest.push({
                        type: diagnosTypes[i],
                        code: diagnoser[i].diagnosKod
                    });
                    promises.push(srsProxy.getSRSHelpTextsByCode(diagnoser[i].diagnosKod));
                }
            }

            // Resolve all server responses
            $q.all(promises).then(function(formDatas){
                var j;
                for(j = 0; j < formDatas.length; j++){
                    fmbViewState.setState(fmbDiagnosRequest[j].type, formDatas[j], fmbDiagnosRequest[j].code);
                }
            }, function(errors) {
                var j;
                for(j = 0; j < errors.length; j++){
                    $log.debug('Error searching fmb help text for diagnostype ' + fmbDiagnosRequest[j].type + ' with diagnoscode: ' + fmbDiagnosRequest[j].code);
                    fmbViewState.reset(fmbDiagnosRequest[j].type);
                }
            });

            return true;
        }

        function _updateFmbText(diagnosType, originalDiagnosKod) {
            if (!ObjectHelper.isDefined(originalDiagnosKod) || originalDiagnosKod.length === 0) {
                fmbViewState.reset(diagnosType);
                return false;
            } else {
                if (!angular.isObject(fmbViewState.diagnoses[diagnosType]) ||
                    fmbViewState.diagnoses[diagnosType].diagnosKod !== originalDiagnosKod) {
                    var fmbSuccess = function fmbSuccess(formData) {
                        fmbViewState.setState(diagnosType, formData, originalDiagnosKod);
                    };
                    var fmbReject = function fmbReject(data) {
                        $log.debug('Error searching fmb help text for diagnostype ' + diagnosType);
                        $log.debug(data);
                    };
                    srsProxy.getSRSHelpTextsByCode(originalDiagnosKod).then(fmbSuccess, fmbReject);
                }
            }
            return true;
        }

        // Return public API for the service
        return {
            checkDiagnos: _checkDiagnos,
            isAnySRSDataAvailable: _isAnySRSDataAvailable,
            updateFmbTextsForAllDiagnoses: _updateFmbTextsForAllDiagnoses,
            updateFmbText: _updateFmbText
        };
    }]);
