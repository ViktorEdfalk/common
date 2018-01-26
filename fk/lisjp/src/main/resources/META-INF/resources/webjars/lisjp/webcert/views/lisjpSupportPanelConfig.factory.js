/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

/**
 * Creates the supportpanel config related to working with lisjp intyg and utkast
 *
 * Created by marced on 2018-01-16.
 */
angular.module('lisjp').factory('lisjp.supportPanelConfigFactory', [ 'common.featureService', function(featureService) {
    'use strict';

    function _getConfig(id, isSigned, isSent, hasKomplettering) {

        var config = {
            tabs: [],
            intygContext: {
                type: 'lisjp',
                id: id,
                isSigned: isSigned,
                isSent: isSent,
                hasKomplettering: hasKomplettering
            }
        };

        if (featureService.isFeatureActive(featureService.features.HANTERA_FRAGOR, config.intygContext.type) && (isSigned || hasKomplettering)) {
            config.tabs.push({
                id: 'wc-arende-panel-tab',
                title: 'Frågor & Svar',
                config: {
                    intygContext: config.intygContext
                }
            });
        }

        if (!isSigned) {
            config.tabs.push({
                id: 'wc-fmb-panel-tab',
                title: 'FMB',
                config: {
                    intygContext: config.intygContext
                }
            });
        }

        //Always has this
        config.tabs.push({
            id: 'wc-help-tips-panel-tab',
            title: 'Tips & Hjälp',
            config: {
                tipsText: 'Hello world!',
                intygContext: config.intygContext
            }
        });

        // First tab of those added should be active by default
        config.tabs[0].active = true;
        return angular.copy(config);
    }

    return {
        getConfig: _getConfig
    };

} ]);