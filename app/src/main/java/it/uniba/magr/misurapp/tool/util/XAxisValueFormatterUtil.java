package it.uniba.magr.misurapp.tool.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Due to the floating point values into the x axis, this class prevents that
 * the number is split into a decimal value format.
 *
 * Eg: 1,5 -> 1.
 */
public class XAxisValueFormatterUtil extends ValueFormatter {

    /**
     * The pattern for the decimal format instance.
     */
    private static final String PATTERN = "###";

    /**
     * An integer format for the x axis values.
     */
    private static final DecimalFormat format = new DecimalFormat(PATTERN);

    /**
     * Convert the float value into an integer value and than to string.
     *
     * @param value The x float value.
     * @param axis The x axis.
     * @return The integer value converted in a String instance.
     */
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return format.format(value);
    }

}
