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
package se.inera.intyg.common.luae_fs.pdf;

import autovalue.shaded.org.apache.commons.lang.StringUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.internal.Tillaggsfraga;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.pdf.FkBasePdfDefinitionBuilder;
import se.inera.intyg.common.fkparent.pdf.PdfConstants;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkDynamicPageDecoratorEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkFormIdentityEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkFormPagePersonnummerEventHandlerImpl;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkLogoEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkOverflowPagePersonnummerEventHandlerImpl;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkPrintedByEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.PageNumberingEventHandler;
import se.inera.intyg.common.fkparent.pdf.model.FkCheckbox;
import se.inera.intyg.common.fkparent.pdf.model.FkDiagnosKodField;
import se.inera.intyg.common.fkparent.pdf.model.FkFieldGroup;
import se.inera.intyg.common.fkparent.pdf.model.FkLabel;
import se.inera.intyg.common.fkparent.pdf.model.FkOverflowPage;
import se.inera.intyg.common.fkparent.pdf.model.FkOverflowableValueField;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.pdf.model.FkTillaggsFraga;
import se.inera.intyg.common.fkparent.pdf.model.FkValueField;
import se.inera.intyg.common.fkparent.pdf.model.PdfComponent;
import se.inera.intyg.common.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Contructs a object graph of PdfComponents that represents a LUAE_FS intyg.
 * Created by eriklup on 16/01/17.
 */
// CHECKSTYLE:OFF MagicNumber
// CHECKSTYLE:OFF MethodLength
public class LuaefsPdfDefinitionBuilder extends FkBasePdfDefinitionBuilder {

    private static final float KATEGORI_FULL_WIDTH = 180f;
    private static final float KATEGORI_OFFSET_X = 15f;

    private static final float FRAGA_9_DELFRAGA_HEIGHT = 23.5f;

    private static final float CHECKBOXROW_DEFAULT_HEIGHT = 9f;
    private static final float CHECKBOX_DEFAULT_WIDTH = 72.2f;

    public FkPdfDefinition buildPdfDefinition(LuaefsUtlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin, IntygTexts intygTexts)
            throws PdfGeneratorException {
        this.intygTexts = intygTexts;

        try {
            FkPdfDefinition def = new FkPdfDefinition();

            // Add page envent handlers
            def.addPageEvent(new PageNumberingEventHandler(180.3f, 6.4f));
            def.addPageEvent(new FkFormIdentityEventHandler(intygTexts.getProperties().getProperty(PROPERTY_KEY_FORMID), intygTexts.getProperties().getProperty(PROPERTY_KEY_BLANKETT_ID), intygTexts.getProperties().getProperty(PROPERTY_KEY_BLANKETT_VERSION)));
            def.addPageEvent(new FkFormPagePersonnummerEventHandlerImpl(intyg.getGrundData().getPatient().getPersonId().getPersonnummer(), -2.0f, 0.0f));
            def.addPageEvent(new FkOverflowPagePersonnummerEventHandlerImpl(intyg.getGrundData().getPatient().getPersonId().getPersonnummer()));
            def.addPageEvent(new FkPrintedByEventHandler(intyg.getId(), getPrintedByText(applicationOrigin)));

            def.addPageEvent(new FkLogoEventHandler(1, 1, -3f, 0.8f));
            def.addPageEvent(new FkLogoEventHandler(3, 99));
            def.addPageEvent(new FkDynamicPageDecoratorEventHandler(3, def.getPageMargins(), "Läkarutlåtande", "för aktivitetsersättning vid förlängd skolgång"));

            def.addChild(createPage1(intyg, statuses, applicationOrigin));
            def.addChild(createPage2(intyg));

            // Only add tillaggsfragor page if there are some
            if (intyg.getTillaggsfragor().size() > 0) {
                def.addChild(createPage5(intyg));
            }

            // Always add the overflow page last, as it will scan the model for overflowing content and must therefore
            // also be renderad last.
            def.addChild(new FkOverflowPage("Fortsättningsblad, svar med hänvisning fortsätter nedan", def));

            return def;
        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Failed to create FkPdfDefinition", e);
        }

    }

    private FkPage createPage1(LuaefsUtlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin) throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();

        boolean showFkAddress;
        if (applicationOrigin.equals(ApplicationOrigin.MINA_INTYG)) {
            // we never include FK address in MI prints..
            showFkAddress = false;
        } else {
            showFkAddress = !isSentToFk(statuses);
        }
        addPage1MiscFields(intyg, showFkAddress, allElements);

