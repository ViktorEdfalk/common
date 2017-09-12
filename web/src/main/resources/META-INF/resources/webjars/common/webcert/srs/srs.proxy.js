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

angular.module('common').factory('common.srsProxy', ['$http', '$q', '$log',
function ($http, $q, $log) {
    'use strict';

    function _setConsent(consentGiven) {
        return "you have " + (!consentGiven ? "not " : "") + "accepeted";
    }

    function _getSrs(intygsId, patientId, diagnosKod, qaIds, prediktion, atgard, statistik) {
        return $http.post('/api/srs/' + intygsId + '/' + patientId + '/' + diagnosKod + '?prediktion=' + prediktion + '&atgard=' + atgard + '&statistik=' + statistik, qaIds).then(function (response) {
            return response.data;
        });
    }

    function _getDiagnosisCodes() {
        return $http.get('/api/srs/codes').then(function (response) {
            return response.data;
        });
    }

    function _setConsent(patientId, hsaId, consentGiven) {
        return $http.put('/api/srs/consent/' + patientId + '/' + hsaId, consentGiven).then(function (response) {
            return response.data;
        });
    }

    function _getConsent(personId, hsaId) {
        return $http.get('/api/srs/consent/' + personId + '/' + hsaId).then(function (response) {
            return response.data;
        });
    }

    function _getFeatures() {
        return $http.get('/api/anvandare').then(function (response) {
            return response.data;
        });
    }

    function _getQuestions(diagnosKod) {
        return $http.get('/api/srs/questions/' + diagnosKod).then(function (response) {
            return response.data;
        });
    }

    // Return public API for the service
    return {
        getConsent: _getConsent,
        getDiagnosisCodes: _getDiagnosisCodes,
        getFeatures: _getFeatures,
        getQuestions: _getQuestions,
        getSrs: _getSrs,
        setConsent: _setConsent,
    };
}]);
