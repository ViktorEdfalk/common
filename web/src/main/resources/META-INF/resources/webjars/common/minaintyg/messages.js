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

/* jshint maxlen: false */
angular.module('common').constant('common.messages', {
    'sv': {
        'common.yes': 'Ja',
        'common.no': 'Nej',
        'common.notset': 'Ej ifyllt',
        'common.yes.caps': 'JA',
        'common.no.caps': 'NEJ',
        'common.nodontask': 'Nej, fråga inte igen',
        'common.ok': 'OK',
        'common.cancel': 'Avbryt',
        'common.goback': 'Tillbaka',
        'common.sign': 'Signera',
        'common.send': 'Skicka',
        'common.copy': 'Kopiera',
        'common.delete': 'Ta bort',

        'common.date': 'Datum',

        // labels for common modal dialogs
        'common.modal.label.discard_draft' : 'Ta bort utkast',
        'common.modal.label.confirm_sign': 'Signera intyget',

        // cert status messages
        'cert.status.draft_incomplete': 'Utkast, ej komplett',
        'cert.status.draft_complete': 'Utkast, komplett',
        'cert.status.signed': 'Signerat',
        'cert.status.cancelled': 'Makulerat',
        'cert.status.unknown': 'Okänd',

        // unused statuses in gui
        'cert.status.sent': 'Skickat',
        'cert.status.received': 'Mottaget',

        // wc-common-directives-resources
        'nav.label.loggedinas': 'Inloggad som:',

        // Fraga/svar resource - used both by webcert and module
        // modal messages
        'modal.title.markforward': 'Markera som vidarebefordrad?',

        // Cert module messages. Used by several cert modules.
        'modules.label.field': 'Fält',
        'modules.label.blank': 'Ej ifyllt',

        // texts used by mi-certificate-action-buttons directive (would be nice to have resource keys per directive, like scss...)
        'modules.actionbar.button.send' : 'Välj mottagare och skicka intyg',
        'modules.actionbar.button.send.tooltip' : 'Skicka intyget elektroniskt, t.ex. till Försäkringskassan eller Transportstyrelsen.',
        'modules.actionbar.button.customizepdf' : 'Anpassa intyg till arbetsgivare',
        'modules.actionbar.button.customizepdf.tooltip' : 'Du kan välja bort intygsinformation du inte vill delge din arbetsgivare.',
        'modules.actionbar.button.print' : 'Ladda ner intyg som PDF',
        'modules.actionbar.button.print.tooltip' : 'Intyget sparas om PDF på din enhet.',
        'modules.actionbar.button.archive' : 'Arkivera intyg',
        'modules.actionbar.button.archive.tooltip' : 'Flytta intyget till Arkiverade intyg.',
        'modules.actionbar.archivedialog.title' : 'Arkivera intyg',
        'modules.actionbar.archivedialog.body' : 'När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'modules.actionbar.archivedialog.archive.button' : 'Arkivera intyg',

        // Common texts used in page headers
        'modules.page-header.info.select-recipients-and-send': '<h3>Välj mottagare och skicka intyget</h3>Klicka på knappen för att komma till sidan där du väljer mottagare och skickar intyget. Detta intyg kan till exempel skickas till Försäkringskassan.',
        'modules.page-header.info.customize-pdf': '<h3>Anpassa intyg till arbetsgivare</h3>Klicka på knappen för att välja vilken information i intyget du vill delge din arbetsgivare. Viss information måste lämnas till arbetsgivaren och kan inte väljas bort. Dessa fält är markerade som obligatoriska.',
        'modules.page-header.info.download-pdf': ' <h3>Ladda ner intyg som PDF</h3>Klicka på knappen för att ladda ner intyget som PDF. Du kan därefter välja hur du vill hantera intyget vidare, till exempel kan du skriva ut det. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna PDF-filer behöver du en PDF läsare, exempelvis <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',
        'modules.page-header.info.archive': ' <h3>Arkivera intyg</h3>Klicka på knappen för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',



        'modules.button.alt.archive': 'Arkivera intyget. Flyttar intyget till Arkiverade intyg',
        'modules.button.save.as.pdf': 'Spara som PDF',
        'modules.link.label.save.as.pdf': 'Spara som PDF',

        'info.loadingcertificate': 'Hämtar intyget...',

        // Common errors
        'common.error.unknown': '<strong>Tekniskt fel</strong>',
        'common.error.cantconnect': 'Kunde inte kontakta servern',
        'common.error.certificatenotfound': 'Intyget finns inte',
        'common.error.certificateinvalid': 'Intyget är inte korrekt ifyllt',
        'common.error.data_not_found': '<strong>Intyget kunde inte hittas.</strong><br>Intyget är borttaget eller så saknas behörighet.'
    },
    'en': {
        'common.ok': 'OK',
        'common.cancel': 'Cancel'
    }
});
