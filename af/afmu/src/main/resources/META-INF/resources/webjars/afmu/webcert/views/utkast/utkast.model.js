/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('afmu').factory('afmu.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform, ObjectHelper) {
            'use strict';

            var AfmuModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'afmuModel', {

                        'id': undefined,
                        'typ': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 1
                        'harFunktionsnedsattning': undefined,
                        'funktionsnedsattning': undefined,

                        // Kategori 2
                        'harAktivitetsbegransning': undefined,
                        'aktivitetsbegransning': undefined,

                        // Kategori 3
                        'harUtredningBehandling': undefined,
                        'utredningBehandling': undefined,

                        // Kategori 4
                        'harArbetetsPaverkan': undefined,
                        'arbetetsPaverkan': undefined,

                        // Kategori 5
                        'ovrigt': undefined
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
                    return new DraftModel(new AfmuModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return AfmuModel;

        }]);