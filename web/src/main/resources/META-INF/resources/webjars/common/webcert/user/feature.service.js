angular.module('common').factory('common.featureService',
    [ 'common.UserModel', function(UserModel) {
        'use strict';

        function _isFeatureActive(feature, intygstyp) {
            if (!feature) {
                return false;
            }

            var activeFeatures = UserModel.getActiveFeatures();
            if (!activeFeatures || !activeFeatures.length) {
                return false;
            }

            if (activeFeatures.indexOf(feature) === -1) {
                return false;
            }

            if (intygstyp && activeFeatures.indexOf(feature + '.' + intygstyp) === -1) {
                return false;
            }

            return true;
        }

        return {
            features: {
                HANTERA_FRAGOR: 'hanteraFragor',
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast',
                KOPIERA_INTYG: 'kopieraIntyg',
                MAKULERA_INTYG: 'makuleraIntyg',
                SKICKA_INTYG: 'skickaIntyg',
                ARBETSGIVARUTSKRIFT: 'arbetsgivarUtskrift',
                JS_LOGGNING: 'jsLoggning',
                JS_MINIFIED: 'jsMinified'
            },
            isFeatureActive: _isFeatureActive
        };
    }]);
