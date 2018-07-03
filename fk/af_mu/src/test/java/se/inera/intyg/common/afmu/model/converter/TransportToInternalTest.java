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
package se.inera.intyg.common.afmu.model.converter;

import org.junit.Test;
import se.inera.intyg.common.afmu.model.internal.AfmuUtlatande;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

import static org.junit.Assert.assertEquals;

public class TransportToInternalTest {

    public static AfmuUtlatande getUtlatande() {
        AfmuUtlatande.Builder utlatande = AfmuUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setPlaneradBehandling("Mer medicin");

        utlatande.setFunktionsnedsattning("Funktionsnedsättning");

        utlatande.setOvrigt("Trevlig kille");
        return utlatande.build();
    }

    @Test
    public void endToEnd() throws Exception {
        AfmuUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        AfmuUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

}
