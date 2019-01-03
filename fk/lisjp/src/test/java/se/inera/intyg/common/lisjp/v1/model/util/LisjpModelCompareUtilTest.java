/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.model.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1.Builder;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;

@RunWith(MockitoJUnitRunner.class)
public class LisjpModelCompareUtilTest {

    public static final String CORRECT_DIAGNOSKOD_FROM_FILE = "S47";
    public static final String CORRECT_DIAGNOSKOD2 = "A00-";

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private WebcertModuleService moduleService;

    @InjectMocks
    private LisjpModelCompareUtil modelCompareUtil;

    @Before
    public void setup() {
        Answer<Boolean> mockAnswer = new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) {
               String codeFragment = (String) invocation.getArguments()[0];
               return CORRECT_DIAGNOSKOD_FROM_FILE.equals(codeFragment) || CORRECT_DIAGNOSKOD2.equals(codeFragment);
            }
        };
        doAnswer(mockAnswer).when(moduleService).validateDiagnosisCode(anyString(), anyString());
    }

    @Test
    public void testModelIsValid() throws Exception {
        LisjpUtlatandeV1 utlatande = getUtlatandeFromFile("utlatande");
        assertTrue(modelCompareUtil.isValidForNotification(utlatande));
    }

    @Test
    public void testModelIsValidDiagnoskodChanged() throws Exception {
        LisjpUtlatandeV1 utlatandeOld = getUtlatandeFromFile("utlatande");
        Builder builder = utlatandeOld.toBuilder();
        LisjpUtlatandeV1 utlatandeNew = builder.setDiagnoser(Arrays.asList(Diagnos.create("A00-", "KSH_97_P", "Kolera", ""))).build();

        assertTrue(modelCompareUtil.isValidForNotification(utlatandeNew));
    }

    @Test
    public void testModelIsInvalidDiagnosInvalid() throws Exception {
        LisjpUtlatandeV1 utlatandeOld = getUtlatandeFromFile("utlatande");
        Builder builder = utlatandeOld.toBuilder();
        LisjpUtlatandeV1 utlatandeNew = builder.setDiagnoser(Arrays.asList(Diagnos.create("BLAH", "ICD_10_SE", "Klämskada skuldra", ""))).build();

        assertFalse(modelCompareUtil.isValidForNotification(utlatandeNew));
    }

    @Test
    public void testModelIsInvalidWithInvalidDate() throws Exception {
        LisjpUtlatandeV1 utlatandeOld = getUtlatandeFromFile("utlatande");
        Builder builder = utlatandeOld .toBuilder();
        LisjpUtlatandeV1 utlatandeNew = builder.setTelefonkontaktMedPatienten(new InternalDate("2016-1")).build();

        assertFalse(modelCompareUtil.isValidForNotification(utlatandeNew));
    }

    @Test
    public void testModelIsInvalidWithInvalidSjukskrivningsperiod() throws Exception {
        LisjpUtlatandeV1 utlatandeOld = getUtlatandeFromFile("utlatande");
        Builder builder = utlatandeOld .toBuilder();
        LisjpUtlatandeV1 utlatandeNew = builder.setSjukskrivningar(Arrays.asList(
                Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval("2016-10-10", "2016-")))).build();

        assertFalse(modelCompareUtil.isValidForNotification(utlatandeNew));
    }

    private LisjpUtlatandeV1 getUtlatandeFromFile(String file) throws IOException {
        return objectMapper.readValue(new ClassPathResource(
                "v1/LisjpModelCompareUtil/" + file + ".json").getFile(), LisjpUtlatandeV1.class);
    }
}
