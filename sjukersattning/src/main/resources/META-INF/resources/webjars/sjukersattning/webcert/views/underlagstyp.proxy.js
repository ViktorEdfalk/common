angular.module('sjukersattning').factory('sjukersattning.UnderlagTypProxy',
    [ '$q', '$http', '$stateParams', '$log', '$location', '$window', '$timeout', '$modal', '$cookieStore',
        'common.User', 'common.dialogService', 'common.featureService', 'common.messageService', 'common.statService',
        'common.UserModel',
        function($q, $http, $stateParams, $log, $location, $window, $timeout, $modal, $cookieStore, User, dialogService,
                 featureService, messageService, statService, UserModel) {
            'use strict';

            /**
             * createUtkast
             * @param onSuccess
             * @param onError
             * @private
             */


            /**
             * Load list of all certificates types
             */
            function _getUtkastTypes(onSuccess, onError) {
                var restPath = '/api/modules/NOTEXISTING'; // to be created by backend
               /* $http.get(restPath).success(function(data) {
                    $log.debug('got data:', data);
                    var sortValue = 0;
                    var types = [
                        { sortValue: sortValue++, id: 'default', label: messageService.getProperty('label.default-cert-type') }
                    ];
                    for (var i = 0; i < data.length; i++) {
                        var m = data[i];

                        // Only add type if feature is active and user has global intygTyp access through their role.
                        if (featureService.isFeatureActive(featureService.features.HANTERA_INTYGSUTKAST, m.id) && UserModel.hasIntygsTyp(m.id)) {
                            types.push({sortValue: sortValue++, id: m.id, label: m.label, fragaSvarAvailable: m.fragaSvarAvailable});
                        }
                    }
                    onSuccess(types);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    if (onError) {
                        onError();
                    }
                });*/
            }

            /**
             * Get intyg type data
             */
            function _getUtkastType(intygType, onSuccess) {

                _getUtkastTypes(function(types) {

                    var intygTypeMeta = {};
                    for (var i = 0; i < types.length; i++) {
                        if (types[i].id === intygType) {
                            intygTypeMeta = types[i];
                            break;
                        }
                    }

                    onSuccess(intygTypeMeta);
                });
            }

            /*
             * Load unsigned certificate list for valdVardenhet
             */
            function _getUtkastList(onSuccess, onError) {
                $log.debug('_getUtkastList:');
                var restPath = '/api/utkast/'; // release version
                $http.get(restPath).success(function(data) {
                    $log.debug('got data:' + data);
                    $log.info('got data in utkast.proxy.js:' + data);
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    // Let calling code handle the error of no data response
                    onError(null);
                });
            }

            /*
             * Load more unsigned certificates by query
             */
            function _getUtkastFetchMore(query, onSuccess, onError) {
                $log.debug('_getUtkastFetchMore');
                var restPath = '/api/utkast';
                $http.get(restPath, { params: query }).success(function(data) {
                    $log.debug('_getUnsignedCertificatesByQueryFetchMore got data:' + data);
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('_getUnsignedCertificatesByQueryFetchMore error ' + status);
                    // Let calling code handle the error of no data response
                    onError(data);
                });
            }

            function _getUtkastSavedByList(onSuccess, onError) {
                $log.debug('_getCertificateSavedByList');
                var restPath = '/api/utkast/lakare/';
                $http.get(restPath).success(function(data) {
                    $log.debug('_getCertificateSavedByList got data:' + data);
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('_getCertificateSavedByList error ' + status);
                    // Let calling code handle the error of no data response
                    onError(data);
                });
            }

            // Return public API for the service
            return {
                createUtkast: _createUtkast,
                getUtkastTypes: _getUtkastTypes,
                getUtkastType: _getUtkastType,
                getUtkastList: _getUtkastList,
                getUtkastFetchMore: _getUtkastFetchMore,
                getUtkastSavedByList: _getUtkastSavedByList
            };
        }]);
