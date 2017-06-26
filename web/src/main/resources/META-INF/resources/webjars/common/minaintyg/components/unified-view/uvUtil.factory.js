angular.module('common').factory('uvUtil', [
    '$parse', '$log', 'common.dynamicLabelService', 'common.messageService',
    function($parse, $log, dynamicLabelService, messageService) {
    'use strict';

        function _isDynamicText(str) {
            return _isString(str) && str.search(/{(.*)}/) > -1;
        }

        function _isString(str) {
            return str instanceof String || typeof str === 'string';
        }

        function _replacer(propertyName, model) {
            function replacer(match, p1, offset, string) {
                return model[p1];
            }
            return propertyName.replace(/{(.*)}/g, replacer);
        }

        return {

        getTextFromConfig: function(value){
            if(value === ''){
                return value;
            }
            // Generate value from dynamic label if it existed, fallback to messageservice,
            // otherwise assume supplied value is what we want already and let it fall through
            var dynamicLabel = dynamicLabelService.getProperty(value);
            if(angular.isDefined(dynamicLabel) && dynamicLabel !== ''){
                return dynamicLabel;
            } else {
                if(messageService.propertyExists(value)){
                    var staticLabel = messageService.getProperty(value);
                    return staticLabel;
                } else {
                    return value;
                }
            }
        },
        getValue: function(obj, pathExpression) {

            // Process each element of an array into a new array, otherwise just the expression
            if(Array.isArray(pathExpression)){
                var list = [];

                pathExpression.forEach(function(item){
                    list.push($parse(item)(obj));
                });

                return list;
            }

            return $parse(pathExpression)(obj);
        },
        resolveValue: function(prop, modelRow, colProp, rowIndex, colIndex){
            var value = null;
            if(typeof prop === 'function'){
                // Resolve using function
                value = prop(modelRow, rowIndex, colIndex, colProp);
            } else if(_isDynamicText(prop)) {
                // This prop is a string with a dynamic text.
                // Property should be on format <prefix>.{property-name}.<suffix>
                // Example: KV_FKMU_0001.{typ}.RBK => KV_FKMU_0001.ANHORIG.RBK
                value = _replacer(prop, modelRow);
            } else if(prop.indexOf('.') !== -1) {
                value = this.getValue(modelRow, prop);
            } else if(modelRow.hasOwnProperty(prop)) {
                // Resolve using property name
                value = modelRow[prop];
            }
            return value;
        },
        isValidValue: function(value) {

            if (angular.isNumber(value)) {
                return true;
            }

            if (angular.isString(value)) {
                return value.length > 0;
            }

            if (angular.isArray(value)) {
                return value.length > 0;
            }

            if (angular.isDefined(value) && angular.isObject(value)) {
                return true;
            }

            if (value === true || value === false) {
                return true;
            }

            return false;

        }
    };
} ]);
