package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class AnmalanAvser {

    @JsonCreator
    public static AnmalanAvser create(@JsonProperty("avserTyp") final AnmalanAvserTyp avserTyp) {
        return new AutoValue_AnmalanAvser(avserTyp);
    }

    @Nullable
    public abstract AnmalanAvserTyp getAvserTyp();
}
