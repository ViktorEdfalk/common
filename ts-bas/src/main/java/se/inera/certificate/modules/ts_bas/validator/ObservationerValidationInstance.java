package se.inera.certificate.modules.ts_bas.validator;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.external.Observation;

public class ObservationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod OBS_H53_4 = CodeConverter.toKod(ObservationsKod.SYNFALTSDEFEKTER);
    private static final Kod OBS_H53_6 = CodeConverter.toKod(ObservationsKod.NATTBLINDHET);
    private static final Kod OBS_OBS1 = CodeConverter.toKod(ObservationsKod.PROGRESIV_OGONSJUKDOM);
    private static final Kod OBS_OBS4 = CodeConverter.toKod(ObservationsKod.FORSAMRAD_RORLIGHET_FRAMFORA_FORDON);
    private static final Kod OBS_OBS7 = CodeConverter.toKod(ObservationsKod.RISKFAKTORER_STROKE);
    private static final Kod OBS_H53_2 = CodeConverter.toKod(ObservationsKod.DIPLOPI);
    private static final Kod OBS_H55_9 = CodeConverter.toKod(ObservationsKod.NYSTAGMUS_MM);
    private static final Kod OBS_73211009 = CodeConverter.toKod(ObservationsKod.HAR_DIABETES);
    private static final Kod OBS_E10 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1);
    private static final Kod OBS_E11 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2);
    private static final Kod OBS_G40_9 = CodeConverter.toKod(ObservationsKod.EPILEPSI);

    private final List<Observation> observationer;

    public ObservationerValidationInstance(ExternalValidatorInstance prototype, List<Observation> observationer) {
        super(prototype.validationErrors, prototype.context);
        this.observationer = observationer;
    }

    public void validateObservationer() {

        boolean hasDiabetes = false;
        Observation diabetesTyp = null;
        String msg = null;
        Iterable<Kod> kodList = new ObservationerIterable(observationer);

        assertKodCountBetween(kodList, OBS_H53_4, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H53_6, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS1, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H53_2, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H55_9, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_73211009, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS4, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS7, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_G40_9, 1, 1, "observationer");

        // TODO: Count all other observations here

        for (Observation observation : observationer) {
            String entity = "Observation " + getDisplayCode(observation.getObservationskod());
            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, entity + ".observationsKod");

            if (observation.getObservationskod().equals(OBS_H53_4)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");
                assertNotNull(observation.getId(), entity + ".observationsid");

            } else if (observation.getObservationskod().equals(OBS_H53_6)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

            } else if (observation.getObservationskod().equals(OBS_OBS4)) {
                // Check OBS4 (Sjukdom som påverkar och medför att fordon inte kan köras på trafiksäkert sätt)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }

            } else if (observation.getObservationskod().equals(OBS_OBS7)) {
                // Check OBS7 (Riskfaktor för stroke)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }

            } else if (observation.getObservationskod().equals(OBS_G40_9)) {
                // Check OBS4 (Sjukdom som påverkar och medför att fordon inte kan köras på trafiksäkert sätt)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }
            }

            // TODO: Check all other observations here
        }

        // If the diabetes flag is set, assert that an observation of the type of diabetes is supplied
        if (context.isDiabetesContext()) {
            Observation diabetesTyp1 = getObservationWithKod(OBS_E10); 
            Observation diabetesTyp2 = getObservationWithKod(OBS_E11);
            if (diabetesTyp1 == null && diabetesTyp2 == null) {
                validationError("observation E10 or E11 must be present when observation 73211009 exitst");
            }
        }
    }

    public Observation getObservationWithKod(Kod observationskod) {
        for (Observation observation : observationer) {
            if (observationskod.equals(observation.getObservationskod())) {
                return observation;
            }
        }

        return null;
    }

    public Observation getObservationWithId(Id id) {
        for (Observation observation : observationer) {
            if (id.equals(observation.getId())) {
                return observation;
            }
        }

        return null;
    }

    private static class ObservationerIterable implements Iterable<Kod> {
        private final List<Observation> observationer;

        public ObservationerIterable(List<Observation> observationer) {
            this.observationer = observationer;
        }

        @Override
        public Iterator<Kod> iterator() {
            final Iterator<Observation> iter = observationer.iterator();
            return new Iterator<Kod>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Kod next() {
                    return iter.next().getObservationskod();
                }

                @Override
                public void remove() {
                }
            };
        }
    }
}
