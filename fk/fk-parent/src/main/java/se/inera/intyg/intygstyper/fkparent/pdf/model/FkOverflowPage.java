/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygstyper.fkparent.pdf.model;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

import java.util.List;

/**
 * This page should be added last - since it scans the model looking for fields that had overflow when they wer'e rendered.
 *
 * Created by marced on 2016-10-24.
 */
public class FkOverflowPage extends FkPage {
    private String pageTitle;
    private FkPdfDefinition model;

    public FkOverflowPage(String pageTitle, FkPdfDefinition model) {
        this.pageTitle = pageTitle;
        this.model = model;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        super.render(document, writer, x, y);
        renderOverflowingItems(document, model.collectOverflowingComponents());
    }

    private void renderOverflowingItems(Document document, List<FkOverflowableValueField> overflowingComponents) throws DocumentException {
        // Skip if nothing to do here..
        if (overflowingComponents.size() < 1) {
            return;
        }

        for (FkOverflowableValueField item : overflowingComponents) {
            Paragraph p = new Paragraph();
            p.setIndentationLeft(2f);
            p.setIndentationRight(2f);
            p.setKeepTogether(true);

            p.add(Chunk.NEWLINE);
            p.add(new Phrase(item.getLabel(), PdfConstants.FONT_FRAGERUBRIK));
            p.add(Chunk.NEWLINE);

            p.add(new Phrase(item.getOverFlowingText(), PdfConstants.FONT_VALUE_TEXT_ARIAL_COMPATIBLE));

            document.add(p);
        }

    }
}
