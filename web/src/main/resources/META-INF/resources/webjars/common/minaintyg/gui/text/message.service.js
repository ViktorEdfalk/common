/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
 * message directive for externalizing text resources.
 *
 * All resourcekeys are expected to be defined in lowercase and available in a
 * global js object named "messages"
 * Also supports dynamic key values such as key="status.{{scopedvalue}}"
 *
 * Usage: <message key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('common').factory('common.messageService',
    function($rootScope) {
        'use strict';

        var _messageResources = null;
        var _links = null;

        function _propertyExists(key) {
            var value;
            var language = $rootScope.lang;
            if (language) {
                value = _getPropertyInLanguage(language, key, null);
                if (value === null || value === undefined) {
                    return false;
                }
            } else {
                value = false;
            }

            return value;
        }

        function _getProperty(key, params, defaultValue, language, fallbackToDefaultLanguage) {
            var value;

            if (!language) {
                language = $rootScope.lang;
                if (!language && fallbackToDefaultLanguage) {
                    language = $rootScope.DEFAULT_LANG;
                }
            }

            if (language) {
                value = _getPropertyInLanguage(language, key, params);
                if (value === null || value === undefined) {
                    value = defaultValue === null || defaultValue === undefined ?
                    '[Missing "' + key + '"]' : defaultValue;
                }
            } else {
                value = '[Missing language]';
            }

            return value;
        }

        function _getPropertyInLanguage(lang, key, params) {
            _checkResources();
            if(typeof key === 'string'){
                key = key.toLowerCase();
            } else {
                return null;
            }
            var message = _messageResources[lang][key];

            if (message && params) {
                message = _format(message, params);
            }

            // Find <LINK: dynamic links and replace
            var regex2 = /<LINK:(.*?)>/gi, result;

            while ( (result = regex2.exec(message)) ) {
                var replace = result[0];
                var linkKey = result[1];

                var dynamicLink = _buildDynamicLink(linkKey);

                var regexp = new RegExp(replace, 'g');
                message = message.replace(regexp, dynamicLink);
            }

            return message;
        }

        function _format(source, params) {
            angular.forEach(params,function (param, i) {
                source = source.replace(new RegExp('\\{' + i + '\\}', 'g'), param);
            });
            return source;
        }

        function _addResources(resources) {
            _checkResources();
            angular.extend(_messageResources.sv, resources.sv);
            angular.extend(_messageResources.en, resources.en);
        }

        function _checkResources() {
            if (_messageResources === null) {
                _messageResources = {
                    'sv': {
                        'initial.key': 'Initial nyckel'
                    },
                    'en': {
                        'initial.key': 'Initial key'
                    }
                };
            }
        }

        function _buildDynamicLink(linkKey) {
            var dynamicLink = '<a href="' + _links[linkKey].url + '" class="external-link"';
            dynamicLink += _links[linkKey].tooltip ? ' title="' + _links[linkKey].tooltip + '"' : '';
            dynamicLink += _links[linkKey].target ? ' target="' + _links[linkKey].target + '">' : '>';
            dynamicLink += _links[linkKey].text + '</a>';
            dynamicLink += _links[linkKey].target ? ' <i aria-hidden="true" class="icon icon-external_link"></i>' : '';
            return dynamicLink;
        }

        function _addLinks(links) {
            _links = links;
        }

        return {
            propertyExists: _propertyExists,
            getProperty: _getProperty,
            addResources: _addResources,
            addLinks: _addLinks
        };
    });
