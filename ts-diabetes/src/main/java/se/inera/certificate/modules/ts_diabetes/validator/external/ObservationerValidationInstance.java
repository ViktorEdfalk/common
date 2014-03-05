package se.inera.certificate.modules.ts_diabetes.validator.external;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Observation;

public class ObservationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod OBS_170745003 = CodeConverter.toKod(ObservationsKod.DIABETIKER_ENBART_KOST);
    private static final Kod OBS_170746006 = CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING);
    private static final Kod OBS_170746002 = CodeConverter.toKod(ObservationsKod.DIABETIKER_TABLETTBEHANDLING);
    private static final Kod OBS_420050001 = CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA);
    private static final Kod OBS_397535007 = CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA);

    private static final Kod OBS_H53_2 = CodeConverter.toKod(ObservationsKod.DIPLOPI);
    private static final Kod OBS_E10 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1);
    private static final Kod OBS_E11 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2);

    private final List<Observation> observationer;

    public ObservationerValidationInstance(ExternalValidatorInstance prototype, List<Observation> observationer) {
        super(prototype.validationErrors, prototype.context);
        this.observationer = observationer;
    }

    /**
     * Validate all the observations. Make sure required observations are present, and that the correct number is
     * present.
     * 
     */
    public void validateObservationer() {

        Iterable<Kod> kodList = new ObservationerIterable(observationer);

        assertKodCountBetween(kodList, OBS_H53_2, 0, 1, "observationer");

        assertKodCountBetween(kodList, OBS_397535007, 0, 3, "observationer");
        assertKodCountBetween(kodList, OBS_420050001, 0, 3, "observationer");

        // TODO: Count all other observations here

        for (Observation observation : observationer) {
            String entity = "Observation " + getDisplayCode(observation.getObservationskod());
            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, entity + ".observationsKod");

            if (observation.getObservationskod().equals(OBS_397535007)) {
                assertNotNull(observation.getLateralitet(), entity + ".lateralitet");
                if (assertNotNull(observation.getVarde(), entity + ".varde").success()) {
                    if (observation.getVarde().get(0).getQuantity() < 0.0
                            || observation.getVarde().get(0).getQuantity() > 2.0) {
                        validationError("Varde for synskarpa med korrektion must be in the interval 0.0 <= X <= 2.0");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");

            } else if (observation.getObservationskod().equals(OBS_420050001)) {
                assertNotNull(observation.getLateralitet(), entity + ".lateralitet");
                if (assertNotNull(observation.getVarde(), entity + ".varde").success()) {
                    if (observation.getVarde().get(0).getQuantity() < 0.0
                            || observation.getVarde().get(0).getQuantity() > 2.0) {
                        validationError("Varde for synskarpa utan korrektion must be in the interval 0.0 <= X <= 2.0");
                    }
                }

                assertNull(observation.getForekomst(), entity + ".förekomst");
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");

            }
        }

        // If the diabetes flag is set, assert that an observation of the type of diabetes is supplied
        if (context.isDiabetesContext()) {
            Observation diabetesTyp1 = getObservationWithKod(OBS_E10);
            Observation diabetesTyp2 = getObservationWithKod(OBS_E11);
            if (diabetesTyp1 == null && diabetesTyp2 == null) {
                validationError("observation E10 or E11 must be present when observation 73211009 exitst");
            }

            if (diabetesTyp1 != null && diabetesTyp2 != null) {
                validationError("only one of E10 or E11 can be present at the same time");
            }

            // Also determine a treatment is specified if type2 diabetes is confirmed
            if (diabetesTyp2 != null) {
                Observation treatmentTablett = getObservationWithKod(OBS_170746002);
                Observation treatmentInsulin = getObservationWithKod(OBS_170746006);
                Observation treatmentKost = getObservationWithKod(OBS_170745003);
                if (treatmentTablett == null && treatmentInsulin == null && treatmentKost == null) {
                    validationError("At least one treatment for diabetes type2 must be specified");
                }
            }
        }

        // If the persontransport flag is set, assert required observations are supplied
        if (context.isPersontransportContext()) {
            // TODO: do stuff here

        }
    }

    /**
     * Returns an Observation based on the specified Kod, or <code>null</code> if none where found.
     * 
     * @param observationskod
     *            Find an observation with this {@link Kod}
     * @return an {@link Observation} if it is found, or null otherwise
     */
    public Observation getObservationWithKod(Kod observationskod) {
        for (Observation observation : observationer) {
            if (observationskod.equals(observation.getObservationskod())) {
                return observation;
            }
        }

        return null;
    }

    /**
     * Returns an Observation based on the specified Id, or <code>null</code> if none where found.
     * 
     * @param observationskod
     *            Find an observation with this {@link Id}
     * @return an {@link Observation} if it is found, or null otherwise
     */
    public Observation getObservationWithId(Id id) {
        for (Observation observation : observationer) {
            if (id.equals(observation.getId())) {
                return observation;
            }
        }

        return null;
    }

    /**
     * An {@link Iterable} emitting the {@link Kod}er of an underlying list of {@link Observation}er.
     */
    protected static class ObservationerIterable implements Iterable<Kod> {
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
