/* jshint maxlen: false */

angular.module('db').constant('db.messages', {
    'sv': {
        //Validation messages
        'db.validation.explosivAvlagsnat.explosivImplantatFalse': 'Den valda kombinationen är ogiltig',
        'db.validation.undersokningDatum.after.dodsdatum': 'Undersökningsdatum får inte infalla efter dödsdatum',

        'db.makulera.body.common-header': '<p>Ett dödsbevis som är inskickat på fel person kan makuleras. Genom att trycka på "Makulera" makulerar du dödsbeviset i Webcert, med detta kommer inte återkalla dödsbeviset hos Skatteverket.</p>Förutom att trycka på "Makulera" måste du omedelbart ta kontakt med Skatteverket så att felet kan rättas fort. Du tar kontakt med Skatteverket genom att ringa till Skatteupplysningen på telefon 0771-567 567 och ange "folkbokföring - dödsfall".',
        'db.modal.ersatt.text': '<p>Senast skapade dödsorsaksintyg är det som gäller. Om du ersätter det tidigare dödsorsaksintyget och lämnar in det nya så blir det därför detta dödsorsaksintyg som gäller.</p>' +
        '<p>Ett dödsorsaksintyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att dödsorsaksintyget utfärdades. När ett dödsorsaksintyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga dödsorsaksintyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt dödsorsaksintyg är endast tillgängligt att läsa och skriva ut. På det ersatta dödsorsaksintyget kommer en länk finnas till det nya dödsorsaksintyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient ska du istället makulera intyget.</p>',

        'db.info.polisanmalan': 'Skriv även ut dödsbeviset och skicka det till polisen per post/fax.',

        'db.label.signandsend': 'Om du går vidare kommer dödsbeviset signeras och skickas direkt till Skatteverkets system.',

        'db.label.status.recieved': '<p>Dödsbeviset är signerat och har nu skickats till Skatteverket.</p>'+
            '<p>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!</p>'+
            '<p>Du kan nu avsluta Webcert eller direkt skriva ett dödsorsaksintyg för samma person genom att trycka på knappen "Skriv dödsorsaksintyg" ovan.</p>',

        'db.warn.previouscertificate.samevg': 'Dödsbevis finns för detta personnummer. Du kan inte skapa ett nytt dödsbevis men kan däremot välja att ersätta det befintliga dödsbeviset.',
        'db.warn.previouscertificate.differentvg': 'Tidigare dödsbevis finns för detta personnummer hos annan vårdgivare. Det är inte möjligt att skapa ett nytt dödsbevis.',

        'db.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat.',
        'db.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat.'
    }
});
