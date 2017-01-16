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
package se.inera.intyg.common.luae_na.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.luae_na.model.internal.LuaenaUtlatande;
import se.inera.intyg.common.services.texts.IntygTextsServiceImpl;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Generate variants of a LUAENA pdf, partly to see that make sure no exceptions occur but mainly for manual visual inspection
 * of the resulting pdf files, as we don't have any way of programmatically assert the content of the pdf.
 * 
 * @author marced
 */
public class LuaenaPdfDefinitionBuilderTest {

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private IntygTextsServiceImpl intygTextsService;
    private List<LuaenaUtlatande> intygList = new ArrayList<>();

    private LuaenaPdfDefinitionBuilder luaenaPdfDefinitionBuilder = new LuaenaPdfDefinitionBuilder();
    private IntygTexts intygTexts;

    @Before
    public void initTexts() throws IOException {
        intygTextsService = new IntygTextsServiceImpl();
        IntygTextsLuaenaRepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsLuaenaRepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        ReflectionTestUtils.setField(intygTextsService, "repo", intygsTextRepositoryHelper);
        intygTextsService.getIntygTextsPojo("luae_na", "1.0");

        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/minimalt_utlatande.json").getFile(), LuaenaUtlatande.class));
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/fullt_utlatande.json").getFile(), LuaenaUtlatande.class));
        intygList.add(objectMapper.readValue(new ClassPathResource("PdfGeneratorTest/overflow_utlatande.json").getFile(), LuaenaUtlatande.class));

        intygTexts = intygTextsService.getIntygTextsPojo("luae_na", "1.0");
    }

    @Test
    public void testGenerateNotSentToFK() throws Exception {
        generate("unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT);
    }

    @Test
    public void testGenerateAlreadySentTOFK() throws Exception {
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, PartKod.FKASSA.getValue(), LocalDateTime.now()));

        generate("sent", statuses, ApplicationOrigin.MINA_INTYG);
        generate("sent", statuses, ApplicationOrigin.WEBCERT);
    }

    private void generate(String scenarioName, List<Status> statuses, ApplicationOrigin origin) throws PdfGeneratorException, IOException {
        for (LuaenaUtlatande intyg : intygList) {
            FkPdfDefinition foo = luaenaPdfDefinitionBuilder.buildPdfDefinition(intyg, statuses, origin, intygTexts);
            byte[] generatorResult = PdfGenerator
                    .generatePdf(luaenaPdfDefinitionBuilder.buildPdfDefinition(intyg, statuses, origin, intygTexts));

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

        File file = new File(String.format("%s/%s-%s-%s-%s", dir, origin.name(), scenarioName, namingPrefix, "luae_na.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

}
