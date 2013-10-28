package se.inera.certificate.modules.rli.pdf;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.internal.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.Graviditet;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Patient;
import se.inera.certificate.modules.rli.model.internal.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utlatande;
import se.inera.certificate.modules.rli.model.internal.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.Vardgivare;

import com.itextpdf.text.DocumentException;

public class PdfGeneratorTest {

    private PdfGeneratorImpl pdfGen;

    public PdfGeneratorTest() {
        pdfGen = new PdfGeneratorImpl();
    }

    @Test
    public void testGeneratePdfPatientIsSick() throws PdfGeneratorException, IOException, DocumentException {

        Utlatande utlatande = buildInternalUtlatandeSjuk();
        byte[] pdf = pdfGen.generatePDF(utlatande);
        assertNotNull(pdf);
        writePdfToFile(pdf);
    }

    @Test
    public void testGeneratePdfPatientIsPregnant() throws PdfGeneratorException, IOException, DocumentException {

        Utlatande utlatande = buildInternalUtlatandeGravid();
        byte[] pdf = pdfGen.generatePDF(utlatande);
        assertNotNull(pdf);
        writePdfToFile(pdf);
    }

    private void writePdfToFile(byte[] pdf) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(dir + "/RLI_intyg_" + LocalDateTime.now().toString("yyyyMMdd_HHmm") + pdf.hashCode() + ".pdf");
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private Utlatande buildInternalUtlatandeSjuk() {

        Utlatande utlatande = new Utlatande();
        utlatande.setUtlatandeid("f6fb361a-e31d-48b8-8657-99b63912dd9b");
        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsdatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatdatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Just ja här fanns det fler upplysningar"));

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangsdatum("2013-08-22");
        arr.setArrangemangstyp(ArrangemangsTyp.RESA);
        arr.setAvbestallningsdatum("2013-08");
        arr.setPlats("Östberga");
        arr.setBokningsreferens("12345678-90");
        arr.setBokningsdatum("2013-01-01");
        utlatande.setArrangemang(arr);

        Patient pat = new Patient();
        pat.setFornamn("Åke");
        pat.setEfternamn("Östlund");
        pat.setPostadress("Rådjurssvängen 1");
        pat.setPostnummer("123 45");
        pat.setPostort("Åre");
        pat.setPersonid("19121212-1212");
        pat.setFullstandigtNamn("Åke Östlund");
        utlatande.setPatient(pat);

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsid("1234-567");
        vardenhet.setEnhetsnamn("Tolvberga vårdcentral");
        vardenhet.setPostadress("Tolvstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.se");

        HoSPersonal hosPers = new HoSPersonal();
        hosPers.setFullstandigtNamn("Doktor Özjan");
        hosPers.setPersonid("19101010-1010");
        hosPers.setVardenhet(vardenhet);
        utlatande.setSkapadAv(hosPers);

        Rekommendation rekommendation = new Rekommendation();
        rekommendation.setRekommendationskod(RekommendationsKod.REK1);
        rekommendation.setSjukdomskannedom(SjukdomsKannedom.SJK3);
        utlatande.setRekommendation(rekommendation);

        Undersokning undersokning = new Undersokning();
        undersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
        undersokning.setUndersokningsdatum("2013-08-12");
        undersokning.setUndersokningsplats("Tolvberga vårdcentral");
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_PATIENT);
        undersokning.setForstaUndersokningsdatum("2010-01");
        undersokning.setForstaUndersokningsplats("Trestadens lasarett");
        utlatande.setUndersokning(undersokning);

        return utlatande;
    }

    private Utlatande buildInternalUtlatandeGravid() {

        Utlatande utlatande = new Utlatande();
        utlatande.setUtlatandeid("f6fb361a-e31d-48b8-8657-99b63912dd9b");
        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsdatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatdatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Just ja här fanns det fler upplysningar"));

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangsdatum("2013-08-22");
        arr.setArrangemangstyp(ArrangemangsTyp.RESA);
        arr.setAvbestallningsdatum("2013-08");
        arr.setPlats("Östberga");
        arr.setBokningsreferens("12345678-90");
        arr.setBokningsdatum("2013-01-01");
        utlatande.setArrangemang(arr);

        Patient pat = new Patient();
        pat.setFornamn("Anna");
        pat.setEfternamn("Östlund");
        pat.setPostadress("Rådjurssvängen 1");
        pat.setPostnummer("123 45");
        pat.setPostort("Åre");
        pat.setPersonid("19121212-1212");
        pat.setFullstandigtNamn("Anna Östlund");
        utlatande.setPatient(pat);

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsid("1234-567");
        vardenhet.setEnhetsnamn("Tolvberga vårdcentral");
        vardenhet.setPostadress("Tolvstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.se");

        HoSPersonal hosPers = new HoSPersonal();
        hosPers.setFullstandigtNamn("Doktor Özjan");
        hosPers.setPersonid("19101010-1010");
        hosPers.setVardenhet(vardenhet);
        utlatande.setSkapadAv(hosPers);

        Rekommendation rekommendation = new Rekommendation();
        rekommendation.setRekommendationskod(RekommendationsKod.REK1);
        rekommendation.setSjukdomskannedom(SjukdomsKannedom.SJK3);
        utlatande.setRekommendation(rekommendation);

        Undersokning undersokning = new Undersokning();
        undersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_GRAVID);

        Graviditet graviditet = new Graviditet();
        graviditet.setBeraknatForlossningsdatum("2013-09-25");

        undersokning.setGraviditet(graviditet);

        undersokning.setUndersokningsdatum("2013-08-12");
        undersokning.setUndersokningsplats("Tolvberga vårdcentral");
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_HOS_PERSONAL);
        undersokning.setForstaUndersokningsdatum("2010-01");
        undersokning.setForstaUndersokningsplats("Trestadens lasarett");
        utlatande.setUndersokning(undersokning);

        return utlatande;
    }

}
