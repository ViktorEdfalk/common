package se.inera.certificate.modules.sjukpenning.model.internal;

import static se.inera.certificate.common.util.StringUtil.emptyToNull;
import static se.inera.certificate.common.util.StringUtil.join;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.common.internal.Utlatande;

public class SjukpenningUtlatande extends Utlatande {

    private boolean avstangningSmittskydd;

    private InternalDate undersokningAvPatienten;
    private InternalDate telefonkontaktMedPatienten;
    private InternalDate journaluppgifter;

    private boolean nuvarandeArbete;
    private String nuvarandeArbetsuppgifter;
    private String nuvarandeYrke;
    private boolean arbetsloshet;
    private boolean foraldraledighet;
    private boolean studier;
    private boolean arbetsmarknadsProgram;

    private String diagnosKod1;
    private String diagnosKodsystem1;
    private String diagnosBeskrivning1;

    private String diagnosKod2;
    private String diagnosKodsystem2;
    private String diagnosBeskrivning2;

    private String diagnosKod3;
    private String diagnosKodsystem3;
    private String diagnosBeskrivning3;

    private String funktionsnedsattning;
    private String aktivitetsbegransning;

    private String pagaendeBehandling;
    private String planeradBehandling;

    private InternalLocalDateInterval nedsattMed25;
    private InternalLocalDateInterval nedsattMed50;
    private InternalLocalDateInterval nedsattMed75;
    private InternalLocalDateInterval nedsattMed100;

    private boolean ressattTillArbeteAktuellt;
    private boolean rekommendationOverSocialstyrelsensBeslutsstod;

    private String vadPatientenKanGora;
    private String prognosNarPatientKanAterga;

    // ==================================================================================================

    private String sjukdomsforlopp;

    private boolean rekommendationKontaktArbetsformedlingen;
    private boolean rekommendationKontaktForetagshalsovarden;
    private boolean rekommendationOvrigtCheck;
    private String rekommendationOvrigt;

    private Rehabilitering rehabilitering;

    private String tjanstgoringstid;
    private String nedsattMed25Beskrivning;
    private String nedsattMed50Beskrivning;
    private String nedsattMed75Beskrivning;

    private String arbetsformagaPrognos;

    private PrognosBedomning prognosBedomning;

    private String arbetsformagaPrognosGarInteAttBedomaBeskrivning;

    private boolean ressattTillArbeteEjAktuellt;

    private boolean kontaktMedFk;

    private String kommentar;

    private LocalDateInterval giltighet;

    public String getTjanstgoringstid() {
        return tjanstgoringstid;
    }

    public void setTjanstgoringstid(String tjanstgoringstid) {
        this.tjanstgoringstid = tjanstgoringstid;
    }

    public String getNedsattMed25Beskrivning() {
        return nedsattMed25Beskrivning;
    }

    public void setNedsattMed25Beskrivning(String nedsattMed25Beskrivning) {
        this.nedsattMed25Beskrivning = nedsattMed25Beskrivning;
    }

    public String getNedsattMed50Beskrivning() {
        return nedsattMed50Beskrivning;
    }

    public void setNedsattMed50Beskrivning(String nedsattMed50Beskrivning) {
        this.nedsattMed50Beskrivning = nedsattMed50Beskrivning;
    }

    public String getNedsattMed75Beskrivning() {
        return nedsattMed75Beskrivning;
    }

    public void setNedsattMed75Beskrivning(String nedsattMed75Beskrivning) {
        this.nedsattMed75Beskrivning = nedsattMed75Beskrivning;
    }

    public boolean isAvstangningSmittskydd() {
        return avstangningSmittskydd;
    }

    public void setAvstangningSmittskydd(boolean avstangningSmittskydd) {
        this.avstangningSmittskydd = avstangningSmittskydd;
    }

    public String getDiagnosKod1() {
        return diagnosKod1;
    }

    public void setDiagnosKod1(String diagnosKod) {
        this.diagnosKod1 = diagnosKod;
    }

    public String getDiagnosKodsystem1() {
        return diagnosKodsystem1;
    }

    public void setDiagnosKodsystem1(String diagnosKodsystem1) {
        this.diagnosKodsystem1 = diagnosKodsystem1;
    }

    public String getDiagnosBeskrivning1() {
        return diagnosBeskrivning1;
    }

    public void setDiagnosBeskrivning1(String diagnosBeskrivning) {
        this.diagnosBeskrivning1 = diagnosBeskrivning;
    }

    public String getDiagnosKod2() {
        return diagnosKod2;
    }

    public void setDiagnosKod2(String diagnosKod2) {
        this.diagnosKod2 = diagnosKod2;
    }

    public String getDiagnosBeskrivning2() {
        return diagnosBeskrivning2;
    }

    public void setDiagnosBeskrivning2(String diagnosBeskrivning2) {
        this.diagnosBeskrivning2 = diagnosBeskrivning2;
    }

    public String getDiagnosKod3() {
        return diagnosKod3;
    }

    public void setDiagnosKod3(String diagnosKod3) {
        this.diagnosKod3 = diagnosKod3;
    }

