/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.certificate.modules.sjukersattning.model.texts;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Category {

    Category() {
    }

    @JsonCreator
    public static Category create(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("helpText") String helpText,
            @JsonProperty("questions") List<Question> questions) {
        return new AutoValue_Category(id, title, helpText, ImmutableList.copyOf(questions));
    }

    public abstract String getId();

    public abstract String getTitle();

    @Nullable
    public abstract String getHelpText();

    public abstract List<Question> getQuestions();
}
