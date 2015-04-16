angular.module('common').controller('common.IntygEditHeader',
    ['$rootScope', '$scope', '$log', '$anchorScroll', '$state', '$stateParams', '$location', '$q', '$window',
        'common.messageService', 'common.ManageCertView', 'common.CertificateService', 'common.statService',
        'common.featureService', 'common.dialogService', 'common.IntygEditViewStateService',
        function($rootScope, $scope, $log, $anchorScroll, $state, $stateParams, $location, $q, $window, messageService,
            ManageCertView, CertificateService, statService, featureService, dialogService, CommonViewState) {
            'use strict';

            /**
             * Toggle header part ('Dölj meny'-knapp)
             */
            $scope.toggleHeader = function() {
                CommonViewState.toggleCollapsedHeader();
            };

            /**
             * Toggle 'Visa vad som behöver kompletteras'
             */
            $scope.toggleShowComplete = function() {
                CommonViewState.toggleShowComplete();
                ManageCertView.save();

                var old = $location.hash();
                $location.hash('top');
                $anchorScroll();
                // reset to old to keep any additional routing logic from kicking in
                $location.hash(old);
            };

            $scope.save = function() {
                ManageCertView.save(false);
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                var bodyText = 'När du raderar utkastet tas det bort från Webcert.';
                var dialogModel = {
                    acceptprogressdone: false,
                    errormessageid: 'Error',
                    showerror: false
                };

                var draftDeleteDialog = {};
                draftDeleteDialog = dialogService.showDialog({
                    dialogId: 'confirm-draft-delete',
                    titleId: 'common.modal.label.discard_draft',
                    bodyText: bodyText,
                    button1id: 'confirm-draft-delete-button',
                    model: dialogModel,

                    button1click: function() {
                        $log.debug('delete draft ');
                        dialogModel.acceptprogressdone = false;
                        var back = function() {
                            $window.doneLoading = true;
                            $window.history.back();
                        };
                        CertificateService.discardDraft($stateParams.certificateId, CommonViewState.intyg.typ, function() {
                            dialogModel.acceptprogressdone = true;
                            statService.refreshStat(); // Update statistics to reflect change

                            if (featureService.isFeatureActive('franJournalsystem')) {
                                CommonViewState.deleted = true;
                                CommonViewState.error.activeErrorMessageKey = 'error';
                                draftDeleteDialog.close(back);
                            } else {
                                draftDeleteDialog.close(back);
                            }
                        }, function(error) {
                            dialogModel.acceptprogressdone = true;
                            if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                statService.refreshStat(); // Update statistics to reflect change
                                draftDeleteDialog.close(back);
                            } else {
                                dialogModel.showerror = true;
                                if (error === '') {
                                    dialogModel.errormessageid = 'common.error.cantconnect';
                                } else {
                                    dialogModel.errormessageid =
                                        ('common.error.' + error.errorCode).toLowerCase();
                                }
                            }
                        });
                    },
                    button1text: 'common.delete',
                    button2text: 'common.cancel',
                    autoClose: false
                });
            };

            /**
             * Print draft
             */
            $scope.print = function() {
                ManageCertView.printDraft( $scope.cert.id, CommonViewState.intyg.typ );
            };
        }
    ]
);
