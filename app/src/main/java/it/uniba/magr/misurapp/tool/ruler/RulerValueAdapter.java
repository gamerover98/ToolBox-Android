package it.uniba.magr.misurapp.tool.ruler;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import it.uniba.magr.misurapp.R;

class RulerValueAdapter extends ArrayAdapter<Integer> {

    private static final int SEGMENT_UNIT_FRACTION = 2;

    private final int    segmentPixels;
    private final double segmentFraction;
    private final int    numSegments;

    private final Map<Integer, Double> fractionMap = new HashMap<>();

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when.
     * @param pixels   The length of the ruler in pixels.
     * @param dpi      The relevant DPI.
     */
    public RulerValueAdapter(Context context, int resource, int pixels, int dpi) {

        super(context, resource);

        // Calculate the pixels per segment unit
        this.segmentPixels   = dpi / SEGMENT_UNIT_FRACTION;
        this.segmentFraction = (double) dpi / (double) SEGMENT_UNIT_FRACTION - segmentPixels;

        // Calculate the necessary number of segments
        this.numSegments = pixels / segmentPixels;

    }

    @Override
    public int getCount() {
        return numSegments;
    }

    @NotNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NotNull ViewGroup parent) {

        // Create new convertView if necessary
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ruler_item_list_value,
                    parent, false);

        }

        // Set segment length
        ViewGroup.LayoutParams segmentParams = convertView.getLayoutParams();

        // Compensate for leap pixels
        Double compensation = fractionMap.get(position - 1); // Get previous fraction

        if (compensation == null) {
            compensation = 0.0;
        }

        compensation += segmentFraction; // Add segment fraction
        segmentParams.height = (int) (segmentPixels + compensation);
        convertView.setLayoutParams(segmentParams);

        // Store remaining fraction
        fractionMap.put(position, compensation - Math.floor(compensation));

        // Customize segment display
        TextView markerTextView = convertView.findViewById(R.id.ruler_segment_value);
        String markerText;
        markerTextView.setTypeface(null, Typeface.BOLD);
        int markerTextSize;

        if (position % 2 == 0) {

            // Whole inch
            markerText = Integer.toString(position / 2);
            markerTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            markerTextSize = 20;

        } else {

            // Half inch
            markerText = "1/2";
            markerTextView.setTypeface(null, Typeface.NORMAL);
            markerTextSize = 8;

        }

        // Apply segment properties
        markerTextView.setText(markerText);
        markerTextView.setTextSize(markerTextSize);

        return convertView;

    }

}
