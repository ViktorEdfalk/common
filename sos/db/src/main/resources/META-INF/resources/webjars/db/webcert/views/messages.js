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
/* jshint maxlen: false */

angular.module('db').constant('db.messages', {
    'sv': {
        //Validation messages
        'db.validation.explosivAvlagsnat.explosivImplantatFalse': 'Den valda kombinationen är ogiltig.',
        'db.validation.undersokningDatum.after.dodsdatum': 'Datumet får inte vara senare än "Dödsdatum".',
        'db.validation.undersokningDatum.after.antraffatDodDatum' : 'Datumet får inte vara senare än datumet för "Anträffad död".',

        'db.validation.datum.innanDodsdatum': 'Datumet får inte vara tidigare än "Dödsdatum".',

        'db.makulera.body.common-header': '<p>Ett dödsbevis som är inskickat på fel person kan makuleras. Genom att trycka på "Makulera" makulerar du dödsbeviset i Webcert, med detta kommer inte återkalla dödsbeviset hos Skatteverket.</p>Förutom att trycka på "Makulera" måste du omedelbart ta kontakt med Skatteverket så att felet kan rättas fort. Du tar kontakt med Skatteverket genom att ringa till Skatteupplysningen på telefon 0771-567 567 och ange "folkbokföring - dödsfall".',
        'db.modal.ersatt.text.info': 'Om dödsbeviset är utfärdat på fel patient ska du istället makulera dödsbeviset.',
        'db.modal.ersatt.text':
        '<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt skapas ett utkast, med samma information som i det ursprungliga intyget, som du kan redigera innan du signerar intyget.</p>' +
        '<p>Senast skapade dödsbevis är det som gäller. Om du ersätter det tidigare dödsbeviset och lämnar in det nya så blir det därför detta dödsbevis som gäller.</p>',

        'db.label.signandsend': 'Om du går vidare kommer dödsbeviset signeras och skickas direkt till Skatteverkets system.',

        'db.label.status.recieved': '<p>Dödsbeviset är signerat och har nu skickats till Skatteverket.</p>'+
            '<p>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!</p>'+
            '<p>Du kan nu avsluta Webcert eller direkt skriva ett dödsorsaksintyg för samma person genom att trycka på knappen "Skriv dödsorsaksintyg" ovan.</p>',

        'db.warn.previouscertificate.samevg': 'Dödsbevis finns för detta personnummer. Du kan inte skapa ett nytt dödsbevis men kan däremot välja att ersätta det befintliga dödsbeviset.',
        'db.warn.previouscertificate.differentvg': 'Dödsbevis finns för detta personnummer hos annan vårdgivare. Det är inte möjligt att skapa ett nytt dödsbevis.',
        'db.warn.previousdraft.samevg': 'Utkast på dödsbevis finns för detta personnummer. Du kan inte skapa ett nytt utkast men kan däremot välja att fortsätta med det befintliga utkastet.',
        'db.warn.previousdraft.differentvg': 'Utkast på dödsbevis finns för detta personnummer hos annan vårdgivare. Senast skapade dödsbevis är det som gäller. Om du fortsätter och lämnar in dödsbeviset så blir det därför detta dödsbevis som gäller.',

        'db.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat.',
        'db.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat.',

        'db.createfromtemplate.doi.tooltip': 'Skapar ett dödsorsaksintyg utifrån dödsbeviset.',
        'db.createfromtemplate.doi.modal.header': 'Skapa dödsorsaksintyg utifrån dödsbevis',
        'db.createfromtemplate.doi.modal.text': 'Skapa ett dödsorsaksintyg utifrån ett dödsbevis innebär att informationsmängder som är gemensama för båda intyg, automatiskt förifylls.',
        'db.createfromtemplate.doi.modal.text.info': 'Dödsorsaksintyg finns för detta personnummer hos annan vårdgivare. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.'
    }
});
