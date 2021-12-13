/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.model.question;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.common.support.facade.model.metadata.CertificateRelation;
import se.inera.intyg.common.support.facade.model.question.Question.QuestionBuilder;

@JsonDeserialize(builder = QuestionBuilder.class)
@Value
@Builder
public class Question {

    private String id;
    private QuestionType type;
    private String subject;
    private String message;
    private String author;
    private LocalDateTime sent;
    private Complement[] complements;
    private boolean isHandled;
    private boolean isForwarded;
    private Answer answer;
    private CertificateRelation answeredByCertificate;
    private Reminder[] reminders;
    private LocalDateTime lastUpdate;
    private LocalDate lastDateToReply;

    @JsonPOJOBuilder(withPrefix = "")
    public static class QuestionBuilder {

    }
}