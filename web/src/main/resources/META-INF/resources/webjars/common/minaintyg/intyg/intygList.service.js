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

angular.module('common').factory('common.IntygListService', ['$rootScope', '$http', '$log',
    function($rootScope, $http, $log) {
        'use strict';

        // cached certificates response
        var cachedArchiveList = null;

        var _selectedCertificate = null;

        function _emptyCache() {
            $log.debug('Clearing archived cache');
            cachedArchiveList = null;
        }

        function _getCertificates(callback) {
            $http.get('/api/certificates').success(function(data) {
                $log.debug('populating cache');
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                //give calling code a chance to handle error
                callback(null);
            });
        }

        function _getArchivedCertificates(callback) {
            if (cachedArchiveList !== null) {
                $log.debug('returning cached archive response');
                callback(cachedArchiveList);
                return;
            }
            $http.get('/api/certificates/archived').success(function(data) {
                $log.debug('populating archive cache');
                cachedArchiveList = data;
                callback(cachedArchiveList);
            }).error(function(data, status) {
                $log.error('error ' + status);
                //give calling code a chance to handle error
                callback(null);
            });
        }

        function _archiveCertificate(item, callback) {
            $log.debug('Archiving ' + item.id);

            $http.put('/api/certificates/' + item.id + '/archive').success(function(data) {
                _emptyCache();
                callback(data, item);
            }).error(function(data, status) {
                $log.error('error ' + status);
                //give calling code a chance to handle error
                callback(null);
            });
        }

        function _restoreCertificate(item, callback) {
            $log.debug('restoring ' + item.id);
            $http.put('/api/certificates/' + item.id + '/restore').success(function(data) {
                _emptyCache();
                callback(data, item);
            }).error(function(data, status) {
                $log.error('error ' + status);
                //give calling code a chance to handle error
                callback(null);
            });
        }

        function _getKnownRecipients(callback) {
            $log.debug('getting all available recipients');
            $http.get('/api/certificates/recipients/list').success(function(data) {
                $rootScope.$broadcast('recipients.updated');
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                callback(null);
            });
        }

        // Return public API for our service
        return {
            getCertificates: _getCertificates,
            getArchivedCertificates: _getArchivedCertificates,
            archiveCertificate: _archiveCertificate,
            restoreCertificate: _restoreCertificate,
            selectedCertificate: _selectedCertificate,
            emptyCache: _emptyCache,
            getKnownRecipients: _getKnownRecipients
        };
    }]);
