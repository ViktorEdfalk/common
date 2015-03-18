package se.inera.certificate.modules.ts_diabetes.model.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.modules.ts_diabetes.model.internal.Bedomning;
import se.inera.certificate.modules.ts_diabetes.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_diabetes.model.internal.Syn;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.model.internal.Vardkontakt;
import se.inera.certificate.schema.Constants;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.BedomningTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.Diabetes;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.GrundData;
import se.inera.intygstjanster.ts.services.v1.Hypoglykemier;
import se.inera.intygstjanster.ts.services.v1.IdentifieringsVarden;
import se.inera.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.inera.intygstjanster.ts.services.v1.IntygsAvserTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.Korkortsbehorighet;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.inera.intygstjanster.ts.services.v1.Vardgivare;

public class InternalToTransportConverter {
    
    public static final Map<String, DiabetesTypVarden> typVardenMap;
    
    static {
        Map<String, DiabetesTypVarden> tempMap = new HashMap<>();
        tempMap.put("DIABETES_TYP_2", DiabetesTypVarden.TYP_2);
        tempMap.put("DIABETES_TYP_1", DiabetesTypVarden.TYP_1);
        
        typVardenMap = Collections.unmodifiableMap(tempMap);
    }
    
	public static TSDiabetesIntyg convert(Utlatande utlatande) {
		TSDiabetesIntyg result = new TSDiabetesIntyg();
		
		result.setBedomning(readBedomning(utlatande.getBedomning()));
		result.setDiabetes(readDiabetes(utlatande.getDiabetes()));
		result.setGrundData(readGrundData(utlatande.getGrundData()));
		result.setHypoglykemier(readHypoglykemier(utlatande.getHypoglykemier()));
		result.setIdentitetStyrkt(readIdentitetStyrkt(utlatande.getVardkontakt()));
		result.setIntygAvser(readIntygAvser(utlatande.getIntygAvser()));
		result.setIntygsId(utlatande.getId());
		//TODO: temp, force type
		result.setIntygsTyp("TSTRK1031 (U06, V06)");
		result.setSeparatOgonLakarintygKommerSkickas(utlatande.getSyn().getSeparatOgonlakarintyg());
		result.setSynfunktion(readSynfunktionDiabetes(utlatande.getSyn()));
		//TODO: temp, force utgåva
		result.setUtgava("06");
		//TODO: temp, force version
		result.setVersion("02");
		return result;
	}

	private static IdentitetStyrkt readIdentitetStyrkt(Vardkontakt vardkontakt) {
	    IdentitetStyrkt result = new IdentitetStyrkt();
        result.getIdkontroll().add(IdentifieringsVarden.fromValue(vardkontakt.getIdkontroll()));
	    return result;
    }

    private static SynfunktionDiabetes readSynfunktionDiabetes(Syn syn) {
		SynfunktionDiabetes result = new SynfunktionDiabetes();
		result.setHarDiplopi(syn.getDiplopi());
		result.setHarSynfaltsdefekt(syn.getSynfaltsprovningUtanAnmarkning() == false);
		result.setSynskarpaMedKorrektion(readMedKorrektion(syn));
		result.setSynskarpaUtanKorrektion(readUtanKorrektion(syn));
		result.setFinnsSynfaltsprovning(syn.getSynfaltsprovning());
		result.setSynfaltsprovningUtanAnmarkning(syn.getSynfaltsprovningUtanAnmarkning());
		result.setFinnsProvningOgatsRorlighet(syn.getProvningOgatsRorlighet());
		return result;
	}

	private static SynskarpaUtanKorrektion readUtanKorrektion(Syn syn) {
        SynskarpaUtanKorrektion result = new SynskarpaUtanKorrektion();
        result.setBinokulart(syn.getBinokulart().getUtanKorrektion());
        result.setHogerOga(syn.getHoger().getUtanKorrektion());
        result.setVansterOga(syn.getVanster().getUtanKorrektion());
        return result;
    }

    private static SynskarpaMedKorrektion readMedKorrektion(Syn syn) {
	    SynskarpaMedKorrektion result = new SynskarpaMedKorrektion();
	    result.setBinokulart(syn.getBinokulart().getMedKorrektion());
	    result.setHogerOga(syn.getHoger().getMedKorrektion());
	    result.setVansterOga(syn.getVanster().getMedKorrektion());
	    return result;
    }

    private static IntygsAvserTypDiabetes readIntygAvser(IntygAvser intygAvser) {
		IntygsAvserTypDiabetes result = new IntygsAvserTypDiabetes();
		
		for(IntygAvserKategori kat : intygAvser.getKorkortstyp()){
			KorkortsbehorighetTsDiabetes bh = KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(kat.name()));
			result.getKorkortstyp().add(bh);
		}
		
