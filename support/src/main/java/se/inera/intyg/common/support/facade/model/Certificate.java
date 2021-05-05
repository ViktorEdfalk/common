/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.model;

import java.util.HashMap;
import java.util.Map;
import se.inera.intyg.common.support.modules.support.facade.dto.ResourceLinkDTO;

public class Certificate {

    private CertificateMetadata metadata;
    private Map<String, CertificateDataElement> data = new HashMap<>();
    private ResourceLinkDTO[] links;

    public CertificateMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CertificateMetadata metadata) {
        this.metadata = metadata;
    }

    public Map<String, CertificateDataElement> getData() {
        return data;
    }

    public void setData(Map<String, CertificateDataElement> data) {
        this.data = data;
    }

    public ResourceLinkDTO[] getLinks() {
        return links;
    }

    public void setLinks(ResourceLinkDTO[] links) {
        this.links = links;
    }
}
