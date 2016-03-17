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

package se.inera.intyg.common.support.model.common.internal;

import java.util.Objects;

import se.inera.intyg.common.support.common.enumerations.RelationKod;

public class Relation {

    private RelationKod relationKod;

    private String relationIntygsId;

    private String meddelandeId;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Relation that = (Relation) object;
        return Objects.equals(this.relationKod, that.relationKod)
                && Objects.equals(this.relationIntygsId, that.relationIntygsId)
                && Objects.equals(this.meddelandeId, that.meddelandeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.relationKod, this.relationIntygsId);
    }

    public RelationKod getRelationKod() {
        return relationKod;
    }

    public void setRelationKod(RelationKod relationKod) {
        this.relationKod = relationKod;
    }

    public String getRelationIntygsId() {
        return relationIntygsId;
    }

    public void setRelationIntygsId(String relationIntygsId) {
        this.relationIntygsId = relationIntygsId;
    }

    public String getMeddelandeId() {
        return meddelandeId;
    }

    public void setMeddelandeId(String meddelandeId) {
        this.meddelandeId = meddelandeId;
    }

}