		return result;
	}

	private static Hypoglykemier readHypoglykemier(se.inera.certificate.modules.ts_diabetes.model.internal.Hypoglykemier hypoglykemier) {
		Hypoglykemier result = new Hypoglykemier();
		result.setAllvarligForekomstBeskrivning(hypoglykemier.getAllvarligForekomstBeskrivning());
		result.setAllvarligForekomstTrafikBeskrivning(hypoglykemier.getAllvarligForekomstTrafikBeskrivning());
		result.setAllvarligForekomstVakenTidAr(hypoglykemier.getAllvarligForekomstVakenTidObservationstid().getDate());
		result.setGenomforEgenkontrollBlodsocker(hypoglykemier.getEgenkontrollBlodsocker());
		result.setHarAllvarligForekomst(hypoglykemier.getAllvarligForekomst());
		result.setHarAllvarligForekomstTrafiken(hypoglykemier.getAllvarligForekomstTrafiken());
		result.setHarAllvarligForekomstVakenTid(hypoglykemier.getAllvarligForekomstVakenTid());
		result.setHarKunskapOmAtgarder(hypoglykemier.getKunskapOmAtgarder());
		result.setHarTeckenNedsattHjarnfunktion(hypoglykemier.getTeckenNedsattHjarnfunktion());
		result.setSaknarFormagaKannaVarningstecken(hypoglykemier.getSaknarFormagaKannaVarningstecken());
		return result;
	}

	private static GrundData readGrundData(se.inera.certificate.model.common.internal.GrundData grundData) {
		GrundData result = new GrundData();
		result.setPatient(readPatient(grundData.getPatient()));
		result.setSigneringsTidstampel(grundData.getSigneringsdatum().toString("yyyyMMdd'T'HHmmss"));
		result.setSkapadAv(readSkapadAv(grundData.getSkapadAv()));
		return result;
	}

	private static SkapadAv readSkapadAv(HoSPersonal skapadAv) {
	    SkapadAv result = new SkapadAv();
	    
	    II ii = new II();
	    ii.setRoot(Constants.HSA_ID_OID);
	    ii.setExtension(skapadAv.getPersonId());
	    
        result.setPersonId(ii);
        result.setFullstandigtNamn(skapadAv.getFullstandigtNamn());
        result.setVardenhet(readVardenhet(skapadAv.getVardenhet()));
	    return result;
    }

    private static Vardenhet readVardenhet(se.inera.certificate.model.common.internal.Vardenhet vardenhet) {
        Vardenhet result = new Vardenhet();
        II ii = new II();
        ii.setRoot(Constants.HSA_ID_OID);
        ii.setExtension(vardenhet.getEnhetsid());
        
        result.setEnhetsId(ii);
        result.setEnhetsnamn(vardenhet.getEnhetsnamn());
        result.setPostadress(vardenhet.getPostadress());
        result.setPostnummer(vardenhet.getPostnummer());
        result.setPostort(vardenhet.getPostort());
        result.setTelefonnummer(vardenhet.getTelefonnummer());
        result.setVardgivare(readVardgivare(vardenhet.getVardgivare()));
        return result;
    }

    private static Vardgivare readVardgivare(se.inera.certificate.model.common.internal.Vardgivare vardgivare) {
        Vardgivare result = new Vardgivare();
        II ii = new II();
        ii.setRoot(Constants.HSA_ID_OID);
        ii.setExtension(vardgivare.getVardgivarid());
        
        result.setVardgivarid(ii);
        result.setVardgivarnamn(vardgivare.getVardgivarnamn());
        return result;
    }

    private static Patient readPatient(
			se.inera.certificate.model.common.internal.Patient patient) {
		Patient result = new Patient();
		result.setEfternamn(patient.getEfternamn());
		result.setFornamn(patient.getFornamn());
		result.setFullstandigtNamn(patient.getFullstandigtNamn());
		
		II iid = new II();
		iid.setRoot(Constants.PERSON_ID_OID);
		iid.setExtension(patient.getPersonId());
		result.setPersonId(iid);
		
		result.setPostadress(patient.getPostadress());
		result.setPostnummer(patient.getPostnummer());
		result.setPostort(patient.getPostort());
		return result;
	}

	private static Diabetes readDiabetes(se.inera.certificate.modules.ts_diabetes.model.internal.Diabetes diabetes) {
		Diabetes result = new Diabetes();
		result.setAnnanBehandlingBeskrivning(diabetes.getAnnanBehandlingBeskrivning());
		result.setDebutArDiabetes(diabetes.getObservationsperiod());
		result.setHarBehandlingInsulin(diabetes.getInsulin());
		result.setHarBehandlingKost(diabetes.getEndastKost());
		result.setHarBehandlingTabletter(diabetes.getTabletter());
		result.setInsulinBehandlingSedanAr(diabetes.getInsulinBehandlingsperiod());
		
		result.getDiabetesTyp().add(typVardenMap.get(diabetes.getDiabetestyp()));
		return result;
	}

	private static BedomningTypDiabetes readBedomning(Bedomning bedomning) {
		BedomningTypDiabetes result = new BedomningTypDiabetes();
		result.setBehovAvLakareSpecialistKompetens(bedomning.getLakareSpecialKompetens());
		result.setKanInteTaStallning(bedomning.getKanInteTaStallning());
		result.setLamplighetInnehaBehorighetSpecial(bedomning.getLamplighetInnehaBehorighet());
		result.setOvrigKommentar(bedomning.getKommentarer());
		
		for (BedomningKorkortstyp typ : bedomning.getKorkortstyp()) {
            result.getKorkortstyp().add(KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(typ.name())));
        }
		
		return result;
	}
}
