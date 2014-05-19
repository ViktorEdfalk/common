define([], function() {
    'use strict';

    return {
        'sv': {

            'common.yes': 'Ja',
            'common.no': 'Nej',
            'common.nodontask': 'Nej, fråga inte igen',
            'common.ok': 'OK',
            'common.cancel': 'Avbryt',
            'common.goback': 'Tillbaka',
            'common.sign': 'Signera',

            'common.copy': 'Kopiera',
            'common.delete': 'Ta bort',

            // cert status messages
            'cert.status.draft_incomplete': 'Utkast, ej komplett',
            'cert.status.draft_complete': 'Utkast, komplett',
            'cert.status.signed': 'Signerat',
            'cert.status.cancelled': 'Rättat',
            'cert.status.unknown': 'Okänd',

            // unused statuses in gui
            'cert.status.sent': 'Skickat',
            'cert.status.received': 'Mottaget',

            // wc-common-directives-resources
            'nav.label.loggedinas': 'Inloggad som:',

            // Fraga/svar resource - used both by webcert and module
            // modal messages
            'modal.title.markforward': 'Markera som vidarebefordrad?',

            'qa.status.pending_internal_action': 'Kräver svar',
            'qa.status.pending_external_action': 'Inväntar svar',
            'qa.status.answered': 'Besvarat',
            'qa.status.closed': 'Hanterat',

            'qa.fragestallare.fk': 'Försäkringskassan',
            'qa.fragestallare.wc': 'Vårdenheten',
            'qa.amne.paminnelse': 'Påminnelse',
            'qa.amne.arbetstidsforlaggning': 'Arbetstidsförläggning',
            'qa.amne.kontakt': 'Kontakt',
            'qa.amne.avstamningsmote': 'Avstämningsmöte',
            'qa.amne.komplettering_av_lakarintyg': 'Komplettering av läkarintyg',
            'qa.amne.ovrigt': 'Övrigt',

            'qa.measure.svarfranvarden': 'Svara',
            'qa.measure.svarfranfk': 'Invänta svar från Försäkringskassan',
            'qa.measure.komplettering': 'Komplettera',
            'qa.measure.markhandled': 'Markera som hanterad',
            'qa.measure.handled': 'Ingen',

            // Cert module messages. Used by several cert modules.
            'modules.label.field': 'Fält',
            'modules.label.blank': '- ej ifyllt',

            'info.loadingcertificate': 'Hämtar intyget...',

            // Common errors
            'common.error.unknown': '<strong>Tekniskt fel</strong>',
            'common.error.cantconnect': 'Kunde inte kontakta servern',
            'common.error.certificatenotfound': 'Intyget finns inte',
            'common.error.certificateinvalid': 'Intyget är inte korrekt ifyllt',
            'common.error.signerror': 'Intyget kunde inte signeras. Försök igen.',
            'common.error.data_not_found': '<strong>Intyget kunde inte hittas.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        },
        'en': {
            'common.ok': 'OK',
            'common.cancel': 'Cancel'
        }
    };
});
