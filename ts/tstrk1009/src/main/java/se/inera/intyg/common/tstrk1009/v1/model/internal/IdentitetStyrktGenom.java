package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class IdentitetStyrktGenom {

    @JsonCreator
    public static IdentitetStyrktGenom create(@JsonProperty("typ") IdKontroll typ) {
        return new AutoValue_IdentitetStyrktGenom(typ);
    }

    @Nullable
    public abstract IdKontroll getTyp();
}
