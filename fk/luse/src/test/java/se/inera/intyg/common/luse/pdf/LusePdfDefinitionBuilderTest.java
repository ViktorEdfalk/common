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
package se.inera.intyg.common.luse.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.support.FkAbstractModuleEntryPoint;
import se.inera.intyg.common.luse.model.internal.LuseUtlatande;
import se.inera.intyg.common.services.texts.IntygTextsServiceImpl;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Generate variants of a LUSE pdf, partly to see that make sure no exceptions occur but mainly for manual visual inspection
 * of the resulting pdf files, as we don't have any way of programmatically assert the content of the pdf.
 *
 * @author marced
 */
@RunWith(MockitoJUnitRunner.class)
public class LusePdfDefinitionBuilderTest {

    private static final String FKASSA_RECIPIENT_ID = "FKASSA";
    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    private ObjectMapper objectMapper = new CustomObjectMapper();

    private IntygTextsServiceImpl intygTextsService;
    private List<LuseUtlatande> intygList = new ArrayList<>();

    @Mock
    FkAbstractModuleEntryPoint entryPoint;

    @InjectMocks
    private LusePdfDefinitionBuilder lusePdfDefinitionBuilder = new LusePdfDefinitionBuilder();
    private IntygTexts intygTexts;

    @Before
    public void initTexts() throws IOException {
        intygTextsService = new IntygTextsServiceImpl();
        IntygTextsLuseRepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsLuseRepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        ReflectionTestUtils.setField(intygTextsService, "repo", intygsTextRepositoryHelper);
        intygTextsService.getIntygTextsPojo("luse", "1.0");
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/utkast_utlatande.json").getFile(), LuseUtlatande.class));
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/minimalt_utlatande.json").getFile(), LuseUtlatande.class));
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/fullt_utlatande.json").getFile(), LuseUtlatande.class));
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/overfyllnad_utlatande.json").getFile(), LuseUtlatande.class));
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/overfyllnad_cornercases_utlatande.json").getFile(), LuseUtlatande.class));

        intygTexts = intygTextsService.getIntygTextsPojo("luse", "1.0");
    }

    @Test
    public void testGenerateNotSentToFK() throws Exception {
        generate("unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT);
    }

    @Test
    public void testGenerateAlreadySentTOFK() throws Exception {
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, FKASSA_RECIPIENT_ID, LocalDateTime.now()));

        generate("sent", statuses, ApplicationOrigin.MINA_INTYG);
        generate("sent", statuses, ApplicationOrigin.WEBCERT);
        statuses.add(new Status(CertificateState.CANCELLED, HSVARD_RECIPIENT_ID, LocalDateTime.now()));
        generate("sent-makulerat", statuses, ApplicationOrigin.WEBCERT);


    }

    private void generate(String scenarioName, List<Status> statuses, ApplicationOrigin origin) throws PdfGeneratorException, IOException {
        for (LuseUtlatande intyg : intygList) {
            FkPdfDefinition foo = lusePdfDefinitionBuilder.buildPdfDefinition(intyg, statuses, origin, intygTexts, false);
            byte[] generatorResult = PdfGenerator
                    .generatePdf(lusePdfDefinitionBuilder.buildPdfDefinition(intyg, statuses, origin, intygTexts, false));

            assertNotNull(generatorResult);

            writePdfToFile(generatorResult, origin, scenarioName, intyg.getId());
        }
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String scenarioName, String namingPrefix) throws IOException {
        String dir = "build/tmp";// TODO: System.getProperty("pdfOutput.dir") only existed in POM file - need to find a
                                 // way in gradle;
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s-%s-%s-%s", dir, origin.name(), scenarioName, namingPrefix, "luse.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

}
