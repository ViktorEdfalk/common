angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$log', '$rootScope', '$routeParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate', 'common.User',
        function($log, $rootScope, $routeParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, User) {
            'use strict';

            // Check if the user used the special qa-link to get here.
            if ($routeParams.qaOnly) {
                $scope.isQaOnly = true;
            }

            // Page setup
            $scope.user = { lakare: User.userContext.lakare };

            $scope.cert = {};
            $scope.cert.filledAlways = true;
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                showTemplate: true,
                printStatus: 'notloaded',
                newPatientId: false
            };

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            /**
             * Private
             */
            function loadCertificate() {
                $log.debug('Loading certificate ' + $routeParams.certificateId);
                CertificateService.getCertificate($routeParams.certificateId, 'fk7263', function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null && result !== '') {
                        $scope.cert = result.contents;
                        $rootScope.$emit('fk7263.ViewCertCtrl.load', result.metaData, result.contents);
                        $rootScope.$broadcast('intyg.loaded', $scope.cert);

                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.metaData.statuses, 'FK');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.metaData.statuses);
                        if ($scope.certProperties.isRevoked) {
                            $scope.widgetState.printStatus = 'revoked';
                        } else {
                            $scope.widgetState.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/fk7263/' + $scope.cert.id + '/pdf';

                    } else {
                        $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
                    }
                    $scope.intygBackup.showBackupInfo = false;
                }, function(error) {
                    $scope.widgetState.doneLoading = true;
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        $scope.widgetState.activeErrorMessageKey = 'error.data_not_found';
                    } else {
                        $scope.widgetState.activeErrorMessageKey = 'error.could_not_load_cert';
                    }
                    $log.debug('Got error while loading cert');
                    $log.debug(error.message);
                    $scope.intygBackup.showBackupInfo = true;
                });
            }

            loadCertificate();

            /**
             * Event response from QACtrl which sends its intyg-info here in case intyg couldn't be loaded but QA info could.
             * @type {{}}
             */
            $scope.intygBackup = {intyg: null, showBackupInfo: false};
            var unbindFastEventFail = $rootScope.$on('fk7263.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            /**
             * Exposed functions
             * @param cert
             */
            ManageCertificate.initSend($scope);
            $scope.send = function(cert) {
                cert.intygType = 'fk7263';
                ManageCertificate.send($scope, cert, 'FK', 'fk7263.label.send', function() {
                    loadCertificate();
                });
            };

            ManageCertificate.initMakulera($scope);
            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty('fk7263.label.makulera.confirmation', {
                    namn: cert.intygMetadata.patient.fullstandigtNamn,
                    personnummer: cert.intygMetadata.patient.personId
                });
                cert.intygType = 'fk7263';
                ManageCertificate.makulera($scope, cert, confirmationMessage, function() {
                    loadCertificate();
                });
            };

            ManageCertificate.initCopyDialog($scope);
            $scope.copy = function(cert) {
                cert.intygType = 'fk7263';
                ManageCertificate.copy($scope, cert);
            };

            $scope.print = function(cert) {

                if ($scope.certProperties.isRevoked) {
                    ManageCertView.printDraft(cert.id, 'fk7263');
                } else {
                    document.pdfForm.submit();
                }
            };
        }]);
