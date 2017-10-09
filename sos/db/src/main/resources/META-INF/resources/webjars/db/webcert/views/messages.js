/* jshint maxlen: false */

angular.module('db').constant('db.messages', {
    'sv': {
        //Validation messages
        'db.validation.explosivAvlagsnat.explosivImplantatFalse': 'Den valda kombinationen är ogiltig',

        'db.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient ska du istället makulera intyget.</p>',

        'db.info.polisanmalan': 'Skriv även ut dödsbeviset och skicka det till polisen per post/fax.',

        'db.label.status.recieved': '<p>Dödsbeviset är signerat och har nu skickats till Skatteverket.</p>'+
            '<p>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!</p>'+
            '<p>Du kan nu avsluta intygstjänsten eller direkt skriva ett dödsorsaksintyg för samma person &lt;Knapp&gt;</p>',

        'db.warn.previouscertificate.samevg': 'Tidigare dödsbevis finns för detta personnummer. Senast skapade dödsbevis är det som gäller. Om du ersätter det tidigare dödsbeviset och lämnar in det nya så blir det därför detta dödsbevis som gäller.',
        'db.warn.previouscertificate.differentvg': 'Tidigare dödsbevis finns för detta personnummer. Senast skapade dödsbevis är det som gäller. Om du fortsätter och lämnar in dödsbeviset så blir det därför detta dödsbevis som gäller.'

    }
});
