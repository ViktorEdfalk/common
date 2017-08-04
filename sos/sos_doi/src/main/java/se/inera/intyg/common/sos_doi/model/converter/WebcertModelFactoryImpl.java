package se.inera.intyg.common.sos_doi.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

public class WebcertModelFactoryImpl implements WebcertModelFactory<DoiUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Override
    public DoiUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        DoiUtlatande.Builder template = DoiUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);

        template.setGrundData(grundData);
        template.setTextVersion("1.0");

        return template.build();
    }

    @Override
    public DoiUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        // We only handle copy from DbUtlatande
        if (!SosUtlatande.class.isInstance(template) || DoiUtlatande.class.isInstance(template)) {
            throw new ConverterException("Template is not of correct type");
        }
        SosUtlatande sosUtlatande = (SosUtlatande) template;
        DoiUtlatande.Builder builder = DoiUtlatande.builder();
        GrundData grundData = sosUtlatande.getGrundData();

        populateWithId(builder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInCopy(grundData);

        builder.setGrundData(grundData);
        builder.setTextVersion("1.0");

        builder.setIdentitetStyrkt(sosUtlatande.getIdentitetStyrkt());
        builder.setDodsdatumSakert(sosUtlatande.getDodsdatumSakert());
        builder.setDodsdatum(new InternalDate(sosUtlatande.getDodsdatum().getDate()));
        if (sosUtlatande.getAntraffatDodDatum() != null) {
            builder.setAntraffatDodDatum(new InternalDate(sosUtlatande.getAntraffatDodDatum().getDate()));
        }
        builder.setDodsplatsKommun(sosUtlatande.getDodsplatsKommun());
        builder.setDodsplatsBoende(sosUtlatande.getDodsplatsBoende());
        builder.setBarn(sosUtlatande.getBarn());

        return builder.build();
    }

    private void populateWithId(DoiUtlatande.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.nullToEmpty(utlatandeId).trim().isEmpty()) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }
}
