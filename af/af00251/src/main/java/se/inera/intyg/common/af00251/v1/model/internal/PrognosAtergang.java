package se.inera.intyg.common.af00251.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.*;

/**
 *
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_PrognosAtergang.Builder.class)
public abstract class PrognosAtergang {

    // Delfråga 8.1
    @Nullable
    public abstract Prognos getPrognos();
    // Delfråga 8.2
    @Nullable
    public abstract String getAnpassningar();


    public static PrognosAtergang.Builder builder() {
        return new AutoValue_PrognosAtergang.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract PrognosAtergang build();

        @JsonProperty(PROGNOS_ATERGANG_SVAR_JSON_ID_81)
        public abstract PrognosAtergang.Builder setPrognos(Prognos prognos);

        @JsonProperty(PROGNOS_ATERGANG_SVAR_JSON_ID_82)
        public abstract PrognosAtergang.Builder setAnpassningar(String anpassningar);

    }


    public enum Prognos {

        UTAN_ANPASSNING("ATERGA_UTAN_ANPASSNING", "Patienten kan återgå utan anpassning"),
        MED_ANPASSNING("ATERGA_MED_ANPASSNING", "Patienten kan återgå med anpassning"),
        KAN_EJ_ATERGA("KAN_EJ_ATERGA", "Patienten inte kan återgå"),
        EJ_MOJLIGT_AVGORA("EJ_MOJLIGT_AVGORA", "Det inte är möjligt att avgöra om patienten kan återgå");


        private String id;
        private String label;

        Prognos(String id, String label) {
            this.id = id;
            this.label = label;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static Prognos fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (Prognos typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

        public static String KODVERK = "kv-prognos-återgång-arbetsmarknadspolitiskt-program";

    }

}
