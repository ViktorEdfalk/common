describe('dynamicLabelService', function() {
    'use strict';

    var dynamicLabelService;
    var $rootScope;

    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['common.dynamicLabelService', '$rootScope',
        function(_dynamicLabelService_, _$rootScope_) {
            dynamicLabelService = _dynamicLabelService_;
            $rootScope = _$rootScope_;
        }
    ]));


    describe('updateTillaggsfragorToModel', function() {

        /* jshint maxlen: false, unused: false */
        var dynamicLabelJson = {
            'texter': {
            },
            'tillaggsfragor': [
                {
                    'id': '9001',
                    'text': 'Blah',
                    'help': '9001',
                },
                {
                    'id': '9003',
                    'text': 'Blah',
                    'help': '9003',
                },
            ]
        };

        it('should add all fragor to model in order if model is empty', function() {
            var model = {
                tillaggsfragor: []
            }

            dynamicLabelService.updateTillaggsfragorToModel(dynamicLabelJson.tillaggsfragor, model);

            expect(model.tillaggsfragor.length).toBe(2);
            expect(model.tillaggsfragor[0].id).toBe('9001');
            expect(model.tillaggsfragor[1].id).toBe('9003');
        });

        it('should only add missing fragor to model in order if model already has values (insert)', function() {
            var model = {
                tillaggsfragor: [
                    {
                        id: '9003',
                        svar: 'yeehaw',
                    }
                ]
            }

            dynamicLabelService.updateTillaggsfragorToModel(dynamicLabelJson.tillaggsfragor, model);

            expect(model.tillaggsfragor.length).toBe(2);
            expect(model.tillaggsfragor[0].id).toBe('9001');
            expect(model.tillaggsfragor[1].id).toBe('9003');
        });

        it('should only add missing fragor to model in order if model already has values (push)', function() {
            var model = {
                tillaggsfragor: [
                    {
                        id: '9001',
                        svar: 'yeehaw',
                    }
                ]
            }

            dynamicLabelService.updateTillaggsfragorToModel(dynamicLabelJson.tillaggsfragor, model);

            expect(model.tillaggsfragor.length).toBe(2);
            expect(model.tillaggsfragor[0].id).toBe('9001');
            expect(model.tillaggsfragor[1].id).toBe('9003');
        });
    });

    describe('#getProperty', function() {
        var testDataDynamicLabelInline = {
            'texter': {
                    'KAT_2.RBK': '2. Intyg är baserat på'
            },
            'tillaggsfragor': [
                {
                    'id': 'TFG_1',
                    'text': 'TFG_1.RBK',
                    'help': 'TFG_1.HLP'
                }
            ]
        };

        beforeEach(function() {
            dynamicLabelService.addLabels(testDataDynamicLabelInline);
        });

        it('should return a question label string when given id', function() {
            var rootProp = 'texter'; // rootprop is only used by mock test in prototype, will not be present later
            var aProp = dynamicLabelService.getProperty('KAT_2.RBK', rootProp);
            expect(aProp).toEqual('2. Intyg är baserat på');
        });

        it('required text type should return a Error string when id is missing', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_999X.RBK', rootProp); // KAT_999X.RBK does not exist
            expect(aProp).toBeUndefined();
        });

        it('Optional text type shoud return a MISSING LABEL string when id is missing', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_999X.HLP', rootProp);
            expect(aProp).toBeUndefined();
        });

        /* it('should return an empty string if the message is an empty string', function() {
         // todo
         });

         it('should return the correct value if a language is set in the root scope', function() {
         // todo
         });

         it('should return the correct value if a default language is set', function() {
         // todo
         });

         it('should return the default value if the key is not present in the resources', function() {
         // todo
         });

         it('should return the missing key if the key is not present in the resources', function() {
         // todo
         });

         it('should return missing language if no language is set', function() {
         // todo
         });

         it('should return string with expanded string if variable available', function() {
         // todo
         }); */

    });
});
