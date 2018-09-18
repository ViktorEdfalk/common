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
package se.inera.intyg.common.ts_diabetes_2.model.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande;
import se.inera.intyg.common.ts_diabetes_2.model.kodverk.KvTypAvDiabetes;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

public class TransportToInternalTest {

    public static TsDiabetes2Utlatande getUtlatande() {
        TsDiabetes2Utlatande.Builder utlatande = TsDiabetes2Utlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");

        utlatande.setAllmant(Allmant.builder().build());
        utlatande.setBedomning(Bedomning.builder().build());
        utlatande.setHypoglykemier(Hypoglykemier.builder().build());
        utlatande.setSynfunktion(Synfunktion.builder().build());

        utlatande.setOvrigt("Trevlig kille");
        return utlatande.build();
    }

    @Test
    public void endToEnd() throws Exception {
        TsDiabetes2Utlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        TsDiabetes2Utlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

}