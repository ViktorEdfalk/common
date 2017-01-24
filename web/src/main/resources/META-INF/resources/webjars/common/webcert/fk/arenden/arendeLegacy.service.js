angular.module('common').factory('common.ArendeLegacyService',
    function() {
        'use strict';

        function _convertSvarFragestallare(frageStallare) {
            if (frageStallare.toLowerCase() === 'fk') {
                return 'wc';
            }
            else if (frageStallare.toLowerCase() === 'wc') {
                return 'fk';
            }
        }

        var amneConvertMap = {
            'KOMPLETTERING_AV_LAKARINTYG': 'KOMPLT',
            'MAKULERING_AV_LAKARINTYG': 'MAKULERING_AV_LAKARINTYG',
            'AVSTAMNINGSMOTE': 'AVSTMN',
            'KONTAKT': 'KONTKT',
            'ARBETSTIDSFORLAGGNING': 'ARBETSTIDSFORLAGGNING',
            'PAMINNELSE': 'PAMINN',
            'OVRIGT': 'OVRIGT'
        };

        function _convertAmneArendeToFragasvar(amne) {
            for(var key in amneConvertMap) {
                if (amneConvertMap[key] === amne) {
                    return key;
                }
            }
            return null;
        }

        function _convertAmneFragasvarToArende(amne) {
            return amneConvertMap[amne];
        }

        function _convertFragasvarListToArendeList(list) {
            var converted = [];
            angular.forEach(list, function(fs) {
                var arende = _convertFragasvarToArende(fs);
                converted.push(arende);
            });
            return converted;
        }

        function _convertFragasvarToArende(fs) {
            var fragaSvar = fs.fragaSvar;

            var arende = null;

            if(fragaSvar) {
                arende = {
                    fraga: {
                        internReferens: fragaSvar.internReferens,
                        frageStallare: fragaSvar.frageStallare,
                        amne: _convertAmneFragasvarToArende(fragaSvar.amne),
                        meddelande: fragaSvar.frageText,
                        meddelandeRubrik: fragaSvar.meddelandeRubrik,
                        externaKontakter: fragaSvar.externaKontakter,
                        intygId: fragaSvar.intygsReferens.intygsId,
                        kompletteringar: fragaSvar.kompletteringar,
                        status: fragaSvar.status,
                        vidarebefordrad: fragaSvar.vidarebefordrad,
                        vardaktorNamn: fragaSvar.vardAktorNamn,
                        timestamp: fragaSvar.frageSkickadDatum
                    },
                    svar: {
                        internReferens: fragaSvar.internReferens,
                        frageStallare: _convertSvarFragestallare(fragaSvar.frageStallare),
                        amne: _convertAmneFragasvarToArende(fragaSvar.amne),
                        meddelande: fragaSvar.svarsText,
                        meddelandeRubrik: fragaSvar.meddelandeRubrik,
                        intygId: fragaSvar.intygsReferens.intygsId,
                        kompletteringar: fragaSvar.kompletteringar,
                        status: fragaSvar.status,
                        vidarebefordrad: fragaSvar.vidarebefordrad,
                        vardaktorNamn: fragaSvar.vardAktorNamn,
                        svarSkickadDatum: fragaSvar.svarSkickadDatum
                    },
                    answeredWithIntyg: fs.answeredWithIntyg,
                    senasteHandelseDatum: fragaSvar.senasteHandelseDatum,
                    paminnelser: []
                };
            }
            return arende;
        }

        return {
            convertAmneArendeToFragasvar: _convertAmneArendeToFragasvar,
            convertFragasvarListToArendeList: _convertFragasvarListToArendeList,
            convertFragasvarToArende: _convertFragasvarToArende
        };

    });
