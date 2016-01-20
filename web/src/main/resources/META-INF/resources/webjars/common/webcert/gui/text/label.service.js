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

            if(rootElement === undefined || rootElement === null){
                rootElement = 'texter';
            }
            var value;
            if(stringEndsWith(key, structureTextTypesEnum.hjalp)) { // We check end here to determine text typ for 'HLP', its optional...
                value = getOptionalTextByPropKey(key, rootElement); // get optional, help text
            } else{
                value = getRequiredTextByPropKey(key, rootElement); // get required text
            }
            return value;
        }

        // get prop optional
        function getOptionalTextByPropKey(key, rootElement){

            var labelsList = _labelResources[rootElement].text; // this structure is for test only, structure might change later.
            // $log.info('Access test count: ' + labelsList.length);
            var match = _.findWhere(labelsList, { id: key } );
            var err
            if (match === null || match === undefined) {
                err = '[Missing optional Label: "' + key + '"]';  // log error when missing complete help object, help text or optional text?
                return err;
            } else if (match.text === '' || match.text === undefined || match.text === null) {
                err = '[Help text not found - have ID but not text for required id: "' + key + '"]';  // if for some reason we have an id and empty text, something is very wrong, return error string here as well.
                return err;
            }
            return match.text;
        }

        // get prop req
        function getRequiredTextByPropKey(key, rootElement){
            if(rootElement === undefined || rootElement === null){
                return;
            }
            var labelsList = _labelResources[rootElement].text; // this structure is for test only, structure might change later.
           // $log.info('Access test count: ' + labelsList.length);
            var match = _.findWhere(labelsList, { id: key } );
            var err;
            if (match === null || match === undefined) {
                err = '[MISSING TEXT ERROR - missing required id: "' + key + '"]';  // log error when missing id!
                return err;
            } else if (match.text === '' || match.text === undefined || match.text === null) {
                err = '[MISSING TEXT ERROR - have ID but not text for required id: "' + key + '"]';  // if for some reason we have an id and empty text, something is very wrong, return error string here as well.
                return err;
            }
            return match.text;
        }

        function _addLabels(labels) {
            _checkLabels();
            angular.extend(_labelResources.texter, labels.texter);

            tillaggsFragor = labels.tillaggsfragor;
        }

        function _getTillaggsFragor() {
            return tillaggsFragor;
        }

        function _convertTextIdToModelId(textObject)
        {
            return textObject.substr(4, 4);
        }

        function _updateTillaggsfragorToModel(tillaggsfragor, model) {
            for (var i = 0; i < tillaggsfragor.length; i++) {
                var modelFrageList = model.tillaggsfragor;
                if (i >= modelFrageList.length) {
                    var tillaggsfraga = {
                        'id': _convertTextIdToModelId(tillaggsfragor[i].id),
                        'svar': ''
                    }
                    modelFrageList.push(tillaggsfraga);
                } else {
                    var tillaggsfraga = {
                        'id': _convertTextIdToModelId(tillaggsfragor[i].id),
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
                _labelResources = {
                    'texter': {
                        'initial.key': 'Initial nyckel'
                    }
                };
            }
        }

        function stringStartsWith (string, prefix) {
            return string.slice(0, prefix.length) === prefix;
        }

        function stringEndsWith (string, suffix) {
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
]);