        // START KATEGORI 1. (Utlåtandet är baserat på....)
        FkFieldGroup fraga1 = new FkFieldGroup("1. " + getText("FRG_1.RBK"))
                .offset(KATEGORI_OFFSET_X, 160.8f)
                .size(KATEGORI_FULL_WIDTH, 52f)
                .withBorders(Rectangle.BOX);

        // Use a variable for yOffset to minimize use of magic numbers for offsets
        float checkBoxYOffset = 2;

        // Special case of first topLabel
        fraga1.addChild(new FkValueField("")
                .offset(CHECKBOX_DEFAULT_WIDTH, 0f)
                .size(CHECKBOX_DEFAULT_WIDTH, 4f)
                .withTopLabel("datum (år, månad, dag)"));

        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.UNDERSOKNING.RBK"), intyg.getUndersokningAvPatienten() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getUndersokningAvPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.JOURNALUPPGIFTER.RBK"), intyg.getJournaluppgifter() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getJournaluppgifter()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.ANHORIG.RBK"), intyg.getAnhorigsBeskrivningAvPatienten() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getAnhorigsBeskrivningAvPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.ANNAT.RBK"), intyg.getAnnatGrundForMU() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getAnnatGrundForMU()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)

                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        fraga1.addChild(new FkLabel(getText("DFR_1.3.RBK"))
                .offset(7f, 36f)
                .size(58f, 9f)
                .withVerticalAlignment(Element.ALIGN_MIDDLE));
        fraga1.addChild(new FkOverflowableValueField(nullSafeString(intyg.getAnnatGrundForMUBeskrivning()), getText("DFR_1.3.RBK"))
                .offset(CHECKBOX_DEFAULT_WIDTH, 37f)
                .size(KATEGORI_FULL_WIDTH - CHECKBOX_DEFAULT_WIDTH, 6f));

        // Dessa fält har top border
        // FRG_2.RBK
        fraga1.addChild(new FkLabel(getText("FRG_2.RBK"))
                .offset(0f, 42.8f)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withBorders(Rectangle.TOP)
                .withVerticalAlignment(Element.ALIGN_BOTTOM));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getKannedomOmPatient()))
                .offset(CHECKBOX_DEFAULT_WIDTH, 42.8f)
                .size(107.8f, CHECKBOXROW_DEFAULT_HEIGHT)
                .withBorders(Rectangle.TOP));

        allElements.add(fraga1);

        FkFieldGroup fraga2 = new FkFieldGroup("2. " + getText("FRG_3.RBK"))
                .offset(KATEGORI_OFFSET_X, 226f)
                .size(KATEGORI_FULL_WIDTH, 63f)
                .withBorders(Rectangle.BOX);
        fraga2.addChild(new FkCheckbox("Nej", intyg.getUnderlagFinns() != null && !intyg.getUnderlagFinns())
                .offset(1f, 0)
                .withCellWith(6f)
                .size(20.5f, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkCheckbox("Ja, fyll i nedan.", safeBoolean(intyg.getUnderlagFinns()))
                .offset(22.5f, 0)
                .size(40f, CHECKBOXROW_DEFAULT_HEIGHT));

        // This is just so that we dont have to enter all y offsets manually
        float yOffset;
        int row = 1;

        // Need to always iterate 3 times even if just 1 underlag exists (to draw the empty form parts)
        for (int i = 0; i < 3; i++) {
            String label = "";
            String datum = "";
            String hamtasFran = null;

            if (i < intyg.getUnderlag().size()) {
                Underlag underlag = intyg.getUnderlag().get(i);
                label = underlag.getTyp() != null ? underlag.getTyp().getLabel() : "";
                datum = underlag.getDatum() != null ? underlag.getDatum().getDate() : "";
                hamtasFran = underlag.getHamtasFran();
            }

            yOffset = CHECKBOXROW_DEFAULT_HEIGHT * row;
            // Ange utredning eller underlag
            fraga2.addChild(new FkValueField(label)
                    .offset(0, yOffset)
                    .size(72.3f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel(getText("FRG_4.RBK")));

            fraga2.addChild(new FkValueField(datum)
                    .offset(72.3f, yOffset)
                    .size(50f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel("datum (år, månad, dag"));

            // Bifogas Ja/Nej är bara till för manuell/fysisk/pappers ifyllnad och skall därför alltid vara ej ifylld i
            // vårt fall.
            fraga2.addChild(new FkLabel("Bifogas")
                    .offset(120f, yOffset)
                    .size(15f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Ja", false)
                    .offset(132.5f, yOffset)
                    .size(30f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Nej", false)
                    .offset(155f, yOffset)
                    .size(25f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP));
            row++;
            yOffset = CHECKBOXROW_DEFAULT_HEIGHT * row;

            // Från vilken vårdgivare kan Försäkringskassan hämta information om utredningen/underlaget?
            fraga2.addChild(new FkValueField(nullSafeString(hamtasFran))
                    .offset(0, yOffset)
                    .size(KATEGORI_FULL_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel(getText("DFR_4.3.RBK")));
            row++;
        }
        allElements.add(fraga2);
        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private void addPage1MiscFields(LuaefsUtlatande intyg, boolean showFkAddress, List<PdfComponent> allElements) throws IOException {
        // Meta information text(s) etc.

        FkLabel elektroniskKopia = new FkLabel(PdfConstants.ELECTRONIC_COPY_WATERMARK_TEXT)
                .offset(18f, 60f)
                .withHorizontalAlignment(PdfPCell.ALIGN_CENTER)
                .withVerticalAlignment(Element.ALIGN_MIDDLE)
                .size(70f, 12f)
                .withFont(PdfConstants.FONT_BOLD_9)
                .withBorders(Rectangle.BOX, BaseColor.RED);
        allElements.add(elektroniskKopia);

        FkLabel fortsBladText = new FkLabel("Använd fortsättningsbladet som finns i slutet av blanketten om utrymmet i fälten inte räcker till.")
                .offset(15f, 20.5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(67f, 10f)
                .withLeading(.0f, 1.2f);
        allElements.add(fortsBladText);

        FkLabel inteKannerPatientenText = new FkLabel("Om du inte känner patienten ska hen styrka sin\nidentitet genom legitimation med foto (SOSFS 2005:29).")
                .offset(15f, 31.5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(76f, 10f)
                .withLeading(.0f, 1.2f);
        allElements.add(inteKannerPatientenText);

        FkLabel mainHeader = new FkLabel("Läkarutlåtande")
                .offset(105.5f, 9.5f)
                .size(40, 12f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_FRAGERUBRIK);
        FkLabel subHeader = new FkLabel("för aktivitetsersättning vid förlängd skolgång")
                .offset(105.5f, 14f)
                .size(80, 15)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_BOLD_9);
        allElements.add(mainHeader);
        allElements.add(subHeader);

        FkLabel patientNamnLbl = new FkLabel("Patientens namn")
                .offset(105.5f, 18f)
                .size(62.5f, 15)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        FkLabel patientPnrLbl = new FkLabel("Personnummer")
                .offset(168f, 18f)
                .size(35f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        allElements.add(patientNamnLbl);
        allElements.add(patientPnrLbl);

        FkLabel patientNamn = new FkLabel(intyg.getGrundData().getPatient().getFullstandigtNamn())
                .offset(105.5f, 22f)
                .size(62.5f, 15)
                .withFont(PdfConstants.FONT_VALUE_TEXT)
                .withVerticalAlignment(Element.ALIGN_TOP);
        FkLabel patientPnr = new FkLabel(intyg.getGrundData().getPatient().getPersonId().getPersonnummer())
                .offset(168f, 22f)
                .size(35f, 15f).withFont(PdfConstants.FONT_VALUE_TEXT)
                .withVerticalAlignment(Element.ALIGN_TOP);
        allElements.add(patientNamn);
        allElements.add(patientPnr);

        if (showFkAddress) {
            FkLabel skickaBlankettenTillLbl = new FkLabel("Skicka blanketten till")
                    .offset(111.7f, 36f)
                    .size(35f, 5f)
                    .withVerticalAlignment(Element.ALIGN_TOP)
                    .withFont(PdfConstants.FONT_STAMPER_LABEL);
            allElements.add(skickaBlankettenTillLbl);

            FkLabel inlasningsCentralRad1 = new FkLabel("Försäkringskassans inläsningscentral")
                    .withVerticalAlignment(Element.ALIGN_TOP)
                    .withFont(PdfConstants.FONT_VALUE_TEXT)
                    .size(90, 6f)
                    .offset(111.7f, 40.5f);
            FkLabel inlasningsCentralRad2 = new FkLabel("839 88 Östersund")
                    .withFont(PdfConstants.FONT_VALUE_TEXT)
                    .withVerticalAlignment(Element.ALIGN_TOP)
                    .size(60f, 6f)
                    .offset(111.7f, 47.25f);

            allElements.add(inlasningsCentralRad1);
            allElements.add(inlasningsCentralRad2);
        }
        // Ledtext: Vad är aktivitetsersättning vid nedsatt arbetsförmåga?
        FkLabel luaefsDescriptonText = new FkLabel(getText("FRM_2.RBK"))
                .withLeading(0.0f, 1.2f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .offset(17.5f, 82.5f)
                .size(174f, 60f)
                .backgroundColor(244, 244, 244)
                .backgroundRounded(true);

        allElements.add(luaefsDescriptonText);

    }

    private FkPage createPage2(LuaefsUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Diagnos/diagnoser för sjukdom som orsakar funktionsnedsattning
        FkFieldGroup fraga3 = new FkFieldGroup("3. " + getText("FRG_6.RBK"))
                .offset(KATEGORI_OFFSET_X, 26f).size(KATEGORI_FULL_WIDTH, 29f)
                .withBorders(Rectangle.BOX);
        Diagnos currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 0);
        FkValueField diagnos1 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(140f, 11f)
                .withTopPadding(2f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);

        // Diagnoskod enligt ICD-10 SE
        FkDiagnosKodField diagnosKodField1 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .withTopLabel(getText("DFR_6.2.RBK"))
                .size(40f, 11f).offset(140f, 0f).withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos1);
        fraga3.addChild(diagnosKodField1);

        currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 1);
        FkValueField diagnos2 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(140f, 9f).offset(0f, 11f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP).withBorders(Rectangle.BOX);
        FkDiagnosKodField diagnosKodField2 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .size(40f, 9f).offset(140f, 11f).withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos2);
        fraga3.addChild(diagnosKodField2);

        currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 2);
        FkValueField diagnos3 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(140f, 9f).offset(0f, 20f)
                .withBorders(Rectangle.BOX).withValueTextAlignment(PdfPCell.ALIGN_TOP);
        FkDiagnosKodField diagnosKodField3 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .size(40f, 9f).offset(140f, 20f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos3);
        fraga3.addChild(diagnosKodField3);

        allElements.add(fraga3);


        // Fraga 4. Funktionsnedsattning
        FkFieldGroup fraga4 = new FkFieldGroup("4. " + getText("FRG_6.RBK"))
                .offset(KATEGORI_OFFSET_X, 66.5f).size(KATEGORI_FULL_WIDTH, 58.5f)
                .withBorders(Rectangle.BOX);

        // När och var ställdes diagnosen/diagnoserna?
        FkOverflowableValueField narOchHur = new FkOverflowableValueField(intyg.getFunktionsnedsattningDebut(), getText("FRG_15.RBK"))
                .size(KATEGORI_FULL_WIDTH, 29.25f).offset(0f, 0f)
                .withBorders(Rectangle.BOTTOM)
                .showLabelOnTop();
        fraga4.addChild(narOchHur);

        FkOverflowableValueField paverkan = new FkOverflowableValueField(intyg.getFunktionsnedsattningPaverkan(), getText("FRG_16.RBK"))
                .size(KATEGORI_FULL_WIDTH, 29.25f).offset(0f, 29.25f)
                .withBorders(Rectangle.BOTTOM)
                .showLabelOnTop();
        fraga4.addChild(paverkan);

        allElements.add(fraga4);



        // Fraga 5. Övriga upplysningar --------------------------------------------------------------------------
        FkFieldGroup fraga9 = new FkFieldGroup("5. " + getText("FRG_25.RBK"))
                .offset(KATEGORI_OFFSET_X, 136f)
                .size(KATEGORI_FULL_WIDTH, 20.5f)
                .withBorders(Rectangle.BOX);

        StringBuilder ovrigt = new StringBuilder();

        if (!StringUtils.isBlank(intyg.getMotiveringTillInteBaseratPaUndersokning())) {
            ovrigt.append("Motivering till varför utlåtandet inte baseras på undersökning av patienten: ")
                    .append(intyg.getMotiveringTillInteBaseratPaUndersokning())
                    .append("\n");
        }

        if (!StringUtils.isBlank(intyg.getOvrigt())) {
            ovrigt.append(intyg.getOvrigt());
        }

        // OBS: Övrigt fältet skall behålla radbytformattering eftersom detta kan vara sammanslaget med motiveringstext
        fraga9.addChild(new FkOverflowableValueField(ovrigt.toString(), getText("FRG_25.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, FRAGA_9_DELFRAGA_HEIGHT)
                .keepNewLines());

        allElements.add(fraga9);

        // Fraga 6. Kontakt med FK --------------------------------------------------------------------------
        FkFieldGroup fraga10 = new FkFieldGroup("6. " + getText("FRG_26.RBK"))
                .offset(KATEGORI_OFFSET_X, 166.7f)
                .size(KATEGORI_FULL_WIDTH, 22.5f)
                .withBorders(Rectangle.BOX);
        // Jag önskar att Försäkringskassan kontaktar mig
        fraga10.addChild(new FkCheckbox(getText("DFR_26.1.RBK"), safeBoolean(intyg.getKontaktMedFk()))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 9f)
                .withBorders(Rectangle.BOTTOM));
        fraga10.addChild(new FkOverflowableValueField(intyg.getAnledningTillKontakt(), getText("DFR_26.2.RBK"))
                .offset(0f, 9f)
                .size(KATEGORI_FULL_WIDTH, 15.5f)
                .showLabelOnTop());

        allElements.add(fraga10);

        // Fraga 7. Underskrift --------------------------------------------------------------------------
        FkFieldGroup fraga11 = new FkFieldGroup("7. Underskrift")
                .offset(KATEGORI_OFFSET_X, 201.2f)
                .size(KATEGORI_FULL_WIDTH, 78f)
                .withBorders(Rectangle.BOX);

        fraga11.addChild(new FkValueField(intyg.getGrundData().getSigneringsdatum().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .offset(0f, 0f)
                .size(45f, 11f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
                .withTopLabel("Datum"));
        fraga11.addChild(new FkValueField("")
                .offset(45f, 0f)
                .size(KATEGORI_FULL_WIDTH - 45f, 11f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withTopLabel("Läkarens namnteckning"));

        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getFullstandigtNamn())
                .offset(0f, 11f)
                .size(KATEGORI_FULL_WIDTH, 11f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withTopLabel("Namnförtydligande"));

        fraga11.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getBefattningar()))
                .offset(0f, 22f)
                .size(90f, 13f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
                .withTopLabel("Befattning"));
        fraga11.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getSpecialiteter()))
                .offset(90f, 22f)
                .size(KATEGORI_FULL_WIDTH - 90f, 13f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("Eventuell specialistkompetens"));
        // skapadAv.personId is always a hsa-id
        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getPersonId())
                .offset(0f, 35f)
                .size(90f, 9f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
                .withTopLabel("Läkarens HSA-id"));
        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getVardenhet().getArbetsplatsKod())
                .offset(90f, 35f)
                .size(KATEGORI_FULL_WIDTH - 90f, 9f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel("Arbetsplatskod"));
        // We only have an hsa-Id - so we never fill this field
        fraga11.addChild(new FkValueField("")
                .offset(0f, 44f)
                .size(KATEGORI_FULL_WIDTH, 9f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel("Läkarens personnummer. Anges endast om du som läkare saknar HSA-id."));

        fraga11.addChild(new FkValueField(buildVardEnhetAdress(intyg.getGrundData().getSkapadAv().getVardenhet()))
                .offset(0f, 54f)
                .size(KATEGORI_FULL_WIDTH, 22.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("Vårdenhetens namn, adress och telefon."));

        // Somewhat hacky, add a label outside the category box.
        fraga11.addChild(new FkLabel("Underskriften omfattar samtliga uppgifter i intyget")
                .offset(3f, 81.5f)
                .size(KATEGORI_FULL_WIDTH - 6, 10f)
                .withVerticalAlignment(PdfPCell.ALIGN_TOP)
                .backgroundColor(245, 245, 245)
                .backgroundRounded(true));

        allElements.add(fraga11);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

//    private FkPage createPage3(LuaefsUtlatande intyg) throws IOException, DocumentException {
//
//        List<PdfComponent> allElements = new ArrayList<>();
//
//        // Fraga 5. (Fortsattning fran sida 2 - ingen rubrik)
//        // --------------------------------------------------------------------------
//        float fraga5YOffset = 0;
//        FkFieldGroup fraga5 = new FkFieldGroup("")
//                .offset(KATEGORI_OFFSET_X, 19f)
//                .size(KATEGORI_FULL_WIDTH, 101f)
//                .withBorders(Rectangle.BOX);
//
//        // Sinnesfunktioner och smärta
//        fraga5.addChild(
//                new FkLabel(getText("FRG_12.RBK"))
//                        .offset(0, fraga5YOffset)
//                        .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
//                        .withBorders(Rectangle.BOX));
//        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;
//
//        fraga5.addChild(new FkOverflowableValueField(intyg.getFunktionsnedsattningSynHorselTal(), getText("FRG_12.RBK"))
//                .offset(0f, fraga5YOffset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT));
//        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;
//
//        // Balans, koordination och motorik
//        fraga5.addChild(new FkLabel(getText("FRG_13.RBK"))
//                .offset(0, fraga5YOffset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
//                .withBorders(Rectangle.BOX));
//        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;
//
//        fraga5.addChild(new FkOverflowableValueField(intyg.getFunktionsnedsattningBalansKoordination(), getText("FRG_13.RBK"))
//                .offset(0f, fraga5YOffset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT));
//        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;
//
//        // Annan kroppslig funktion
//        fraga5.addChild(new FkLabel(getText("FRG_14.RBK"))
//                .offset(0, fraga5YOffset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
//                .withBorders(Rectangle.BOX));
//        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;
//
//        fraga5.addChild(new FkOverflowableValueField(intyg.getFunktionsnedsattningAnnan(), getText("FRG_14.RBK"))
//                .offset(0f, fraga5YOffset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT));
//
//        allElements.add(fraga5);
//
//        // Fraga 6.Aktivitetsbegränsningar --------------------------------------------------------------------------
//        FkFieldGroup fraga6 = new FkFieldGroup("6. " + getText("FRG_17.RBK"))
//                .offset(KATEGORI_OFFSET_X, 138.5f)
//                .size(KATEGORI_FULL_WIDTH, 29.2f)
//                .withBorders(Rectangle.BOX);
//
//        // Ge konkreta exempel
//        fraga6.addChild(new FkOverflowableValueField(intyg.getAktivitetsbegransning(), getText("DFR_17.1.RBK"))
//                .offset(0f, 0f)
//                .size(KATEGORI_FULL_WIDTH, 32f)
//                .showLabelOnTop());
//
//        allElements.add(fraga6);
//
//        // Fraga 7.Medicinsk behandling --------------------------------------------------------------------------
//        FkFieldGroup fraga7 = new FkFieldGroup("7. " + getText("KAT_7.RBK"))
//                .offset(KATEGORI_OFFSET_X, 178.7f)
//                .size(KATEGORI_FULL_WIDTH, 110.2f)
//                .withBorders(Rectangle.BOX);
//
//        float fraga7Offset = 0f;
//        fraga7.addChild(new FkLabel(getText("FRG_18.RBK") + ". " + getText("DFR_18.1.RBK"))
//                .offset(0, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT + 2f));
//        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT + 2f;
//
//        fraga7.addChild(new FkOverflowableValueField(intyg.getAvslutadBehandling(), getText("FRG_18.RBK") + ". " + getText("DFR_18.1.RBK"))
//                .offset(0f, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
//                .withBorders(Rectangle.BOTTOM));
//        fraga7Offset += FRAGA_7_DELFRAGA_HEIGHT;
//
//        fraga7.addChild(new FkLabel(getText("FRG_19.RBK") + ". " + getText("DFR_19.1.RBK"))
//                .offset(0, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT));
//        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT;
//
//        fraga7.addChild(new FkOverflowableValueField(intyg.getPagaendeBehandling(), getText("FRG_19.RBK") + ". " + getText("DFR_19.1.RBK"))
//                .offset(0f, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
//                .withBorders(Rectangle.BOTTOM));
//        fraga7Offset += FRAGA_7_DELFRAGA_HEIGHT;
//
//        fraga7.addChild(new FkLabel(getText("FRG_20.RBK") + ". " + getText("DFR_20.1.RBK"))
//                .offset(0, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT));
//        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT;
//
//        fraga7.addChild(new FkOverflowableValueField(intyg.getPlaneradBehandling(), getText("FRG_20.RBK") + ". " + getText("DFR_20.1.RBK"))
//                .offset(0f, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
//                .withBorders(Rectangle.BOTTOM));
//        fraga7Offset += FRAGA_7_DELFRAGA_HEIGHT;
//
//        fraga7.addChild(new FkLabel(getText("FRG_21.RBK") + " (" + getText("DFR_21.1.RBK") + ")")
//                .offset(0, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT));
//        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT;
//
//        fraga7.addChild(new FkOverflowableValueField(intyg.getSubstansintag(), getText("FRG_21.RBK") + " (" + getText("DFR_21.1.RBK") + ")")
//                .offset(0f, fraga7Offset)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT));
//
//        allElements.add(fraga7);
//
//        FkPage thisPage = new FkPage();
//        thisPage.getChildren().addAll(allElements);
//        return thisPage;
//
//    }
//
//    private FkPage createPage4(LuaefsUtlatande intyg) throws IOException, DocumentException {
//
//        List<PdfComponent> allElements = new ArrayList<>();
//        // Fraga 8. Medicinska forutsattningar
//        // --------------------------------------------------------------------------
//        FkFieldGroup fraga8 = new FkFieldGroup("8. " + getText("KAT_8.RBK"))
//                .offset(KATEGORI_OFFSET_X, 26f)
//                .size(KATEGORI_FULL_WIDTH, 81f) // 27*3
//                .withBorders(Rectangle.BOX);
//
//        fraga8.addChild(new FkLabel(getText("FRG_22.RBK"))
//                .offset(0, 0)
//                .size(KATEGORI_FULL_WIDTH, 6.5f)
//                .withTopPadding(0.5f)
//                .withVerticalAlignment(PdfPCell.TOP));
//        fraga8.addChild(new FkOverflowableValueField(intyg.getMedicinskaForutsattningarForArbete(), getText("FRG_22.RBK"))
//                .offset(0f, 6.5f)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_8_DELFRAGA_HEIGHT - 6.5f));
//
//        fraga8.addChild(new FkLabel(getText("FRG_23.RBK"))
//                .offset(0, FRAGA_8_DELFRAGA_HEIGHT)
//                .size(KATEGORI_FULL_WIDTH, 4f)
//                .withTopPadding(0.5f)
//                .withVerticalAlignment(PdfPCell.TOP)
//                .withBorders(Rectangle.TOP));
//        fraga8.addChild(new FkOverflowableValueField(intyg.getFormagaTrotsBegransning(), getText("FRG_23.RBK"))
//                .offset(0f, FRAGA_8_DELFRAGA_HEIGHT + 4f)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_8_DELFRAGA_HEIGHT - 4f));
//
//        fraga8.addChild(new FkLabel(getText("FRG_24.RBK"))
//                .offset(0, FRAGA_8_DELFRAGA_HEIGHT * 2)
//                .size(KATEGORI_FULL_WIDTH, 4f)
//                .withTopPadding(0.5f)
//                .withVerticalAlignment(PdfPCell.TOP)
//                .withBorders(Rectangle.TOP));
//        fraga8.addChild(new FkOverflowableValueField(intyg.getFormagaTrotsBegransning(), getText("FRG_24.RBK"))
//                .offset(0f, FRAGA_8_DELFRAGA_HEIGHT * 2 + 4f)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_8_DELFRAGA_HEIGHT - 4f));
//
//        allElements.add(fraga8);
//
//        // Fraga 9. Övriga upplysningar --------------------------------------------------------------------------
//        FkFieldGroup fraga9 = new FkFieldGroup("9. " + getText("FRG_25.RBK"))
//                .offset(KATEGORI_OFFSET_X, 120.9f)
//                .size(KATEGORI_FULL_WIDTH, 22.5f)
//                .withBorders(Rectangle.BOX);
//
//        StringBuilder ovrigt = new StringBuilder();
//
//        if (!StringUtils.isBlank(intyg.getMotiveringTillInteBaseratPaUndersokning())) {
//            ovrigt.append("Motivering till varför utlåtandet inte baseras på undersökning av patienten: ")
//                    .append(intyg.getMotiveringTillInteBaseratPaUndersokning())
//                    .append("\n");
//        }
//
//        if (!StringUtils.isBlank(intyg.getOvrigt())) {
//            ovrigt.append(intyg.getOvrigt());
//        }
//
//        // OBS: Övrigt fältet skall behålla radbytformattering eftersom detta kan vara sammanslaget med motiveringstext
//        fraga9.addChild(new FkOverflowableValueField(ovrigt.toString(), getText("FRG_25.RBK"))
//                .offset(0f, 0f)
//                .size(KATEGORI_FULL_WIDTH, FRAGA_9_DELFRAGA_HEIGHT)
//                .keepNewLines());
//
//        allElements.add(fraga9);
//
//        // Fraga 10. Kontakt med FK --------------------------------------------------------------------------
//        FkFieldGroup fraga10 = new FkFieldGroup("10. " + getText("FRG_26.RBK"))
//                .offset(KATEGORI_OFFSET_X, 156.5f)
//                .size(KATEGORI_FULL_WIDTH, 22.5f)
//                .withBorders(Rectangle.BOX);
//        // Jag önskar att Försäkringskassan kontaktar mig
//        fraga10.addChild(new FkCheckbox(getText("DFR_26.1.RBK"), safeBoolean(intyg.getKontaktMedFk()))
//                .offset(0f, 0f)
//                .size(KATEGORI_FULL_WIDTH, 9f)
//                .withBorders(Rectangle.BOTTOM));
//        fraga10.addChild(new FkOverflowableValueField(intyg.getAnledningTillKontakt(), getText("DFR_26.2.RBK"))
//                .offset(0f, 9f)
//                .size(KATEGORI_FULL_WIDTH, 15.5f)
//                .showLabelOnTop());
//
//        allElements.add(fraga10);
//
//        // Fraga 11. Underskrift --------------------------------------------------------------------------
//        FkFieldGroup fraga11 = new FkFieldGroup("11. Underskrift")
//                .offset(KATEGORI_OFFSET_X, 192.8f)
//                .size(KATEGORI_FULL_WIDTH, 85.5f)
//                .withBorders(Rectangle.BOX);
//
//        fraga11.addChild(new FkValueField(intyg.getGrundData().getSigneringsdatum().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
//                .offset(0f, 0f)
//                .size(45f, 11f)
//                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
//                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
//                .withTopLabel("Datum"));
//        fraga11.addChild(new FkValueField("")
//                .offset(45f, 0f)
//                .size(KATEGORI_FULL_WIDTH - 45f, 11f)
//                .withBorders(Rectangle.BOTTOM)
//                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
//                .withTopLabel("Läkarens namnteckning"));
//
//        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getFullstandigtNamn())
//                .offset(0f, 11f)
//                .size(KATEGORI_FULL_WIDTH, 11f)
//                .withBorders(Rectangle.BOTTOM)
//                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
//                .withTopLabel("Namnförtydligande"));
//
//        fraga11.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getBefattningar()))
//                .offset(0f, 22f)
//                .size(90f, 13f)
//                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
//                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
//                .withTopLabel("Befattning"));
//        fraga11.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getSpecialiteter()))
//                .offset(90f, 22f)
//                .size(KATEGORI_FULL_WIDTH - 90f, 13f)
//                .withBorders(Rectangle.BOTTOM)
//                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
//                .withTopLabel("Eventuell specialistkompetens"));
//        // skapadAv.personId is always a hsa-id
//        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getPersonId())
//                .offset(0f, 35f)
//                .size(90f, 9f)
//                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
//                .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
//                .withTopLabel("Läkarens HSA-id"));
//        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getVardenhet().getArbetsplatsKod())
//                .offset(90f, 35f)
//                .size(KATEGORI_FULL_WIDTH - 90f, 9f)
//                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
//                .withBorders(Rectangle.BOTTOM)
//                .withTopLabel("Arbetsplatskod"));
//        // We only have an hsa-Id - so we never fill this field
//        fraga11.addChild(new FkValueField("")
//                .offset(0f, 44f)
//                .size(KATEGORI_FULL_WIDTH, 9f)
//                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
//                .withBorders(Rectangle.BOTTOM)
//                .withTopLabel("Läkarens personnummer. Anges endast om du som läkare saknar HSA-id."));
//
//        fraga11.addChild(new FkValueField(buildVardEnhetAdress(intyg.getGrundData().getSkapadAv().getVardenhet()))
//                .offset(0f, 54f)
//                .size(KATEGORI_FULL_WIDTH, 30f)
//                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
//                .withTopLabel("Vårdenhetens namn, adress och telefon."));
//
//        // Somewhat hacky, add a label outside the category box.
//        fraga11.addChild(new FkLabel("Underskriften omfattar samtliga uppgifter i intyget")
//                .offset(3f, 92f)
//                .size(KATEGORI_FULL_WIDTH - 6, 10f)
//                .withVerticalAlignment(PdfPCell.ALIGN_TOP)
//                .backgroundColor(245, 245, 245)
//                .backgroundRounded(true));
//
//        allElements.add(fraga11);
//
//        FkPage thisPage = new FkPage();
//        thisPage.getChildren().addAll(allElements);
//        return thisPage;
//    }

    private FkPage createPage5(LuaefsUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Sida 5 ar en extrasida, har lagger vi ev tillaggsfragor
        for (int i = 0; i < intyg.getTillaggsfragor().size(); i++) {
            Tillaggsfraga tillaggsfraga = intyg.getTillaggsfragor().get(i);
            allElements.add(new FkTillaggsFraga((i + 1) + ". " + getText("DFR_" + tillaggsfraga.getId() + ".1.RBK"), tillaggsfraga.getSvar()));
        }

        FkPage thisPage = new FkPage("Tilläggsfrågor");
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

}
// CHECKSTYLE:ON MagicNumber
// CHECKSTYLE:ON MethodLength
