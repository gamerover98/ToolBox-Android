package it.uniba.magr.misurapp.util;

import com.github.mikephil.charting.components.AxisBase;

import java.text.DecimalFormat;

public class XAxisValueFormatterUtil extends com.github.mikephil.charting.formatter.ValueFormatter {

    private DecimalFormat mFormat;

    public XAxisValueFormatterUtil() {
        mFormat = new DecimalFormat("###"); // sets precision to 1
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return mFormat.format(value);
    }
}