    public String getDiagnosBeskrivning3() {
        return diagnosBeskrivning3;
    }

    public void setDiagnosBeskrivning3(String diagnosBeskrivning3) {
        this.diagnosBeskrivning3 = diagnosBeskrivning3;
    }

    public String getDiagnosKodsystem2() {
        return diagnosKodsystem2;
    }

    public void setDiagnosKodsystem2(String diagnosKodsystem2) {
        this.diagnosKodsystem2 = diagnosKodsystem2;
    }

    public String getDiagnosKodsystem3() {
        return diagnosKodsystem3;
    }

    public void setDiagnosKodsystem3(String diagnosKodsystem3) {
        this.diagnosKodsystem3 = diagnosKodsystem3;
    }

    public String getSjukdomsforlopp() {
        return sjukdomsforlopp;
    }

    public void setSjukdomsforlopp(String sjukdomsforlopp) {
        this.sjukdomsforlopp = sjukdomsforlopp;
    }

    public String getFunktionsnedsattning() {
        return funktionsnedsattning;
    }

    public void setFunktionsnedsattning(String funktionsnedsattning) {
        this.funktionsnedsattning = funktionsnedsattning;
    }

    public InternalDate getUndersokningAvPatienten() {
        return undersokningAvPatienten;
    }

    public void setUndersokningAvPatienten(InternalDate undersokningAvPatienten) {
        this.undersokningAvPatienten = undersokningAvPatienten;
    }

    public InternalDate getTelefonkontaktMedPatienten() {
        return telefonkontaktMedPatienten;
    }

    public void setTelefonkontaktMedPatienten(InternalDate telefonkontaktMedPatienten) {
        this.telefonkontaktMedPatienten = telefonkontaktMedPatienten;
    }

    public InternalDate getJournaluppgifter() {
        return journaluppgifter;
    }

    public void setJournaluppgifter(InternalDate journaluppgifter) {
        this.journaluppgifter = journaluppgifter;
    }

    public String getAktivitetsbegransning() {
        return aktivitetsbegransning;
    }

    public void setAktivitetsbegransning(String aktivitetsbegransning) {
        this.aktivitetsbegransning = aktivitetsbegransning;
    }

    public boolean isRekommendationKontaktArbetsformedlingen() {
        return rekommendationKontaktArbetsformedlingen;
    }

    public void setRekommendationKontaktArbetsformedlingen(boolean rekommendationKontaktArbetsformedlingen) {
        this.rekommendationKontaktArbetsformedlingen = rekommendationKontaktArbetsformedlingen;
    }

    public boolean isRekommendationKontaktForetagshalsovarden() {
        return rekommendationKontaktForetagshalsovarden;
    }

    public void setRekommendationKontaktForetagshalsovarden(boolean rekommendationKontaktForetagshalsovarden) {
        this.rekommendationKontaktForetagshalsovarden = rekommendationKontaktForetagshalsovarden;
    }

    public boolean isRekommendationOvrigtCheck() {
        return rekommendationOvrigtCheck;
    }

    public void setRekommendationOvrigtCheck(boolean rekommendationOvrigtCheck) {
        this.rekommendationOvrigtCheck = rekommendationOvrigtCheck;
    }

    public String getRekommendationOvrigt() {
        return rekommendationOvrigt;
    }

    public void setRekommendationOvrigt(String rekommendationOvrigt) {
        this.rekommendationOvrigt = rekommendationOvrigt;
    }

    public String getPagaendeBehandling() {
        return pagaendeBehandling;
    }

    public void setPagaendeBehandling(String pagaendeBehandling) {
        this.pagaendeBehandling = pagaendeBehandling;
    }

    public String getPlaneradBehandling() {
        return planeradBehandling;
    }

    public void setPlaneradBehandling(String planeradBehandling) {
        this.planeradBehandling = planeradBehandling;
    }

    public Rehabilitering getRehabilitering() {
        return this.rehabilitering;
    }

    public void setRehabilitering(Rehabilitering rehabilitering) {
        this.rehabilitering = rehabilitering;
    }

    public boolean isNuvarandeArbete() {
        return nuvarandeArbete;
    }

    public void setNuvarandeArbete(boolean nuvarandeArbete) {
        this.nuvarandeArbete = nuvarandeArbete;
    }

    public String getNuvarandeArbetsuppgifter() {
        return nuvarandeArbetsuppgifter;
    }

    public void setNuvarandeArbetsuppgifter(String nuvarandeArbetsuppgifter) {
        this.nuvarandeArbetsuppgifter = nuvarandeArbetsuppgifter;
    }

    public boolean isArbetsloshet() {
        return arbetsloshet;
    }

    public void setArbetsloshet(boolean arbetsloshet) {
        this.arbetsloshet = arbetsloshet;
    }

    public boolean isForaldraledighet() {
        return foraldraledighet;
    }

    public void setForaldraledighet(boolean foraldraledighet) {
        this.foraldraledighet = foraldraledighet;
    }

    public InternalLocalDateInterval getNedsattMed25() {
        return nedsattMed25;
    }

