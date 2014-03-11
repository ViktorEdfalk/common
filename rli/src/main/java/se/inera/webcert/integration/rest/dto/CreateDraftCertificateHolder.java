/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.webcert.integration.rest.dto;

import se.inera.certificate.modules.rli.model.external.HosPersonal;
import se.inera.certificate.modules.rli.model.external.Patient;

public class CreateDraftCertificateHolder {

    private String certificateType;

    private HosPersonal skapadAv;

    private Patient patientInfo;

    private Object somethingOnlyWebcertKnowsAbout;

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public HosPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HosPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Patient getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(Patient patientInfo) {
        this.patientInfo = patientInfo;
    }

    public Object getSomethingOnlyWebcertKnowsAbout() {
        return somethingOnlyWebcertKnowsAbout;
    }

    public void setSomethingOnlyWebcertKnowsAbout(Object somethingOnlyWebcertKnowsAbout) {
        this.somethingOnlyWebcertKnowsAbout = somethingOnlyWebcertKnowsAbout;
    }
}
