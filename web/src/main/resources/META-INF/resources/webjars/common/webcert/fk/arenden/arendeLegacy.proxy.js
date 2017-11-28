angular.module('common').factory('common.ArendeLegacyProxy', ['$http', '$log', 'common.ArendeLegacyService',
    function($http, $log, ArendeLegacyService) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getArenden(intygsId, intygsTyp, onSuccess, onError) {

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.get(restPath).then(function(response) {
                $log.debug('got data:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarViewListToArendeList(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * save new question
         */
        function _sendNewArende(intygsId, intygsTyp, question, onSuccess, onError) {
            var payload = {};
            payload.amne = ArendeLegacyService.convertAmneArendeToFragasvar(question.chosenTopic.value);
            payload.frageText = question.frageText;

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.post(restPath, payload).then(function(response) {
                $log.debug('got callback data:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * save new answer to a question
         */
        function _saveAnswer(ArendeSvar, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + ArendeSvar.internReferens + '/besvara';
            $http.put(restPath, ArendeSvar.meddelande).then(function(response) {
                $log.debug('got data:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }


        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAsHandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/stang';
            $http.get(restPath).then(function(response) {
                $log.debug('got data:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAllAsHandled(arenden, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/stang';
            var fs = [];
            angular.forEach(arenden, function(arendeListItem) {
                this.push({ intygsTyp : intygsTyp, fragaSvarId:arendeListItem.arende.fraga.internReferens });
            }, fs);

            $http.put(restPath, fs).then(function(response) {
                $log.debug('got data:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarListToArendeList(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * update the handled status to unhandled ('ANSWERED or PENDING_EXTERNAL_ACTION depending if the question has an
         * answer set or not') of a QuestionAnswer
         */
        function _openAsUnhandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: fragaSvarId:' + fragaSvarId + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/oppna';
            $http.get(restPath).then(function(response) {
                $log.debug('got data:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * Toggle vidarebefordrad state of a fragasvar entity with given id
         */
        function _setVidarebefordradState(fragaSvarId, intygsTyp, isVidareBefordrad, callback) {
            $log.debug('_setVidareBefordradState');
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/hanterad';
            $http.put(restPath, {'dispatched' : isVidareBefordrad}).then(function(response) {
                $log.debug('_setVidareBefordradState data:' + response.data);
                callback(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                callback(null);
            });
        }

        return {
            getArenden: _getArenden,
            sendNewArende: _sendNewArende,
            saveAnswer: _saveAnswer,
            closeAsHandled: _closeAsHandled,
            openAsUnhandled: _openAsUnhandled,
            closeAllAsHandled: _closeAllAsHandled,
            setVidarebefordradState: _setVidarebefordradState
        };

    }]);
