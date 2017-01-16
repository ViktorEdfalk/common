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
package se.inera.intyg.common.fkparent.pdf.eventhandlers;

import com.google.common.io.ByteStreams;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Generic fk-logo stamper. It's rendered at a fixed position, but page interval is configurable.
 */
public class FkLogoEventHandler extends PdfPageEventHelper {
    private static String logoPath = "images/forsakringskassans_logotyp.jpg";
    private static final float LINEAR_SCALE = 0.253f * 100f;
    private static final float LEFT_OFFSET = Utilities.millimetersToPoints(16f);
    private static final float TOP_OFFSET = Utilities.millimetersToPoints(20f);

    private int activeFromPage;
    private int activeToPage;
    private float offsetX;
    private float offsetY;
    private Image fkLogo = null;

    /**
     * Constructs an Eventhandler for rendering the FK logotype on the specified pages.
     *
     * @param activeFromPage
     *      From page number to render FK logo, inclusive. 1-indexed.
     * @param activeToPage
     *      To page number.
     * @throws DocumentException
     *      If the image couldn't be read or other iText-related exception.
     */
    public FkLogoEventHandler(int activeFromPage, int activeToPage) throws DocumentException {
         this(activeFromPage, activeToPage, 0f, 0f);
    }

    /**
     * Constructs an Eventhandler for rendering the FK logotype on the specified pages, with adjustable offsets.
     *
     * @param activeFromPage
     *      From page number to render FK logo, inclusive. 1-indexed.
     * @param activeToPage
     *      To page number.
     * @param offsetX
     *      The default X offset is {@link FkLogoEventHandler#LEFT_OFFSET} (16 mm), but since this may vary slightly amongst
     *      FK intyg, one can optionally adjust this offset by the supplied number of millimeters. E.g. - to move the FK
     *      logotype 2 millimeters to the left, supplied -2.0f as offsetX.
     * @param offsetY
     *      The default Y offset, see offsetX for details.
     * @throws DocumentException
     *      If the image couldn't be read or other iText-related exception.
     */
    public FkLogoEventHandler(int activeFromPage, int activeToPage, float offsetX, float offsetY) throws DocumentException {
        this.activeFromPage = activeFromPage;
        this.activeToPage = activeToPage;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        try {
            Resource resource = new ClassPathResource(logoPath);
            fkLogo = Image.getInstance(ByteStreams.toByteArray(resource.getInputStream()));
            fkLogo.scalePercent(LINEAR_SCALE);
        } catch (IOException e) {
            throw new DocumentException("Unable to initialise FkLogoEventHandler: " + e.getMessage());
        }
    }

    /**
     * Adds a FK logo to every page in the from-tom interval.
     *
     * @see PdfPageEventHelper#onEndPage(PdfWriter,
     *      Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (writer.getPageNumber() >= activeFromPage && writer.getPageNumber() <= activeToPage) {

            try {
                fkLogo.setAbsolutePosition(LEFT_OFFSET + Utilities.millimetersToPoints(offsetX), document.getPageSize().getTop() - TOP_OFFSET + Utilities.millimetersToPoints(offsetY));
                writer.getDirectContent().addImage(fkLogo);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
