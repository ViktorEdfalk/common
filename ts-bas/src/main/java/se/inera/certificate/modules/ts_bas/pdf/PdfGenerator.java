/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_bas.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfGenerator {

    public String generatePdfFilename(Utlatande utlatande) {
        // TODO: Implement
        return null;
    }

    public byte[] generatePDF(Utlatande utlatande) throws PdfGeneratorException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader("pdf/blankett.pdf");
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            pdfStamper.setFormFlattening(true);
            AcroFields fields = pdfStamper.getAcroFields();
            populatePdfFields(utlatande, fields);
            pdfStamper.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
        }
    }

    /**
     * Method for filling out the fields of a pdf with data from the internal model
     * 
     * @param utlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande} containing data for populating
     *            the pdf
     * @param fields
     *            The fields of the pdf
     * @throws DocumentException
     * @throws IOException
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        // TODO: Implement
    }
}
