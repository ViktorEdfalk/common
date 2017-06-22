/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fk7263.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

/**
 * Factory for creating an editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new FK7263 draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link Fk7263Utlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Fk7263Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Fk7263Utlatande template = new Fk7263Utlatande();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(template.getGrundData(), newDraftData);
        resetDataInUtlatande(template);

        template.setNuvarandeArbete(true);
        template.setArbetsloshet(false);

        template.setAvstangningSmittskydd(false);
        template.setForaldrarledighet(false);
        template.setKontaktMedFk(false);
        template.setRekommendationKontaktArbetsformedlingen(false);
        template.setRekommendationKontaktForetagshalsovarden(false);
        template.setRessattTillArbeteAktuellt(false);
        template.setRessattTillArbeteEjAktuellt(false);

        return template;
    }

    public Fk7263Utlatande createCopy(CreateDraftCopyHolder copyData, Fk7263Utlatande template) throws ConverterException {

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), template.getId());

        populateWithId(template, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(template.getGrundData(), copyData);

        resetDataInUtlatande(template);

        return template;
    }

    private void populateWithId(Fk7263Utlatande utlatande, String utlatandeId) throws ConverterException {
        if (Strings.isNullOrEmpty(utlatandeId)) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInUtlatande(Fk7263Utlatande utlatande) {
        Patient patient = new Patient();
        patient.setPersonId(utlatande.getGrundData().getPatient().getPersonId());
        utlatande.getGrundData().setPatient(patient);

        utlatande.getGrundData().setSigneringsdatum(null);
    }
}