    public void setNedsattMed25(InternalLocalDateInterval nedsattMed25) {
        this.nedsattMed25 = nedsattMed25;
    }

    public InternalLocalDateInterval getNedsattMed50() {
        return nedsattMed50;
    }

    public void setNedsattMed50(InternalLocalDateInterval nedsattMed50) {
        this.nedsattMed50 = nedsattMed50;
    }

    public InternalLocalDateInterval getNedsattMed75() {
        return nedsattMed75;
    }

    public void setNedsattMed75(InternalLocalDateInterval nedsattMed75) {
        this.nedsattMed75 = nedsattMed75;
    }

    public InternalLocalDateInterval getNedsattMed100() {
        return nedsattMed100;
    }

    public void setNedsattMed100(InternalLocalDateInterval nedsattMed100) {
        this.nedsattMed100 = nedsattMed100;
    }

    public String getArbetsformagaPrognos() {
        return arbetsformagaPrognos;
    }

    public void setArbetsformagaPrognos(String arbetsformagaPrognos) {
        this.arbetsformagaPrognos = arbetsformagaPrognos;
    }

    public boolean isRessattTillArbeteAktuellt() {
        return ressattTillArbeteAktuellt;
    }

    public void setRessattTillArbeteAktuellt(boolean ressattTillArbeteAktuellt) {
        this.ressattTillArbeteAktuellt = ressattTillArbeteAktuellt;
    }

    public boolean isRessattTillArbeteEjAktuellt() {
        return ressattTillArbeteEjAktuellt;
    }

    public void setRessattTillArbeteEjAktuellt(boolean ressattTillArbeteEjAktuellt) {
        this.ressattTillArbeteEjAktuellt = ressattTillArbeteEjAktuellt;
    }

    public boolean isKontaktMedFk() {
        return kontaktMedFk;
    }

    public void setKontaktMedFk(boolean kontaktMedFk) {
        this.kontaktMedFk = kontaktMedFk;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getForskrivarkodOchArbetsplatskod() {
        return emptyToNull(join(" - ", getGrundData().getSkapadAv().getForskrivarKod(), getGrundData().getSkapadAv().getVardenhet().getArbetsplatsKod()));
    }

    public String getNamnfortydligandeOchAdress() {
        return join("\n", getGrundData().getSkapadAv().getFullstandigtNamn(),
                getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn(),
                getGrundData().getSkapadAv().getVardenhet().getPostadress(),
                join(" ", getGrundData().getSkapadAv().getVardenhet().getPostnummer(), getGrundData().getSkapadAv().getVardenhet().getPostort()),
                getGrundData().getSkapadAv().getVardenhet().getTelefonnummer());
    }

    public LocalDateInterval getGiltighet() {
        return giltighet;
    }

    public void setGiltighet(LocalDateInterval giltighet) {
        this.giltighet = giltighet;
    }

    public String getArbetsformagaPrognosGarInteAttBedomaBeskrivning() {
        return arbetsformagaPrognosGarInteAttBedomaBeskrivning;
    }

    public void setArbetsformagaPrognosGarInteAttBedomaBeskrivning(
            String arbetsformagaPrognosGarInteAttBedomaBeskrivning) {
        this.arbetsformagaPrognosGarInteAttBedomaBeskrivning = arbetsformagaPrognosGarInteAttBedomaBeskrivning;
    }

    public PrognosBedomning getPrognosBedomning() {
        return prognosBedomning;
    }

    public void setPrognosBedomning(PrognosBedomning prognosBedomning) {
        this.prognosBedomning = prognosBedomning;
    }

    public String getNuvarandeYrke() {
        return nuvarandeYrke;
    }

    public void setNuvarandeYrke(String nuvarandeYrke) {
        this.nuvarandeYrke = nuvarandeYrke;
    }

    public boolean isStudier() {
        return studier;
    }

    public void setStudier(boolean studier) {
        this.studier = studier;
    }

    public boolean isArbetsmarknadsProgram() {
        return arbetsmarknadsProgram;
    }

    public void setArbetsmarknadsProgram(boolean arbetsmarknadsProgram) {
        this.arbetsmarknadsProgram = arbetsmarknadsProgram;
    }

    public boolean isRekommendationOverSocialstyrelsensBeslutsstod() {
        return rekommendationOverSocialstyrelsensBeslutsstod;
    }

    public void setRekommendationOverSocialstyrelsensBeslutsstod(boolean rekommendationOverSocialstyrelsensBeslutsstod) {
        this.rekommendationOverSocialstyrelsensBeslutsstod = rekommendationOverSocialstyrelsensBeslutsstod;
    }

    public String getVadPatientenKanGora() {
        return vadPatientenKanGora;
    }

    public void setVadPatientenKanGora(String vadPatientenKanGora) {
        this.vadPatientenKanGora = vadPatientenKanGora;
    }

    public String getPrognosNarPatientKanAterga() {
        return prognosNarPatientKanAterga;
    }

    public void setPrognosNarPatientKanAterga(String prognosNarPatientKanAterga) {
        this.prognosNarPatientKanAterga = prognosNarPatientKanAterga;
    }
}
