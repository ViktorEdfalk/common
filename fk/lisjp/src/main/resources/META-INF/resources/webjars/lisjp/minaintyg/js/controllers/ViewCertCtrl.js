angular.module('lisjp').controller('lisjp.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'common.moduleService', 'lisjp.customizeViewstate',
        function($location, $log, $rootScope, $stateParams, $scope, listCertService, certificateService, dialogService,
            messageService, moduleService, customizeViewstate) {
            'use strict';

            $scope.cert = {};
            $rootScope.cert = {};

            $scope.messageService = messageService;
            $scope.moduleService = moduleService;

            $scope.doneLoading = false;

            $scope.send = function() {
                $location.path('/send/lisjp/' + $stateParams.certificateId + '/FK');
            };

            $scope.visibleStatuses = [ 'SENT' ];

            $scope.dialog = {
                acceptprogressdone: true,
                focus: false
            };

            var archiveDialog = {};

            $scope.archiveSelected = function() {
                var item = $scope.cert;
                $log.debug('archive ' + item.id);
                $scope.dialog.acceptprogressdone = false;
                listCertService.archiveCertificate(item, function(fromServer, oldItem) {
                    $log.debug('statusUpdate callback:' + fromServer);
                    if (fromServer !== null) {
                        // Better way to update the object?
                        oldItem.archived = fromServer.archived;
                        oldItem.status = fromServer.status;
                        oldItem.selected = false;
                        archiveDialog.close();
                        $scope.dialog.acceptprogressdone = true;
                        $location.path('#/start');
                    } else {
                        // show error view
                        $location.path('/lisjp/fel/couldnotarchivecert');
                    }
                });
            };

            // Archive dialog
            $scope.certToArchive = {};

            $scope.openArchiveDialog = function(cert) {
                $scope.certToArchive = cert;
                $scope.dialog.focus = true;
                archiveDialog = dialogService.showDialog($scope, {
                    dialogId: 'archive-confirmation-dialog',
                    titleId: 'inbox.archivemodal.header',
                    bodyTextId: 'inbox.archivemodal.text',
                    button1click: function() {
                        $log.debug('archive');
                        $scope.archiveSelected();
                    },
                    button1id: 'archive-button',
                    button1text: 'button.archive',
                    autoClose: false
                });
            };

            $scope.filterStatuses = function(statuses) {
                var result = [];
                if (!angular.isObject(statuses)) {
                    return result;
                }
                for (var i = 0; i < statuses.length; i++) {
                    if ($scope.userVisibleStatusFilter(statuses[i])) {
                        result.push(statuses[i]);
                    }
                }
                return result;
            };

            $scope.userVisibleStatusFilter = function(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.showStatusHistory = function() {
                $location.path('/lisjp/statushistory');
            };

            $scope.backToViewCertificate = function() {
                $location.path('/lisjp/view/' + $stateParams.certificateId);
            };

            $scope.customizeCertificate = function() {
                customizeViewstate.resetModel();
                $location.path('/lisjp/customize/' + $stateParams.certificateId);
            };

            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + 'lisjp' + '/' + $stateParams.certificateId + '/pdf';

            certificateService.getCertificate('lisjp', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $log.info('res in virecertcontr' + JSON.stringify(result));
                    $scope.cert = result.utlatande;
                    $scope.cert.filteredStatuses = $scope.filterStatuses(result.meta.statuses);
                    $rootScope.cert = $scope.cert;
                } else {
                    // show error view
                    $location.path('/lisjp/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/lisjp/visafel/certnotfound');
            });

            $scope.pagefocus = true;
        }]);
