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

describe('wcFmbHelpDisplay', function () {
    'use strict';

    beforeEach(angular.mock.module('common'));
    beforeEach(angular.mock.module('htmlTemplates'));

    var element, outerScope;

    beforeEach(inject(function ($rootScope, $compile) {
        element = angular.element('<wc-fmb-help-display related-form-id="SOMEFORM" diagnosis-code="fmb.diagnosKod" '+
            'diagnosis-description="fmb.diagnosBeskrivning" original-diagnosis-code="fmb.originalDiagnoskod" help-text-contents="fmb.formData.FORM"></wc-fmb-help-display>');
        outerScope = $rootScope;
        $compile(element)(outerScope);

        outerScope.$digest(); //digest the outerscope before the innerScope is called

        element.isolateScope(); //This will get the isolate scope
    }));

    function setUpFMBData(data) {
        outerScope.$apply(function () {
            outerScope.fmb = data;
        });
    }

    describe('correct headlines', function () {
        beforeEach(function() {
            setUpFMBData({
                formData: {},
                diagnosKod: 'J22',
                diagnosBeskrivning: 'Akut bronkit'
            });
        });

        it('should have a static headline set', function() {
           expect(element.find('#fmb_static_heading_SOMEFORM').first().text()).toEqual('Information från Socialstyrelsens försäkringsmedicinska beslutsstöd');
        });
        it('should have a diagnosis headline set', function() {
            expect(element.find('#fmb_diagnos_heading_SOMEFORM').first().text()).toEqual('J22 - Akut bronkit');
        });
    });

    describe('text are set for forms', function () {
        beforeEach(function() {
            setUpFMBData({
                formData: {FORM: [
                    {heading: 'SYMPTOM_PROGNOS_BEHANDLING', text: 'Akut bronkit'},
                    {heading: 'GENERELL_INFO', list: ['BulletText1', 'BulletText2', 'BulletText3']}
                ]},
                diagnosKod: 'J22'
            });
        });

        it('should create a section with text for formdata that is a text node', function() {
            expect(element.find('section').first().text()).toEqual('Akut bronkit');
        });


        it('should create a section with an unordered list for formdata that is a list node', function() {
            expect(element.find('#fmb_bullet_GENERELL_INFO_0').first().text()).toEqual('BulletText1');
            expect(element.find('#fmb_bullet_GENERELL_INFO_1').first().text()).toEqual('BulletText2');
            expect(element.find('#fmb_bullet_GENERELL_INFO_2').first().text()).toEqual('BulletText3');
        });

    });

    describe('alert appears correctly', function () {
        beforeEach(function() {
            setUpFMBData({
                formData: {},
                diagnosKod: 'J22',
                diagnosBeskrivning: 'Akut bronkit',
                originalDiagnoskod: 'J222'
            });
        });

        it('should have a visible alert text', function() {
           expect(element.find('#fmb_diagnos_not_in_fmb_alert').first().text())
               .toEqual('För J222 finns inget FMB-stöd. Det FMB-stöd som visas nedan gäller den mindre specifika koden J22 - Akut bronkit.');
        });
    });


});

