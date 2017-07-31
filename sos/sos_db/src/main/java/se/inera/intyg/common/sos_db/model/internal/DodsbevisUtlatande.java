package se.inera.intyg.common.sos_db.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TILLAGGSFRAGOR_SVAR_JSON_ID;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import autovalue.shaded.com.google.common.common.collect.ImmutableList;
import se.inera.intyg.common.services.texts.model.Tillaggsfraga;
import se.inera.intyg.common.sos_db.support.DodsbevisModuleEntryPoint;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.common.internal.GrundData;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_DodsbevisUtlatande.Builder.class)
public abstract class DodsbevisUtlatande implements SosUtlatande {

    public static Builder builder() {
         return new AutoValue_DodsbevisUtlatande.Builder().setTillaggsfragor(ImmutableList.<Tillaggsfraga> of());
    }

    @Override
    public String getTyp() {
        return DodsbevisModuleEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    public abstract String getIdentitetStyrkt();

    @Override
    public abstract boolean isDodsdatumSakert();

    @Override
    public abstract String getDodsdatum();

    @Override
    @Nullable
    public abstract LocalDate getAntraffatDodDatum();

    @Override
    public abstract String getDodsplatsKommun();

    @Override
    public abstract String getDodsplatsBoende();

    @Override
    public abstract boolean isBarn();

    public abstract ImmutableList<Tillaggsfraga> getTillaggsfragor();

    /*
     * Retrieve a builder from an existing LuseUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract DodsbevisUtlatande build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(IDENTITET_STYRKT_JSON_ID)
        public abstract Builder setIdentitetStyrkt(String identitetStyrkt);

        @JsonProperty(DODSDATUM_SAKERT_JSON_ID)
        public abstract Builder setDodsdatumSakert(boolean dodsdatumSakert);

        @JsonProperty(DODSDATUM_JSON_ID)
        public abstract Builder setDodsdatum(String dodsdatum);

        @JsonProperty(ANTRAFFAT_DOD_DATUM_JSON_ID)
        public abstract Builder setAntraffatDodDatum(LocalDate antraffatDodDatum);

        @JsonProperty(DODSPLATS_KOMMUN_JSON_ID)
        public abstract Builder setDodsplatsKommun(String dodsplatsKommun);

        @JsonProperty(DODSPLATS_BOENDE_JSON_ID)
        public abstract Builder setDodsplatsBoende(String dodsplatsBoende);

        @JsonProperty(BARN_JSON_ID)
        public abstract Builder setBarn(boolean barn);

        @JsonProperty(TILLAGGSFRAGOR_SVAR_JSON_ID)
        public Builder setTillaggsfragor(List<Tillaggsfraga> tillaggsfragor) {
            return setTillaggsfragor(ImmutableList.copyOf(tillaggsfragor));
        }

        /* package private */
        abstract Builder setTillaggsfragor(ImmutableList<Tillaggsfraga> tillaggsfragor);
    }
}
