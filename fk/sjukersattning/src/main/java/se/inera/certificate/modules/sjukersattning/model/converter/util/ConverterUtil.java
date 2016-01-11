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

package se.inera.certificate.modules.sjukersattning.model.converter.util;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;

public class ConverterUtil {

    @Autowired
    @Qualifier("sjukersattning-objectMapper")
    private ObjectMapper objectMapper;

    @VisibleForTesting
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CertificateHolder toCertificateHolder(SjukersattningUtlatande utlatande) throws ModuleException {
        String document = toJsonString(utlatande);
        return toCertificateHolder(utlatande, document);
    }

    public CertificateHolder toCertificateHolder(SjukersattningUtlatande utlatande, String document) throws ModuleException {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setId(utlatande.getId());
        certificateHolder.setCareUnitId(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        certificateHolder.setCareUnitName(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        certificateHolder.setCareGiverId(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        certificateHolder.setSigningDoctorName(utlatande.getGrundData().getSkapadAv().getFullstandigtNamn());
        certificateHolder.setCivicRegistrationNumber(utlatande.getGrundData().getPatient().getPersonId());
        certificateHolder.setSignedDate(utlatande.getGrundData().getSigneringsdatum());
        certificateHolder.setType(SjukersattningEntryPoint.MODULE_ID);
        certificateHolder.setDocument(document);
        return certificateHolder;
    }

    public SjukersattningUtlatande fromJsonString(String s) throws ModuleException {
        try {
            return objectMapper.readValue(s, SjukersattningUtlatande.class);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    public String toJsonString(SjukersattningUtlatande utlatande) throws ModuleException {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        return writer.toString();
    }

}
