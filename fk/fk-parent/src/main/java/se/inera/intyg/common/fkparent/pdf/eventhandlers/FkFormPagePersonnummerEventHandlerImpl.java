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

/**
 * Extension of the generic FkAbstractPersonnummerEventHandler, overriding the positioning an on which pages to show the
 * personnummer on static form pages.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkFormPagePersonnummerEventHandlerImpl extends FkAbstractPersonnummerEventHandler {

    private static final float DEFAULT_X_OFFSET = 152f;
    private static final float DEFAULT_Y_OFFSET = 9f;

    private float offsetX = 0.0f;
    private float offsetY = 0.0f;

    public FkFormPagePersonnummerEventHandlerImpl(String personnummer) {
        super(personnummer);
    }

    public FkFormPagePersonnummerEventHandlerImpl(String personnummer, float offsetX, float offsetY) {
        super(personnummer);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public FkFormPagePersonnummerEventHandlerImpl withOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }
    public FkFormPagePersonnummerEventHandlerImpl withOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    @Override
    protected int getActiveFromPage() {
        return 2;
    }

    @Override
    protected int getActiveToPage() {
        return 4;
    }

    @Override
    protected float getXOffset() {
        return DEFAULT_X_OFFSET + offsetX;
    }

    @Override
    protected float getYOffset() {
        return DEFAULT_Y_OFFSET + offsetY;
    }
}
