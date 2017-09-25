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
package se.inera.intyg.common.ts_bas.model.converter;

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
import se.inera.intyg.common.ts_bas.model.internal.TsBasUtlatande;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactoryImpl implements WebcertModelFactory<TsBasUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new TS-bas draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     *
     * @return {@link TsBasUtlatande}
     *
     * @throws se.inera.intyg.common.support.model.converter.util.ConverterException
     *             if something unforeseen happens
     */
    @Override
    public TsBasUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
        TsBasUtlatande template = new TsBasUtlatande();

        template.setId(newDraftData.getCertificateId());
        template.setTyp(TsBasEntryPoint.MODULE_ID);
        template.setTextVersion(intygTexts.getLatestVersion(TsBasEntryPoint.MODULE_ID));

        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(template.getGrundData(), newDraftData);

        return template;
    }

    @Override
    public TsBasUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!TsBasUtlatande.class.isInstance(template)) {
            throw new ConverterException("Template is not of type TsBasUtlatande");
        }
        TsBasUtlatande tsBasUtlatande = (TsBasUtlatande) template;
        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsBasUtlatande.getId());

        populateWithId(tsBasUtlatande, copyData.getCertificateId());
        GrundData grundData = tsBasUtlatande.getGrundData();
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);
        resetDataInCopy(grundData);
        return tsBasUtlatande;
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }

    private void populateWithId(TsBasUtlatande utlatande, String utlatandeId) throws ConverterException {
        if (Strings.isNullOrEmpty(utlatandeId)) {
            throw new ConverterException("No certificateID found");
        }

        utlatande.setId(utlatandeId);
    }

}
