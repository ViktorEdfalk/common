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
    [ '$log', '$rootScope', 'common.DynamicLabelProxy',
        function($log, $rootScope, DynamicLabelProxy) {
            'use strict';

            var _labelResources = null;
            var tillaggsFragor = null;

            function _hasProperty(key) {
                var text = _labelResources[key];
                if (typeof text === 'undefined' || text === '') {
                    return false;
                }
                return true;
            }

            function _getProperty(key) {
                var value = getRequiredTextByPropKey(key); // get required text
                return value;
            }

            // get prop req
            function getRequiredTextByPropKey(key) {
                if (_labelResources === null) {
                    return '';
                }

                var text = _labelResources[key];
                if (typeof text === 'undefined') {
                    $log.debug('[MISSING TEXT ERROR - missing required id: "' + key + '"]');
                } else if (text === '') {
                    $log.debug('[MISSING TEXT ERROR - have ID but not text for required id: "' + key + '"]');
                }

                return text;
            }

            function _clearLabels() {
                _labelResources = {};
            }

            function _addLabels(labels) {
                _checkLabels();
                angular.extend(_labelResources, labels.texter);

                tillaggsFragor = labels.tillaggsfragor;
            }

            function _getTillaggsFragor() {
                return tillaggsFragor;
            }

            function _updateTillaggsfragorToModel(tillaggsfragor, model) {
                var modelFrageList = model.tillaggsfragor;

                if (!modelFrageList) {
                    return;
                }

                for (var i = 0; i < tillaggsfragor.length; i++) {
                    var tillaggsfraga = null;
                    if (i >= modelFrageList.length) {
                        tillaggsfraga = {
                            'id': tillaggsfragor[i].id,
                            'svar': ''
                        };
                        modelFrageList.push(tillaggsfraga);
                    } else {
                        tillaggsfraga = {
                            'id': tillaggsfragor[i].id,
                            'svar': ''
                        };
                        if (modelFrageList[i].id !== tillaggsfraga.id) {
                            modelFrageList.splice(i, 0, tillaggsfraga);
                        }
                    }
                }

                var textHasFraga = function(id) {
                    for (var i = 0; i < tillaggsfragor.length; i++) {
                        if (tillaggsfragor[i].id === id) {
                            return true;
                        }
                    }
                    return false;
                };
                for(var j = 0; j < modelFrageList.length; j++) {
                    if (!textHasFraga(modelFrageList[j].id)) {
                        modelFrageList.splice(j, 1);
                        j--;
                    }
                }
            }

            function _checkLabels() {
                if (_labelResources === null) { // extend to local storage here
                    _labelResources = {};
                }
            }

            function _updateDynamicLabels(intygsTyp, model) {
                if (model.textVersion) {
                    DynamicLabelProxy.getDynamicLabels(intygsTyp, model.textVersion).then(
                        function(dynamicLabelJson) {
                            if (dynamicLabelJson !== null && typeof dynamicLabelJson !== 'undefined') {
                                $log.debug(dynamicLabelJson);
                                _clearLabels();
                                _addLabels(dynamicLabelJson);
                                _updateTillaggsfragorToModel(dynamicLabelJson.tillaggsfragor, model);
                            } else {
                                $log.debug('No dynamic text for intygType: ' + intygsTyp);
                            }
                            $rootScope.$broadcast('dynamicLabels.updated');
                        },
                        function(error) {
                            $log.debug('error:' + error);
                        });
                } else {
                    //This intygstype does not use dynamic texts, so let's empty any cached labels
                    _labelResources = null;
                    $rootScope.$broadcast('dynamicLabels.updated');
                }
            }
            return {
                checkLabels: _checkLabels,
                getProperty: _getProperty,
                hasProperty: _hasProperty,
                getTillaggsFragor: _getTillaggsFragor,
                updateDynamicLabels: _updateDynamicLabels
            };
        }
    ]);
