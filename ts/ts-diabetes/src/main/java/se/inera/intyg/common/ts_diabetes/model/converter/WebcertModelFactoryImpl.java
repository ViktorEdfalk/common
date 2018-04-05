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
package se.inera.intyg.common.ts_diabetes.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.ts_diabetes.model.internal.TsDiabetesUtlatande;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactoryImpl implements WebcertModelFactory<TsDiabetesUtlatande> {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new TS-diabetes draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link TsDiabetesUtlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    @Override
    public TsDiabetesUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
        TsDiabetesUtlatande template = new TsDiabetesUtlatande();

        template.setId(newDraftData.getCertificateId());
        template.setTyp(TsDiabetesEntryPoint.MODULE_ID);
        template.setTextVersion(intygTexts.getLatestVersion(TsDiabetesEntryPoint.MODULE_ID));

        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(template.getGrundData(), newDraftData);

        return template;
    }

    @Override
    public TsDiabetesUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!TsDiabetesUtlatande.class.isInstance(template)) {
            throw new ConverterException("Template is not of type TsDiabetesUtlatande");
        }

        TsDiabetesUtlatande tsDiabetesUtlatande = (TsDiabetesUtlatande) template;

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsDiabetesUtlatande.getId());

        populateWithId(tsDiabetesUtlatande, copyData.getCertificateId());
        GrundData grundData = tsDiabetesUtlatande.getGrundData();
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);
        resetDataInCopy(grundData);
        return tsDiabetesUtlatande;
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }

    private void populateWithId(TsDiabetesUtlatande utlatande, String utlatandeId) throws ConverterException {
        if (Strings.isNullOrEmpty(utlatandeId)) {
            throw new ConverterException("No certificateID found");
        }

        utlatande.setId(utlatandeId);
    }

}
