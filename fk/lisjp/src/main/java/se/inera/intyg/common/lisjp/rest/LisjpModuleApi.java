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
package se.inera.intyg.common.lisjp.rest;

import java.util.List;
import java.util.stream.Collectors;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.lisjp.model.converter.*;
import se.inera.intyg.common.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class LisjpModuleApi extends FkParentModuleApi<LisjpUtlatande> {
    public LisjpModuleApi() {
        super(LisjpUtlatande.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> optionalFields)
            throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected String getSchematronFileName() {
        return LisjpEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LisjpUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected LisjpUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LisjpUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected LisjpUtlatande decorateDiagnoserWithDescriptions(LisjpUtlatande utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
                .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                        moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }
}
