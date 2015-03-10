package se.inera.certificate.modules.ts_bas.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.intygstjanster.ts.services.v1.IntygMeta;
import se.intygstjanster.ts.services.v1.IntygStatus;
import se.intygstjanster.ts.services.v1.TSBasIntyg;

public class TsBasMetaDataConverter {
    public static CertificateMetaData toCertificateMetaData(IntygMeta intygMeta, TSBasIntyg intyg) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(intyg.getIntygsId());
        metaData.setCertificateType(intyg.getIntygsTyp());
        //metaData.setValidFrom(intygMeta.getValidFrom());
        //metaData.setValidTo(intygMeta.getValidTo());
        metaData.setIssuerName(intyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        metaData.setFacilityName(intyg.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        metaData.setSignDate(LocalDateTime.parse(intyg.getGrundData().getSigneringsTidstampel()));
        metaData.setAdditionalInfo(intygMeta.getAdditionalInfo());
        metaData.setAvailable(intygMeta.getAvailable().toLowerCase().equals("true"));
        List<Status> statuses = toStatusList(intygMeta.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }

    public static List<Status> toStatusList(List<IntygStatus> certificateStatuses) {
        List<Status> statuses = new ArrayList<>(certificateStatuses.size());
        for (IntygStatus certificateStatus : certificateStatuses) {
            statuses.add(toStatus(certificateStatus));
        }
        return statuses;
    }

    public static Status toStatus(IntygStatus certificateStatus) {
        Status status = new Status(CertificateState.valueOf(certificateStatus.getType().value()),
                certificateStatus.getTarget(),
                LocalDateTime.parse(certificateStatus.getTimestamp()));
        return status;
    }
}
