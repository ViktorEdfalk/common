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
angular.module('common').run(['formlyConfig', 'common.dynamicLabelService', function(formlyConfig, dynamicLabelService) {
    'use strict';

    function _onLabelsUpdated(scope, options) {
        if (dynamicLabelService.hasProperty(scope.frageId + '.RBK')) {
            if (dynamicLabelService.hasProperty(options.templateOptions.label + '.RBK')) {
                scope.required = options.templateOptions.required;
                options.templateOptions.required = false;
                scope.showFrageLabel = true;
            } else {
                options.templateOptions.label = scope.frageId;
                options.templateOptions.bold = true;
            }
        }
    }

    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        var runOnTypes = ['checkbox-inline', 'radioGroup', 'checkGroup', 'date', 'sjukskrivningar'];
        if (runOnTypes.indexOf(options.type) >= 0) {
            if (options.templateOptions.label && options.templateOptions.label.substring(0, 4) === 'FRG_') {
                options.templateOptions.bold = true;
            }
            // Check labels on all delfraga with id x.1
            if (options.templateOptions.label && options.templateOptions.label.substring(0, 4) === 'DFR_') {
                var questionIds = options.templateOptions.label.substring(4).split('.');
                if (questionIds.length === 2 && questionIds[1] === '1') {
                    scope.showFrageLabel = false;
                    scope.frageId = 'FRG_' + questionIds[0];
                    scope.$on('dynamicLabels.updated', angular.bind(this, _onLabelsUpdated, scope, options));
                    return '<h4 ng-if="showFrageLabel">' +
                        '<span dynamic-label key="{{frageId}}.RBK"></span> {{required ? "*" : ""}}' +
                        '<span wc-help-chevron help-text-key="{{frageId}}.HLP"></span>' +
                        '</h4><span wc-help-chevron-text help-text-key="{{frageId}}.HLP"></span>' + template;
                }
            }
        }
        return template;
    });

    // Add an id to every question. This id is used to scroll to the question from arendePanelFraga.directive
    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        var key = options.key;
        if (options.templateOptions.id) {
            key = options.templateOptions.id;
        }
        if (key && $.type(key) === 'string') {
            key = key.replace(/\.|\[|\]/g, '_');
            return '<div id="form_' + key + '">' + template + '</div>';
        } else {
            return template;
        }
    });

}]);
