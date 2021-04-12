package it.uniba.magr.toolbox.util;

import org.jetbrains.annotations.NotNull;

public final class MathUtil {

    private MathUtil() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * @param value The text value of the number.
     * @return True if it is a long value.
     */
    public static boolean isLong(@NotNull String value) {

        try {

            Long.parseLong(value);
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * @param value The text value of the number.
     * @return True if it is an integer value.
     */
    public static boolean isInteger(@NotNull String value) {

        try {

            Integer.parseInt(value);
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * @param value The text value of the number.
     * @return True if it is a short value.
     */
    @SuppressWarnings("all")
    public static boolean isShort(@NotNull String value) {

        try {

            Short.parseShort(value);
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * @param value The text value of the number.
     * @return True if it is a byte value.
     */
    @SuppressWarnings("all")
    public static boolean isByte(@NotNull String value) {

        try {

            Byte.parseByte(value);
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * @param value The text value of the number.
     * @return True if it is a double value.
     */
    public static boolean isDouble(@NotNull String value) {

        try {

            Double.parseDouble(value);
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * @param value The text value of the number.
     * @return True if it is a float value.
     */
    public static boolean isFloat(@NotNull String value) {

        try {

            Float.parseFloat(value);
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * Gets the number wrapped instance of a string text.
     *
     * @param value The string text that contains the numeric value.
     * @return The Number wrapped instance of the type.
     * @throws IllegalArgumentException If the argument is not a number.
     */
    public static Number getNumber(@NotNull String value) {

        if (isLong(value)) {
            return Long.parseLong(value);
        } else if (isInteger(value)) {
            return Integer.parseInt(value);
        } else if (isShort(value)) {
            return Short.parseShort(value);
        } else if (isByte(value)) {
            return Byte.parseByte(value);
        } else if (isDouble(value)) {
            return Double.parseDouble(value);
        } else if (isFloat(value)) {
            return Float.parseFloat(value);
        }

        throw new IllegalArgumentException("The argument is not a number");

    }

}
