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
angular.module('ts-tstrk1062').factory('ts-tstrk1062.viewConfigFactory.v1', [
    '$filter', 'uvUtil',
    function($filter, uvUtil) {
        'use strict';

        var viewConfig = [
            {
                type: 'uv-kategori',
                labelKey: 'KAT_1.RBK',
                components: [
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_1.RBK',
                        components: [{
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-list',
                                labelKey: 'KV_INTYGET_AVSER.{var}.RBK',
                                listKey: function(model) {
                                    return model.selected ? model.type : null;
                                },
                                separator: ', ',
                                modelProp: 'intygAvser.korkortstyp'
                            }]
                        }]
                    }
                ]
            },
            {
                type: 'uv-kategori',
                labelKey: 'KAT_2.RBK',
                components: [{
                    type: 'uv-fraga',
                    components: [{
                        type: 'uv-kodverk-value',
                        kvModelProps: ['idKontroll.typ'],
                        kvLabelKeys: ['IDENTITET_{var}.RBK']
                    }]
                }]
            },
            {
                type: 'uv-skapad-av',
                modelProp: 'grundData.skapadAv'
            }];

        return {
            getViewConfig: function(webcert) {
                var config = angular.copy(viewConfig);

                if (webcert) {
                    config = uvUtil.convertToWebcert(config);
                }

                return config;
            }
        };
    }]);
