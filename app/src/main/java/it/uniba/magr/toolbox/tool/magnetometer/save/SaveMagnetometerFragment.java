package it.uniba.magr.toolbox.tool.magnetometer.save;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.uniba.magr.toolbox.R;
import it.uniba.magr.toolbox.database.realtime.NotConnectedException;
import it.uniba.magr.toolbox.database.realtime.RealtimeManager;
import it.uniba.magr.toolbox.database.realtime.bean.RealtimeMagnetometer;
import it.uniba.magr.toolbox.database.sqlite.SqliteManager;
import it.uniba.magr.toolbox.database.sqlite.bean.Magnetometer;
import it.uniba.magr.toolbox.database.sqlite.bean.Measure;
import it.uniba.magr.toolbox.database.sqlite.bean.Type;
import it.uniba.magr.toolbox.database.sqlite.dao.MagnetometersDao;
import it.uniba.magr.toolbox.navigation.save.SaveMeasureFragment;
import it.uniba.magr.toolbox.tool.magnetometer.MagnetometerNavigation;
import it.uniba.magr.toolbox.tool.util.XAxisValueFormatterUtil;

public class SaveMagnetometerFragment extends SaveMeasureFragment {

    /**
     * An integer format for the x axis values.
     */
    private static final DecimalFormat FORMAT = new DecimalFormat("###");

    private int[]   seconds;
    private float[] values;

    public SaveMagnetometerFragment() {
        super(() -> R.layout.fragment_save_magnetometer);
    }

    @NotNull
    @Override
    protected Type getMeasureType() {
        return Type.MAGNETOMETER;
    }

    @Override
    protected void handleParametersCreation(@NotNull FragmentActivity activity, @NotNull Bundle bundle) {

        LineChart lineChart = activity.findViewById(R.id.save_magnetometer_line_chart);
        TextInputEditText editTextMinValue = activity.findViewById(R.id.save_magnetometer_input_text_min_value);
        TextInputEditText editTextMaxValue = activity.findViewById(R.id.save_magnetometer_input_text_max_value);

        seconds = bundle.getIntArray(MagnetometerNavigation.BUNDLE_SECONDS_KEY);
        values  = bundle.getFloatArray(MagnetometerNavigation.BUNDLE_VALUES_KEY);
        assert seconds != null && values != null;

        lineChart.setVisibleXRangeMaximum(seconds.length);
        initLineChart(lineChart);

        if (seconds.length != values.length) {
            throw new IllegalStateException("The seconds array length must be the same of the values array length");
        }

        float minValue = Integer.MAX_VALUE;
        float maxValue = Integer.MIN_VALUE;

        LineData data = lineChart.getData();
        ILineDataSet set = data.getDataSetByIndex(0);

        if (set == null) {

            set = createSet(lineChart);
            data.addDataSet(set);

        }

        for (int i = 0 ; i < seconds.length ; i++) {

            float value = values[i];

            if (minValue > value) {
                minValue = value;
            }

            if (maxValue < value) {
                maxValue = value;
            }

            data.addEntry(new Entry(set.getEntryCount(), value), 0);

        }

        updateLineChart(lineChart, minValue, maxValue);

        data.notifyDataChanged();
        lineChart.notifyDataSetChanged();

        String magnetometerUnit = activity.getString(R.string.magnetometer_unit);

        String minText = "min: " + FORMAT.format(minValue) + " " + magnetometerUnit;
        String maxText = "max: " + FORMAT.format(maxValue) + " " + magnetometerUnit;

        editTextMinValue.setText(minText);
        editTextMaxValue.setText(maxText);

    }

    @Override
    protected void saveToSqlite(@NotNull SqliteManager sqliteManager, @NotNull Measure measure) {

        MagnetometersDao magnetometersDao = sqliteManager.magnetometersDao();
        int length = seconds.length;

        Magnetometer[] magnetometers = new Magnetometer[length];

        for (int i = 0 ; i < length ; i++) {


            Magnetometer magnetometer = new Magnetometer();

            magnetometer.setMeasureId(measure.getId());
            magnetometer.setCount(i);
            magnetometer.setValue(values[i]);
            magnetometer.setTime(seconds[i]);

            magnetometers[i] = magnetometer;

        }

        magnetometersDao.insertMagnetometers(magnetometers);

    }

    @Override
    protected void saveToRealtime(@NotNull RealtimeManager realtimeManager,
                                  @NotNull Measure measure) throws NotConnectedException {

        int measureId      = measure.getId();
        String title       = measure.getTitle();
        String description = measure.getDescription();
        Date startDate     = measure.getStartDate();

        RealtimeMagnetometer realtimeMagnetometer = new RealtimeMagnetometer();

        assert seconds.length == values.length;
        List<Integer> secondsToList = new ArrayList<>(seconds.length);
        List<Float>   valuesToList  = new ArrayList<>(values.length);

        int length = seconds.length;

        for (int i = 0 ; i < length ; i++) {

            secondsToList.add(seconds[i]);
            valuesToList.add(values[i]);

        }

        realtimeMagnetometer.setMeasureId(measureId);
        realtimeMagnetometer.setTitle(title);
        realtimeMagnetometer.setDescription(description);
        realtimeMagnetometer.setStartDate(startDate);
        realtimeMagnetometer.setSeconds(secondsToList);
        realtimeMagnetometer.setValues(valuesToList);

        realtimeManager.addMeasure(realtimeMagnetometer);

    }

    private void initLineChart(@NotNull LineChart lineChart) {

        lineChart.setDrawBorders(true);

        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);

        // empty chart initialization
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        lineChart.setData(data);

        Legend legend = lineChart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(15f);
        legend.setTextColor(Color.BLACK);

        updateLineChart(lineChart, 0, MagnetometerNavigation.MAGNETIC_SENSOR_MAX_VALUE);

    }

    private void updateLineChart(@NotNull LineChart lineChart, float minValue, float maxValue) {

        YAxis leftAxis  = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        XAxis xAxis     = lineChart.getXAxis();

        leftAxis.setTextColor    (Color.BLACK);
        leftAxis.setTextSize     (20);
        leftAxis.setAxisMaximum  (maxValue + MagnetometerNavigation.VALUE_BOUND);
        leftAxis.setAxisMinimum  (minValue - 10);
        leftAxis.setDrawZeroLine (true);
        leftAxis.setDrawGridLines(true);
        rightAxis.setEnabled     (false);
        xAxis.setDrawGridLines   (false);
        xAxis.setValueFormatter  (new XAxisValueFormatterUtil());
        lineChart.setDrawBorders (true);

    }

    /**
     * @return The not null chart line data with its decorations.
     */
    @NotNull
    private LineDataSet createSet(@NotNull LineChart lineChart) {

        Context context = lineChart.getContext();
        Resources resources = context.getResources();

        int cardColor = ResourcesCompat.getColor(context.getResources(),
                R.color.magnetometer_card, context.getTheme());

        LineDataSet lineDataSet = new LineDataSet(null,
                resources.getString(R.string.tesla_chart_label));

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(2f);

        lineDataSet.setColor(cardColor);
        lineDataSet.setHighlightEnabled(false);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);

        lineDataSet.setCircleColor(cardColor);
        lineDataSet.setCircleRadius(2f);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);

        lineDataSet.setFillFormatter(new DefaultFillFormatter());
        lineDataSet.setFillColor(cardColor);

        return lineDataSet;

    }

}
