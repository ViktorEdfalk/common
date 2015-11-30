package se.inera.intyg.intygstyper.ts_bas.utils;

import junit.framework.AssertionFailedError;

import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

public final class ModelAssert {

    private ModelAssert() {

    }
    public static void assertEquals(Object expected, Object actual) {
        try {
            ReflectionAssert.assertReflectionEquals(expected, actual, ReflectionComparatorMode.LENIENT_ORDER);
        } catch (AssertionFailedError e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        try {
            ReflectionAssert.assertReflectionEquals(message, expected, actual, ReflectionComparatorMode.LENIENT_ORDER);
        } catch (AssertionFailedError e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }
}
