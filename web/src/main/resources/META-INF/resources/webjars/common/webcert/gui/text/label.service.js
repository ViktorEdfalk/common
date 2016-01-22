/**
 * message directive for externalizing text resources.
 *
 * All resourcekeys are expected to be defined in lowercase and available in a
 * global js object named "messages"
 * Also supports dynamic key values such as key="status.{{scopedvalue}}"
 *
 * Usage: <dynamicLabel key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('common').factory('common.dynamicLabelService',
    [ '$log', '$rootScope',
        function($log, $rootScope) {
            'use strict';

            var _labelResources = null;
            var structureTypesEnum = { kategori: 'KAT', fraga: 'FRG', delFraga: 'DFR' };
            var structureTextTypesEnum = { rubrik: 'RBK', hjalp: 'HLP' };
            var tillaggsFragor = null;

            function _getProperty(key, rootElement) {

                if (rootElement === undefined || rootElement === null) {
                    rootElement = 'texter';
                }
                var value = getRequiredTextByPropKey(key, rootElement); // get required text
                return value;
            }

            // get prop req
            function getRequiredTextByPropKey(key, rootElement) {
                if (rootElement === undefined || rootElement === null) {
                    return;
                }

                var textFound = true;
                var text = _labelResources[key];
                if (typeof text === 'undefined') {
                    text = '[MISSING TEXT ERROR - missing required id: "' + key + '"]';
                    textFound = false;
                } else if (text === '') {
                    text = '[MISSING TEXT ERROR - have ID but not text for required id: "' + key + '"]';
                    textFound = false;
                }

                if(!textFound && typeof tillaggsFragor !== 'undefined')
                {
                    // Check if its a tillaggsfraga
                    for (var i = 0; i < tillaggsFragor.length; i++) {
                        if (tillaggsFragor[i].id == _convertTextIdToModelId(key)) {
                            text = tillaggsFragor[i].text;
                            break;
                        }
                    }
                }

                return text;
            }

            function _addLabels(labels) {
                _checkLabels();
                angular.extend(_labelResources, labels.texter);

                tillaggsFragor = labels.tillaggsfragor;
            }

            function _getTillaggsFragor() {
                return tillaggsFragor;
            }

            function _convertTextIdToModelId(textObject) {
                return textObject.substr(4, 4);
            }

            function _updateTillaggsfragorToModel(tillaggsfragor, model) {
                for (var i = 0; i < tillaggsfragor.length; i++) {
                    var modelFrageList = model.tillaggsfragor;
                    if (i >= modelFrageList.length) {
                        var tillaggsfraga = {
                            'id': tillaggsfragor[i].id,
                            'svar': ''
                        }
                        modelFrageList.push(tillaggsfraga);
                    } else {
                        var tillaggsfraga = {
                            'id': tillaggsfragor[i].id,
                            'svar': ''
                        }
                        if (modelFrageList[i].id != tillaggsfraga.id) {
                            modelFrageList.splice(i, 0, tillaggsfraga);
                        }
                    }
                }
            }

            function _checkLabels() {
                if (_labelResources === null) { // extend to local storage here
                    _labelResources = {};
                }
            }

            function stringStartsWith(string, prefix) {
                return string.slice(0, prefix.length) === prefix;
            }

            function stringEndsWith(string, suffix) {
                return suffix === '' || string.slice(-suffix.length) === suffix;
            }

            return {
                checkLabels: _checkLabels,
                updateTillaggsfragorToModel: _updateTillaggsfragorToModel,
                getProperty: _getProperty,
                addLabels: _addLabels,
                getTillaggsFragor: _getTillaggsFragor
            };
        }
    ])
;
