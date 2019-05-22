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

/* globals Highcharts */
angular.module('common').directive('wcSrsRiskDiagram',
    [ 'common.srsViewState', 'common.wcSrsChartFactory', '$timeout',
        function(srsViewState, chartFactory, $timeout) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    // config: '='
                },
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsRiskDiagram.directive.html',
                link: function($scope, $element, $attrs) {
                    $scope.srs = srsViewState;

                    // TODO: Flytta till där webcert-modulen definieras på samma sätt som i statistik:app.run.js? eller boot-app.jsp?
                    Highcharts.seriesTypes.line.prototype.drawLegendSymbol = Highcharts.seriesTypes.area.prototype.drawLegendSymbol;
                    Highcharts.setOptions({
                        lang: { thousandsSep: ' ' }
                    });


                    var riskChart = {};
                    var chartData = {
                        risk: {
                            chartData: [
                                {
                                    name: 'Genomsnittlig risk',
                                    type: 'RISK',
                                    y: 32
                                },
                                {
                                    name: '',
                                    type: 'RISK',
                                    y: 0
                                }
                            ]
                        }
                    };

                    var setTooltipText = function (result) {
                        $scope.popoverTextRiskChart = 'Diagrammet visar risk för ... .' +
                            '<br><br>Ställ markören i respektive stapel för att se respektive riskvärde.';
                    };

                    var updateCharts = function (result) {
                        chartFactory.addColor(result.risk.chartData);
                        riskChart = paintBarChart('riskChart', result.risk.chartData);
                    };

                    var dataReceivedSuccess = function(result) {
                        setTooltipText(result);
                        $scope.statisticNotDone = false;
                        $scope.doneLoading = true;
                        $timeout(function() {
                            updateCharts(result);
                        }, 1);
                    };

                    function paintBarChart(containerId, chartData) {
                        var series = [
                            {
                                name: 'Risk',
                                data: chartData,
                                color: chartFactory.getColors().risk
                            }
                        ];
                        var categories = chartData.map(function (e) {
                            return {name: e.name};
                        });

                        var chartConfigOptions = {
                            categories: categories,
                            series: series,
                            type: 'column',
                            renderTo: containerId,
                            unit: '%',
                            maxWidthPercentage: 80,
                            marginRight: 120
                        };

                        var chartOptions = chartFactory.getHighChartConfigBase(chartConfigOptions);
                        chartOptions.chart.height = 240;
                        chartOptions.chart.plotBorderWidth = 0;
                        chartOptions.subtitle.text = null;
                        chartOptions.yAxis[0].tickInterval = 20;
                        chartOptions.yAxis[0].max = 100;
                        chartOptions.yAxis[0].gridLineWidth=0;
                        chartOptions.yAxis[0].lineWidth = 1;
                        chartOptions.yAxis[0].lineColor = '#c7c7c7';
                        chartOptions.yAxis[0].alternateGridColor = true;
                        chartOptions.legend.enabled = false;
                        chartOptions.yAxis[0].plotLines = [
                            {
                                color: '#C7C7C7',
                                width: 1,
                                value: 39
                            },
                            {
                                color: '#C7C7C7',
                                width: 1,
                                value: 62
                            },
                            {
                                color: '#C7C7C7',
                                width: 1,
                                value: 100
                            }
                        ];

                        chartOptions.yAxis[0].plotBands = [{
                            color: 'white',
                            from: 0,
                            to: 39,
                            label: {
                                text:'Måttlig risk',
                                align: 'right',
                                rotation: -15,
                                textAlign: 'left'
                            }
                        },
                            {
                                color: 'white',
                                from: 39,
                                to: 62,
                                label: {
                                    text:'Hög risk',
                                    align: 'right',
                                    rotation: -15,
                                    textAlign: 'left'
                                }
                            },
                            {
                                color: 'white',
                                from: 62,
                                to: 100,
                                label: {
                                    text:'Mycket hög risk',
                                    align: 'right',
                                    rotation: -15,
                                    textAlign: 'left'
                                }
                            }
                        ];

                        return new Highcharts.Chart(chartOptions);
                    }

                    $scope.$on('$destroy', function() {
                        if(riskChart && typeof riskChart.destroy === 'function') {
                            riskChart.destroy();
                        }
                    });

                    $scope.$watch('srs.prediction', function(newVal, oldVal) {
                        if (newVal.prevalence !== null) {
                            chartData.risk.chartData[0].y = Math.round(newVal.prevalence * 100);
                            chartData.risk.chartData[0].name = 'Genomsnittlig risk';
                        } else {
                            chartData.risk.chartData[0].y = 0;
                            chartData.risk.chartData[0].name = '';
                        }
                        if (newVal.probabilityOverLimit !== null) {
                            chartData.risk.chartData[1].y = Math.round(newVal.probabilityOverLimit * 100);
                            chartData.risk.chartData[1].name = 'Patientens risk';
                        } else {
                            chartData.risk.chartData[1].y = null;
                            chartData.risk.chartData[1].name = '';
                        }
                        updateCharts(chartData);
                    });

                    // Kick start rendering
                    $timeout(function () {
                        dataReceivedSuccess(chartData);
                    });

                }
        };
} ]);