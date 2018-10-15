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
package se.inera.intyg.common.pdf.model;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

/**
 * Base class for all UV / Unified Print components. Contains constants and helper functions.
 *
 * @author eriklupander
 */
// CHECKSTYLE:OFF MagicNumber
public abstract class UVComponent {

    static final float ELEM_MARGIN_LEFT_POINTS = millimetersToPoints(5f);
    static final float ELEM_MARGIN_RIGHT_POINTS = millimetersToPoints(10f);

    static final float DEFAULT_BORDER_WIDTH = 0.5f;

    public static final float KATEGORI_FONT_SIZE = 12f;
    public static final float FRAGA_DELFRAGA_FONT_SIZE = 10f;
    public static final float SVAR_FONT_SIZE = 10f;

    static final String HIDE_EXPRESSION = "hideExpression";
    static final String SHOW_EXPRESSION = "showExpression";

    protected static final String EJ_ANGIVET_STR = "Ej angivet";

    protected final UVRenderer renderer;

    Color wcColor02 = new DeviceRgb(0xFF, 0xEB, 0xBA);
    Color wcColor05 = new DeviceRgb(0x76, 0x5A, 0x20);
    Color wcColor07 = new DeviceRgb(33, 33, 33);
    Color wcColor09 = new DeviceRgb(106, 106, 106);

    static final String LABEL_KEY = "labelKey";
    static final String MODEL_PROP = "modelProp";

    public UVComponent(UVRenderer renderer) {
        this.renderer = renderer;
    }

    public abstract boolean render(Div parent, ScriptObjectMirror currentUvNode);

    /**
     * Transforms the value entries of a ScriptObjectMirror into a list of strings.
     */
    List<String> fromStringArray(Object arrayValues) {
        List<String> results = new ArrayList<>();
        if (arrayValues != null) {
            ScriptObjectMirror valuesRoot = (ScriptObjectMirror) arrayValues;
            for (Map.Entry<String, Object> entry : valuesRoot.entrySet()) {
                // Typically string or function
                if (entry.getValue() instanceof String) {
                    results.add((String) entry.getValue());
                } else {
                    throw new IllegalStateException("Unhandled array type " + entry.getValue().getClass().getName());
                }
            }
        }
        return results;
    }

    /**
     * Resolves the boolean value of the supplied modelProp and returns Ja/Nej or ERROR if not boolean.
     */
    String getBooleanValue(String modelProp) {
        Object eval = renderer.evalValueFromModel(modelProp);
        if (eval != null) {
            if (eval instanceof Boolean) {
                return (Boolean) eval ? "Ja" : "Nej";
            }
        }
        return EJ_ANGIVET_STR;
    }

    boolean show(ScriptObjectMirror obj) {
        boolean render = true;
        if (isNotEligibleForCheck(obj)) {
            return true;
        }

        // Show expression
        if (obj.containsKey(HIDE_EXPRESSION)) {
            render = handleHideExpression(obj);
        } else if (obj.containsKey(SHOW_EXPRESSION)) {
            render = handleShowExpression(obj);
        }
        return render;
    }

    void renderEjAngivet(Div parent) {
        parent.add(new Paragraph(EJ_ANGIVET_STR)
                .setItalic()
                .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
                .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
    }

    private boolean isNotEligibleForCheck(ScriptObjectMirror obj) {
        return !obj.containsKey(HIDE_EXPRESSION) && !obj.containsKey(SHOW_EXPRESSION);
    }

    private boolean handleHideExpression(ScriptObjectMirror obj) {
        boolean render;
        Object hideExpression = obj.get(HIDE_EXPRESSION);
        if (hideExpression instanceof ScriptObjectMirror) {
            Object result = ((ScriptObjectMirror) hideExpression).call(null, renderer.getIntygModel());
            render = !isTrue(result);
        } else {
            // handle as predicate expression...
            render = resolveHideExpression((String) hideExpression);
        }
        return render;
    }

    private boolean handleShowExpression(ScriptObjectMirror obj) {
        boolean render;
        Object showExpression = obj.get(SHOW_EXPRESSION);
        if (showExpression instanceof ScriptObjectMirror) {
            Object result = ((ScriptObjectMirror) showExpression).call(null, renderer.getIntygModel());
            render = isTrue(result);
        } else {
            // handle as predicate expression...
            render = resolveShowExpression((String) showExpression);
        }
        return render;
    }

    private boolean resolveHideExpression(String hideExpression) {
        boolean render;
        if (hideExpression.startsWith("!")) {
            // negation
            Object eval = renderer.evalValueFromModel(hideExpression.substring(1));
            render = isTrue(eval);
        } else {
            Object eval = renderer.evalValueFromModel(hideExpression);
            render = !isTrue(eval);
        }
        return render;
    }

    private boolean resolveShowExpression(String expression) {
        boolean render;
        if (expression.startsWith("!")) {
            // negation
            Object eval = renderer.evalValueFromModel(expression.substring(1));
            render = !isTrue(eval);
        } else {
            Object eval = renderer.evalValueFromModel(expression);
            render = isTrue(eval);
        }
        return render;
    }

    private boolean isTrue(Object res) {
        if (res == null) {
            return false;
        }
        if (res instanceof Undefined) {
            return false;
        }
        if (res instanceof Boolean) {
            return (Boolean) res;
        }
        return false;
    }
}
// CHECKSTYLE:ON MagicNumber
