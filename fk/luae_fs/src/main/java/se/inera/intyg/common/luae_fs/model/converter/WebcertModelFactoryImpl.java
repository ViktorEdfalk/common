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
package se.inera.intyg.common.luae_fs.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.common.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

/**
 * Factory for creating an editable model.
 */
public class WebcertModelFactoryImpl implements WebcertModelFactory<LuaefsUtlatande> {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new luae_fs draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link LuaefsUtlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    @Override
    public LuaefsUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        LuaefsUtlatande.Builder template = LuaefsUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);

        // Default to latest version available of intyg
        template.setTextVersion(intygTexts.getLatestVersion(LuaefsEntryPoint.MODULE_ID));

        return template.setGrundData(grundData).build();
    }

    @Override
    public LuaefsUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!LuaefsUtlatande.class.isInstance(template)) {
            throw new ConverterException("Template is not of type LuaefsUtlatande");
        }

        LuaefsUtlatande luaefsUtlatande = (LuaefsUtlatande) template;
        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), luaefsUtlatande.getId());

        LuaefsUtlatande.Builder templateBuilder = luaefsUtlatande.toBuilder();
        GrundData grundData = luaefsUtlatande.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInGrundData(grundData);

        return templateBuilder.build();
    }

    private void populateWithId(LuaefsUtlatande.Builder utlatande, String utlatandeId) throws ConverterException {
        if (utlatandeId == null || utlatandeId.trim().length() == 0) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInGrundData(GrundData grundData) {
        Patient patient = new Patient();
        patient.setPersonId(grundData.getPatient().getPersonId());
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(null);
    }
}
