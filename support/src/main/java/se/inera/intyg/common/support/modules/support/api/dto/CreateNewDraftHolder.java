/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.api.dto;

import static org.springframework.util.Assert.notNull;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;

public class CreateNewDraftHolder {

    private final String certificateId;

    private final HoSPersonal skapadAv;

    private final Patient patient;

    public CreateNewDraftHolder(String certificateId, HoSPersonal skapadAv, Patient patient) {
        notNull(certificateId, "'certificateId' must not be null");
        notNull(skapadAv, "'skapadAv' must not be null");
        notNull(patient, "'patient' must not be null");
        this.certificateId = certificateId;
        this.skapadAv = skapadAv;
        this.patient = patient;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public HoSPersonal getSkapadAv() {
        return skapadAv;
    }

    public Patient getPatient() {
        return patient;
    }
}
