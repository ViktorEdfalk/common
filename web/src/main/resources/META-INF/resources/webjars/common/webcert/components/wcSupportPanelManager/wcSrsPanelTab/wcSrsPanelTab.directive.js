/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcSrsPanelTab',
    [ 'common.ObjectHelper', 'common.srsProxy', 'common.authorityService', '$stateParams',
        'common.srsService', 'common.srsViewState',
    function(ObjectHelper, srsProxy, authorityService, $stateParams,
             srsService, srsViewState) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsPanelTab.directive.html',
        link: function($scope, $element, $attrs) {
            $scope.srs = srsViewState;
            $scope.questionsFilledForVisaButton = function() {
                var answers = $scope.getSelectedAnswerOptions();
                for (var i = 0; i < answers.length; i++) {
                    if (!answers[i] || !answers[i].answerId) {
                        return false;
                    }
                }
                return true;
            };

            $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction = function() {
                var predictionIntygId = $scope.srs.userClientContext === 'SRS_FRL' ? $scope.srs.extensionFromIntygId : $scope.srs.intygId;
                return srsProxy.getAtgarderAndStatistikAndHistoricPredictionForDiagnosis(predictionIntygId, $scope.srs.personId,
                    srsViewState.diagnosKod)
                    .then(function(data) {
                        $scope.srs.statistik = data.statistik || 'error';
                        $scope.srs.atgarder = data.atgarder || 'error';

                        if (!$scope.srs.isForlangning || ($scope.srs.isForlangning && !$scope.srs.extensionPredictionFetched)) {
                            $scope.srs.prediction = data.prediktion || 'error';
                            $scope.srs.extensionPredictionFetched = true;
                        }

                        // Update the selected answers to the received stored answer
                        if ($scope.srs.prediction.predictionQuestionsResponses) {
                            $scope.srs.prediction.predictionQuestionsResponses.forEach(function(qnr) {
                                // Find correct question and answer option (in the scope) for received qnr
                                var correspondingQuestion = $scope.srs.questions.filter(function(q){
                                    return qnr.questionId===q.questionId;
                                })[0];
                                // Some prediction params like "Region" aren't reflected as questions in the gui so if we don't get a
                                // match, ignore that one
                                if (correspondingQuestion) {
                                    var storedAnswer = correspondingQuestion.answerOptions.filter(function (a) {
                                        return qnr.answerId === a.id;
                                    })[0];
                                    correspondingQuestion.model = storedAnswer;
                                }
                            });
                        }
                    }, function(error) {
                        $scope.srs.statistik = 'error';
                        $scope.srs.atgarder = 'error';
                        $scope.srs.prediction = 'error';
                    });
            };

            $scope.getQuestions = function(diagnosKod) {
                return srsProxy.getQuestions(diagnosKod).then(function(questions) {
                    $scope.srs.selectedButtons = [];
                    var qas = questions;
                    for (var i = 0; i < questions.length; i++) {
                        for (var e = 0; e < questions[i].answerOptions.length; e++) {
                            if (questions[i].answerOptions[e].defaultValue) {
                                qas[i].model = questions[i].answerOptions[e];
                            }
                        }
                    }
                    return qas;
                });
            };

            $scope.setConsent = function(consent) {
                $scope.srs.consent = consent ? 'JA' : 'NEJ';
                if (consent) {
                    srsProxy.logSrsConsentAnswered($scope.srs.userClientContext, $scope.srs.intygId,
                        $scope.srs.vardgivareHsaId, $scope.srs.hsaId);
                }
                srsProxy.setConsent($scope.srs.personId, $scope.srs.hsaId, consent);

                if ($scope.srs.consent === 'NEJ') {
                    var cleanPrediction = angular.copy($scope.srs.prediction);
                    cleanPrediction.probabilityOverLimit = null;
                    $scope.srs.prediction = cleanPrediction;
                }
            };

            $scope.setOpinion = function(opinion) {
                srsProxy.setOwnOpinion(opinion, $scope.srs.personId, $scope.srs.vardgivareHsaId, $scope.srs.hsaId,
                    $scope.srs.intygId, $scope.srs.prediction.predictionDiagnosisCode).then(function(result) {
                    $scope.srs.prediction.opinion = opinion;
                }, function(error) {
                    $scope.srs.prediction.opinionError = 'Fel när egen bedömning skulle sparas';
                });
            };

            $scope.logSrsPanelActivated = function() {
                if (!$scope.srs.activatedFirstTime) {
                    $scope.srs.activatedFirstTime = true;
                    srsProxy.logSrsPanelActivated($scope.srs.userClientContext, $scope.srs.intygId,
                        $scope.srs.vardgivareHsaId, $scope.srs.hsaId);
                }
            };

            /**
             * Starts the flow when the user enters the certificate editor
             * or the editor is reloaded.
             */
            $scope.$on('intyg.loaded', function(event, content) {
                    if (content.grundData.relation.relationKod === 'FRLANG') {
                        $scope.srs.isForlangning = true;
                        $scope.srs.extensionFromIntygId = content.grundData.relation.relationIntygsId;
                        $scope.srs.userClientContext = 'SRS_FRL';
                        $scope.srs.extensionPredictionFetched = false;
                    } else {
                        $scope.srs.isForlangning = false;
                        $scope.srs.userClientContext = 'SRS_UTK';
                    }
                    $scope.srs.intygId = $stateParams.certificateId;

                    if(!$scope.srs.diagnosisListFetching) {
                        loadDiagCodes();
                    }
                    $scope.srs.userHasSrsFeature = checkIfUserHasSrsFeature();
                    // INTYG-4543: Only use srs endpoints if user has srs-feature enabled.
                    if ($scope.srs.userHasSrsFeature) {
                        $scope.srs.consentError = '';
                        $scope.srs.consent = '';
                        srsProxy.getConsent($scope.srs.personId, $scope.srs.hsaId).then(function(consent) {
                            if(consent.status === 200){
                                $scope.srs.consent = consent.data;
                            }
                            else{
                                $scope.srs.consent = 'error';
                            }
                            setConsentMessages();
                        });

                        $scope.$watch('srs.originalDiagnosKod', function(newVal, oldVal) {
                            if (newVal === null) {
                                reset();
                            } else if (newVal !== oldVal) {
                                applySrsForDiagnosCode();
                            }
                        });

                        $scope.$watch('srs.consent', function(newVal, oldVal) {
                            if (newVal === 'JA' && srsViewState.diagnosKod) {
                                srsViewState.diagnosisListFetching.then(function() {
                                    loadSrs();
                                });
                            } else {
                                if (newVal === false && oldVal === true) {
                                    reset();
                                    $scope.srs.srsApplicable = isSrsApplicable($scope.srs.diagnosKod);
                                }
                            }
                        });
                    }
                }
            );

            /* jshint ignore:start */
            function debugLog() {
                var debugLogEnabled = false;
                if (debugLogEnabled) {
                    console.log.apply(null, arguments);
                }
            }
            /* jshint ignore:end */

            function applySrsForDiagnosCode() {
                resetMessages();
                reset();
                $scope.srs.diagnosKod = srsViewState.originalDiagnosKod;
                $scope.srs.diagnosisListFetching.then(function() {
                    $scope.srs.srsApplicable = isSrsApplicable($scope.srs.diagnosKod);
                    // TODO: Check if we remove patient data already otherwise replace this with
                    //  removing patient data if the consent is removed
                    if ($scope.srs.srsApplicable) {
                        if (!$scope.srs.shownFirstTime) { // TODO: Needs to be combined with intygsId if we shall have something like this
                            srsProxy.logSrsLoaded($scope.srs.userClientContext, $scope.srs.intygId,
                                $scope.srs.vardgivareHsaId, $scope.srs.hsaId, $scope.srs.diagnosKod);
                        }
                        $scope.srs.shownFirstTime = true; // TODO: do we need to make this survive a page reload?
                        loadSrs();
                    }
                });
            }

            function setConsentMessages() {
                if ($scope.srs.consent === 'error') {
                    $scope.srs.consentError = 'Tekniskt fel. Det gick inte att hämta information om samtycke.';
                }
            }

            function setAtgarderMessages() {
                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';
                if ($scope.srs.atgarder) {
                    if ($scope.srs.atgarder === 'error') {
                        $scope.srs.atgarderError = 'Tekniskt fel.\nDet gick inte att hämta information om åtgärder.';
                    }
                    else if ($scope.srs.atgarder.atgarderStatusCode === 'INFORMATION_SAKNAS') {
                        $scope.srs.atgarderInfo = 'Observera! För ' + $scope.srs.diagnosKod +
                            ' - ' + $scope.srs.diagnosBeskrivning + ' finns ingen SRS-information för detta fält.';
                    }
                    else if (!$scope.srs.atgarder.atgarderRek ||
                        ($scope.srs.atgarder.atgarderRek && $scope.srs.atgarder.atgarderRek.length < 1)) {
                        $scope.srs.atgarderInfo = 'Observera! För ' + srsViewState.diagnosKod +
                            ' finns ingen SRS-information för detta fält.';
                    }
                }
            }

            function setStatistikMessages() {
                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';
                if ($scope.srs.statistik) {
                    if ($scope.srs.statistik === 'error') {
                        $scope.srs.statistikError =
                            'Tekniskt fel.\nDet gick inte att hämta information om statistik';
                    }
                    else if ($scope.srs.statistik.statistikStatusCode === 'STATISTIK_SAKNAS' ||
                        !$scope.srs.statistik.nationellStatistik) {
                        $scope.srs.statistikInfo = 'Observera! För ' + srsViewState.diagnosKod +
                            ' finns ingen SRS-information för detta fält.';
                    }
                }
            }

            $scope.getSelectedAnswerOptions = function() {
                var selectedOptions = [];
                for (var i = 0; i < $scope.srs.questions.length; i++) {
                    selectedOptions.push(
                        {questionId: $scope.srs.questions[i].questionId, answerId: $scope.srs.questions[i].model.id});
                }
                return selectedOptions;
            };

            function checkIfUserHasSrsFeature() {
                var options = {
                    feature: 'SRS',
                    intygstyp: $scope.srs.intygsTyp
                };
                return authorityService.isAuthorityActive(options);
            }

            function isSrsApplicable() {
                if (($scope.srs.intygsTyp.toLowerCase().indexOf('fk7263') > -1 || $scope.srs.intygsTyp.toLowerCase().indexOf('lisjp') > -1) &&
                    isSrsApplicableForCode(srsViewState.diagnosKod)) {
                    return true;
                } else {
                    return false;
                }

                function isSrsApplicableForCode(diagnosKod) {
                    return diagnosKod !== undefined &&
                        diagnosKod !== null &&
                        $scope.srs.diagnosisCodes.indexOf(diagnosKod.substring(0, 3)) !== -1;
                }
            }

            function loadDiagCodes() {
                $scope.srs.diagnosisListFetching = srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                    $scope.srs.diagnosisCodes = diagnosisCodes;
                });
            }

            function loadSrs() {
                $scope.getQuestions($scope.srs.diagnosKod).then(function(questions) {
                    setConsentMessages();
                    $scope.srs.questions = questions;
                    $scope.srs.allQuestionsAnswered = $scope.questionsFilledForVisaButton();
                    $scope.srs.showVisaKnapp = $scope.srs.allQuestionsAnswered;
                    $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction().then(function () {
                        setAtgarderMessages();
                        setStatistikMessages();
                        $scope.setPrediktionMessages();
                        $scope.setPredictionRiskLevel();
                    });
                    $scope.setPrediktionMessages(); // No prediction data as of yet, only used to ensure initial correct state.
                });
            }

            $scope.setPrediktionMessages = function() {
                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';
                if ($scope.srs.prediction === 'error') {
                    $scope.srs.prediktionError =
                        'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                } else {
                    if ($scope.srs.prediction.statusCode === 'NOT_OK') {
                        $scope.srs.prediktionError =
                            'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                    }
                }
            };

            $scope.setPredictionRiskLevel = function() {
                if ($scope.srs.prediction.probabilityOverLimit && $scope.srs.prediction.probabilityOverLimit !== null) {
                    var probability = $scope.srs.prediction.probabilityOverLimit;
                    var riskLevel = null;
                    if (probability < 0.39) {
                        riskLevel = 'Måttlig risk';
                    } else if (probability >= 0.39 && probability <= 0.62) {
                        riskLevel = 'Hög risk';
                    } else if (probability > 0.62) {
                        riskLevel = 'Mycket hög risk';
                    }
                    $scope.srs.prediction.riskLevel = riskLevel;
                }
            };


            function resetMessages(){
                $scope.srs.consentError = '';

                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';

                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';

                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';
            }

            function reset() {
                // On forlangning we want to preserve the latest prediction
                if ($scope.srs.isForlangning) {
                    $scope.srs.statistik = {};
                    $scope.srs.atgarder = {};
                    $scope.srs.srsApplicable = false;
                    $scope.srs.activeTab = 'atgarder';
                } else {
                    $scope.srs.questions = [];
                    $scope.srs.statistik = {};
                    $scope.srs.atgarder = {};
                    $scope.srs.prediction = {};
                    $scope.srs.prediction.description = '';
                    $scope.srs.shownFirstTime = false;
                    $scope.srs.activatedFirstTime = false;
                    $scope.srs.srsApplicable = false;
                    $scope.srs.errorMessage = '';
                    $scope.srs.allQuestionsAnswered = false;
                    $scope.srs.showVisaKnapp = false;
                    $scope.srs.riskImage = '';
                    $scope.srs.activeTab = 'atgarder';
                }
            }

            $scope.$on('panel.activated', function(event, activatedPanelId) {
                if (activatedPanelId === 'wc-srs-panel-tab') {
                    $scope.logSrsPanelActivated();
                    if ($scope.config.isReadOnly) {
                        $scope.srs.isReadOnly = true;
                        $scope.srs.userClientContext = 'SRS_REH'; // Rehabstöd
                        $scope.srs.isForlangning = false;
                        $scope.srs.intygId = $scope.config.intygContext.id;
                        srsService.updatePersonnummer($scope.config.readOnlyIntyg.grundData.patient.personId);
                        srsService.updateHsaId($scope.config.readOnlyIntyg.grundData.skapadAv.vardenhet.enhetsid);
                        srsService.updateVardgivareHsaId($scope.config.readOnlyIntyg.grundData.skapadAv.vardenhet.vardgivare.vardgivarid);
                        srsService.updateIntygsTyp($scope.config.readOnlyIntyg.typ);
                        srsService.updateDiagnosKod($scope.config.diagnoseCode);
                        if(!$scope.srs.diagnosisListFetching) {
                            loadDiagCodes();
                        }
                        $scope.srs.userHasSrsFeature = checkIfUserHasSrsFeature();
                        if ($scope.srs.userHasSrsFeature) {
                            $scope.srs.consentError = '';
                            $scope.srs.consent = '';
                            srsProxy.getConsent($scope.srs.personId, $scope.srs.hsaId).then(function (consent) {
                                if (consent.status === 200) {
                                    $scope.srs.consent = consent.data;
                                } else {
                                    $scope.srs.consent = 'error';
                                }
                                setConsentMessages();
                            });
                            applySrsForDiagnosCode();
                        }
                    }

                }
            });

        }
    };
} ]);
