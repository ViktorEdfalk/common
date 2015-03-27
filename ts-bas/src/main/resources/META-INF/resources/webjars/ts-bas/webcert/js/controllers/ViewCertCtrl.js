angular.module('ts-bas').controller('ts-bas.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$cookieStore', 'common.CertificateService',
        'common.ManageCertView', 'common.messageService', 'webcert.ManageCertificate','common.User',
        'common.IntygCopyRequestModel',
        function($log, $rootScope, $stateParams, $scope, $cookieStore, CertificateService, ManageCertView,
            messageService, ManageCertificate, User, IntygCopyRequestModel) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/
            
            var intygType = 'ts-bas';

            $scope.user = { lakare: User.getUserContext().lakare };
            $scope.cert = {};
            $scope.widgetState = {
                doneLoading: false,
                activeErrorMessageKey: null,
                showTemplate: true,
                printStatus: 'notloaded'
            };

            $scope.view = {
                intygAvser: '',
                bedomning: ''
            };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/intyg/ts-bas/' + $stateParams.certificateId + '/pdf';

            // Decide if helptext related to field 1.a) - 1.c)
            $scope.achelptext = false;

            $scope.certProperties = {
                isSent: false,
                isRevoked: false
            };

            /*********************************************************************
             * Private support functions
             *********************************************************************/

            function createKorkortstypListString(list) {

                var tempList = [];
                angular.forEach(list, function(key) {
                    if (key.selected) {
                        this.push(key);
                    }
                }, tempList);

                var resultString = '';
                for (var i = 0; i < tempList.length; i++) {
                    if (i < tempList.length - 1) {
                        resultString += tempList[i].type + (', ');
                    } else {
                        resultString += tempList[i].type;
                    }
                }

                return resultString;
            }

            function loadCertificate() {
                CertificateService.getCertificate($stateParams.certificateId, intygType, function(result) {
                    $scope.widgetState.doneLoading = true;
                    if (result !== null) {
                        $scope.cert = result.contents;
                        if ($scope.cert.syn.synfaltsdefekter === true || $scope.cert.syn.nattblindhet === true ||
                            $scope.cert.syn.progressivOgonsjukdom === true) {
                            $scope.achelptext = true;
                        }
                        $scope.view.intygAvser = createKorkortstypListString($scope.cert.intygAvser.korkortstyp);
                        $scope.view.bedomning = createKorkortstypListString($scope.cert.bedomning.korkortstyp);

                        $rootScope.$emit('ts-bas.ViewCertCtrl.load', result);
                        $scope.certProperties.isSent = ManageCertView.isSentToTarget(result.statuses, 'TS');
                        $scope.certProperties.isRevoked = ManageCertView.isRevoked(result.statuses);
                        if($scope.certProperties.isRevoked) {
                            $scope.widgetState.printStatus = 'revoked';
                        } else {
                            $scope.widgetState.printStatus = 'signed';
                        }

                        $scope.pdfUrl = '/moduleapi/intyg/ts-bas/' + $scope.cert.id + '/pdf';

                    } else {
                        $log.debug('Got error while loading cert - invalid data');
                        $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    $scope.widgetState.doneLoading = true;
                    $log.debug('Got error while loading cert: ' + error.message);
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                    } else {
                        $scope.widgetState.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                });
            }
            loadCertificate();

            /*********************************************************************
             * Exposed scope interaction functions
             *********************************************************************/

            $scope.send = function(cert) {
                cert.intygType = 'ts-bas';
                ManageCertificate.send( cert, 'TS', 'ts-bas.label.send', function() {
                        loadCertificate();
                    });
            };

            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty('ts-bas.label.makulera.confirmation', {
                    namn: cert.grundData.patient.fullstandigtNamn, personnummer: cert.grundData.patient.personId });

                cert.intygType = 'ts-bas';
                ManageCertificate.makulera( cert, confirmationMessage, function() {
                    loadCertificate();
                });
            };

            $scope.copy = function(cert) {

                if (cert === undefined || cert.grundData === undefined) {
                    $log.debug('cert or cert.grundData is undefined. Aborting copy.');
                    return;
                }

                var isOtherCareUnit = User.getValdVardenhet() !== cert.grundData.skapadAv.vardenhet.enhetsid;

                ManageCertificate.copy($scope,
                    IntygCopyRequestModel.build({
                        intygId: cert.id,
                        intygType: intygType,
                        patientPersonnummer: cert.grundData.patient.personId,
                        nyttPatientPersonnummer: $stateParams.patientId
                    }),
                    isOtherCareUnit);
            };

            $scope.print = function(cert) {

                if ($scope.certProperties.isRevoked) {
                    ManageCertView.printDraft(cert.id, intygType);
                } else {
                    document.pdfForm.submit();
                }
            };

			$scope.$on('loadCertificate', loadCertificate);
        }]);
