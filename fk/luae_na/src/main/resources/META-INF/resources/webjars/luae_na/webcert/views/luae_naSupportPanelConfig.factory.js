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

/**
 * Creates the supportpanel config related to working with luae_na intyg and utkast
 *
 * Created by marced on 2018-01-16.
 */
angular.module('luae_na').factory('luae_na.supportPanelConfigFactory', [ 'common.featureService', function(featureService) {
    'use strict';

    function _getConfig(id, isSigned, isSent, isKompletteringsUtkast) {

        var config = {
            tabs: [],
            intygContext: {
                type: 'luae_na',
                aboutMsgKey: 'FRM_2.RBK',
                id: id,
                isSigned: isSigned,
                isSent: isSent
            }
        };


        if (featureService.isFeatureActive(featureService.features.HANTERA_FRAGOR, config.intygContext.type) && (isSigned || isKompletteringsUtkast)) {
            config.tabs.push({
                id: 'wc-arende-panel-tab',
                title: 'Frågor & Svar',
                config: {
                    intygContext: config.intygContext
                },
                active: isSigned || isKompletteringsUtkast
            });
        }

        //Always has this, but only active by default utkast and not kompletteringsutkast
        config.tabs.push({
            id: 'wc-help-tips-panel-tab',
            title: 'Tips & Hjälp',
            config: {
                intygContext: config.intygContext
            },
            active: !(isSigned || isKompletteringsUtkast)
        });


        return angular.copy(config);
    }

    return {
        getConfig: _getConfig
    };

} ]);
