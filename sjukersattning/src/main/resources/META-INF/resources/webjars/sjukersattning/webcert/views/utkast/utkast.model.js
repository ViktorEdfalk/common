angular.module('sjukpenning').factory('sjukpenning.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
            'use strict';

            // --- Transform functions
            var nedsattFromTransform = function(nedsatt) {
                if (!nedsatt || (!nedsatt.from && !nedsatt.tom)) {
                    return undefined;
                } else {
                    return nedsatt;
                }
            };
            var sjukpenningModel = BaseAtticModel._extend({
                init: function init() {
                    //console.log('--- init fk gd' + GrundData);
                    var grundData = GrundData.build();
                    init._super.call(this, 'sjukpenningModel', {

                        form1: [new ModelAttr('avstangningSmittskydd', {defaultValue: false})],

                        form2: ['journaluppgifter',
                            'telefonkontaktMedPatienten',
                            'undersokningAvPatienten'],

                        form3: ['arbetsloshet',
                            'foraldraledighet',
                            'studier',
                            'arbetsmarknadsProgram',
                            'nuvarandeArbete',
                            'nuvarandeArbetsuppgifter'],

                        form4: ['diagnosBeskrivning1',
                            'diagnosBeskrivning2',
                            'diagnosBeskrivning3',
                            'diagnosKod1',
                            'diagnosKod2',
                            'diagnosKod3',
                            'diagnosKodsystem1',
                            'diagnosKodsystem2',
                            'diagnosKodsystem3',
                            new ModelAttr('samsjuklighet', {defaultValue: false})],

                        form5: ['funktionsnedsattning'],

                        form6: ['aktivitetsbegransning'],

                        form7: ['pagaendeBehandling',
                            'planeradBehandling'],

                        form8b: ['tjanstgoringstid',
                            'ressattTillArbeteAktuellt', 'rekommendationOverSocialstyrelsensBeslutsstod',
                            new ModelAttr('nedsattMed100', {fromTransform: nedsattFromTransform}),
                            new ModelAttr('nedsattMed25', {fromTransform: nedsattFromTransform}),
                            'nedsattMed25Beskrivning',
                            new ModelAttr('nedsattMed50', {fromTransform: nedsattFromTransform}),
                            'nedsattMed50Beskrivning',
                            new ModelAttr('nedsattMed75', {fromTransform: nedsattFromTransform}),
                            'nedsattMed75Beskrivning', 'vadPatientenKanGora', 'prognosNarPatientKanAterga'],

                        form9: ['atgardInteAktuellt', 'atgardArbetstraning', 'atgardArbetsanpassning',
                            'atgardSokaNyttArbete', 'atgardBesokPaArbete', 'atgardErgonomi',
                            'atgardHjalpmedel', 'atgardKonflikthantering', 'atgardOmfordelning',
                            'atgardForetagshalsovard', 'atgardOvrigt'],

                        form10: ['kommentar'],

                        form11: ['kontaktMedFk'],

                        misc: ['forskrivarkodOchArbetsplatskod',
                                'namnfortydligandeOchAdress', 'id',
                                new ModelAttr('grundData', {defaultValue: grundData})]

                    });

                },

                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }

            }, {
                build : function(){
                    //console.log('----- build *****' + DraftModel + ',' + sjukpenningModel._members);
                    return new DraftModel(new sjukpenningModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return sjukpenningModel;

        }]);
