/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.converters;

import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Utlatande;


public class XmlToExternalTest {

    private TransportToExternalConverter converter;

    @Before
    public void setUp() {
        converter = new TransportToExternalConverterImpl();
    }

    @Test
    public void testXmlToExternal() {
        Utlatande extUtlatande = converter.transportToExternal(unmarshallXml("rli-example-1.xml"));

        assertEquals("RLI", extUtlatande.getTyp().getCode());

        assertEquals("39f80245-9730-4d76-aaff-b04a2f3cfbe7", extUtlatande.getId().getExtension());

        assertEquals("Övriga upplysningar", extUtlatande.getKommentars().get(0));

        LocalDateTime signeratDate = new LocalDateTime("2013-08-12T11:25:00");
        assertEquals(signeratDate, extUtlatande.getSigneringsdatum());

        LocalDateTime skickatDate = new LocalDateTime("2013-08-12T11:25:30");
        assertEquals(skickatDate, extUtlatande.getSkickatdatum());

        assertEquals("1.2.752.129.2.1.3.1", extUtlatande.getPatient().getId().getRoot());
        assertEquals("19121212+1212", extUtlatande.getPatient().getId().getExtension());
        assertEquals("Test", extUtlatande.getPatient().getFornamn().get(0));
        assertEquals("Testsson", extUtlatande.getPatient().getEfternamn());
        assertEquals("Teststigen 1", extUtlatande.getPatient().getPostadress());
        assertEquals("123 45", extUtlatande.getPatient().getPostnummer());
        assertEquals("Stockholm", extUtlatande.getPatient().getPostort());

        assertEquals("1.2.752.129.2.1.4.1", extUtlatande.getSkapadAv().getId().getRoot());
        assertEquals("19101010+1010", extUtlatande.getSkapadAv().getId().getExtension());
        assertEquals("Doktor Alban", extUtlatande.getSkapadAv().getNamn());
        assertEquals("ABC123", extUtlatande.getSkapadAv().getForskrivarkod());

        // skapad av ->enhet
        assertEquals("1.2.752.129.2.1.4.1", extUtlatande.getSkapadAv().getVardenhet().getId().getRoot());
        assertEquals("vardenhet_test", extUtlatande.getSkapadAv().getVardenhet().getId().getExtension());
        assertEquals("Testenheten", extUtlatande.getSkapadAv().getVardenhet().getNamn());

        // skapad av -> enhet -> vardgivare
        assertEquals("1.2.752.129.2.1.4.1", extUtlatande.getSkapadAv().getVardenhet().getVardgivare().getId()
                .getRoot());
        assertEquals("vardgivare_test", extUtlatande.getSkapadAv().getVardenhet().getVardgivare().getId()
                .getExtension());
        assertEquals("Testvårdgivaren", extUtlatande.getSkapadAv().getVardenhet().getVardgivare().getNamn());

        List<Aktivitet> aktiviteter = extUtlatande.getAktiviteter();

        Aktivitet a = aktiviteter.get(0);

        assertEquals("AV020", a.getAktivitetskod().getCode());
        assertEquals("1.2.752.116.1.3.2.1.4", a.getAktivitetskod().getCodeSystem());
        assertEquals("KVÅ", a.getAktivitetskod().getCodeSystemName());

        assertEquals("1.2.752.129.2.1.4.1", a.getUtforsVid().getId().getRoot());
        assertEquals("vardenhet_test", a.getUtforsVid().getId().getExtension());
        assertEquals("Testenheten", a.getUtforsVid().getNamn());

        assertEquals("1.2.752.129.2.1.4.1", a.getUtforsVid().getVardgivare().getId().getRoot());
        assertEquals("vardgivare_test", a.getUtforsVid().getVardgivare().getId().getExtension());
        assertEquals("Testvårdgivaren", a.getUtforsVid().getVardgivare().getNamn());

        List<Rekommendation> rekommendationer = extUtlatande.getRekommendationer();
        for (Rekommendation rekommendation : rekommendationer) {
            assertEquals("REK1", rekommendation.getRekommendationskod().getCode());
            assertEquals("kv_rekommendation_intyg", rekommendation.getRekommendationskod().getCodeSystemName());
            assertEquals("SJK2", rekommendation.getSjukdomskannedom().getCode());
            assertEquals("f3a556c4-d54b-4f67-8496-d5259df70493", rekommendation.getSjukdomskannedom().getCodeSystem());
            assertEquals("kv_sjukdomskännedom_intyg", rekommendation.getSjukdomskannedom().getCodeSystemName());
        }

        List<Observation> observationer = extUtlatande.getObservations();
        for (Observation observation : observationer) {
            assertEquals("39104002", observation.getObservationsKod().getCode());
            assertEquals("1.2.752.116.2.1.1.1", observation.getObservationsKod().getCodeSystem());
            assertEquals("SNOMED-CT", observation.getObservationsKod().getCodeSystemName());
        }

        assertEquals("12345678-90", extUtlatande.getArrangemang().getBokningsreferens());
        assertEquals("2013-01-01", extUtlatande.getArrangemang().getBokningsdatum().toString());
        assertEquals("2013-07-22", extUtlatande.getArrangemang().getArrangemangstid().getFrom().toString());
        assertEquals("2013-08-02", extUtlatande.getArrangemang().getArrangemangstid().getTom().toString());
        assertEquals("2013-08", extUtlatande.getArrangemang().getAvbestallningsdatum().toString());
        assertEquals("420008001", extUtlatande.getArrangemang().getArrangemangstyp().getCode());
        assertEquals("1.2.752.116.2.1.1.1", extUtlatande.getArrangemang().getArrangemangstyp().getCodeSystem());
        assertEquals("SNOMED-CT", extUtlatande.getArrangemang().getArrangemangstyp().getCodeSystemName());
        assertEquals("New York", extUtlatande.getArrangemang().getPlats());

    }

    private se.inera.certificate.common.v1.Utlatande unmarshallXml(String resource) {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resource);
        return JAXB.unmarshal(stream, se.inera.certificate.common.v1.Utlatande.class);
    }
}
