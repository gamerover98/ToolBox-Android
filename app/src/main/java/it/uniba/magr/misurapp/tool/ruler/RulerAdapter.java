package it.uniba.magr.misurapp.tool.ruler;

import android.content.Context;
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

/**
 * Adapter to convert a vertical ListView into a RulerView.
 */
class RulerAdapter extends ArrayAdapter<Integer> {

    private static final int SEGMENT_UNIT_FRACTION = 16;

    private static final double WHOLE_INCH_MULTIPLIER     = 32;
    private static final double HALF_INCH_MULTIPLIER      = 24;
    private static final double QUARTER_INCH_MULTIPLIER   = 16;
    private static final double EIGHTH_INCH_MULTIPLIER    = 8;
    private static final double SIXTEENTH_INCH_MULTIPLIER = 4;

    private final int segmentPixels;
    private final double segmentFraction;
    private final int    numSegments;

    private final Map<Integer, Double> fractionMap = new HashMap<>();

    private final int   baseMarkerThickness; // Base segment marker width in dp
    private final int   baseMarkerLength;    // Base segment marker length in dp
    private final float baseMarkerTextSize;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when.
     * @param pixels   The length of the ruler in pixels.
     * @param density  The relevant pixels density (like DPI).
     */
    public RulerAdapter(Context context, int resource, int pixels, int density) {

        super(context, resource);

        // Calculate the pixels per segment unit
        this.segmentPixels   = density / SEGMENT_UNIT_FRACTION;
        this.segmentFraction = (double) density / (double) SEGMENT_UNIT_FRACTION - segmentPixels;

        // Calculate the necessary number of segments
        this.numSegments = pixels / segmentPixels;

        baseMarkerThickness = segmentPixels / 4;
        baseMarkerLength    = segmentPixels;
        baseMarkerTextSize  = segmentPixels / 5f;

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

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ruler_item_list_segment,
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
        double multiplier;
        ViewGroup.LayoutParams markerParams = convertView
                .findViewById(R.id.ruler_segment_marker).getLayoutParams();

        TextView markerTextView = convertView.findViewById(R.id.ruler_segment_value);
        String markerText;

        float markerTextSize = (float) (baseMarkerTextSize * 0.75);

        if (position % 16 == 0) {

            // Whole inch
            multiplier = WHOLE_INCH_MULTIPLIER;
            markerText = ""; // Draw text with independent ruler marker

        } else if (position % 8 == 0) {

            // Half inch
            multiplier = HALF_INCH_MULTIPLIER;
            markerText = ""; // Draw text with independent ruler marker

        } else if (position % 4 == 0) {

            // Quarter inch
            multiplier = QUARTER_INCH_MULTIPLIER;
            markerText = "";

        } else if (position % 2 == 0) {

            // Eighth inch
            multiplier = EIGHTH_INCH_MULTIPLIER;
            markerText = "";

        } else {

            // Sixteenth inch
            multiplier = SIXTEENTH_INCH_MULTIPLIER;
            markerText = "";

        }

        // Apply segment properties
        markerParams.height = baseMarkerThickness;
        markerParams.width = (int) (baseMarkerLength * multiplier);
        convertView.findViewById(R.id.ruler_segment_marker).setLayoutParams(markerParams);
        markerTextView.setText(markerText);
        markerTextView.setTextSize(markerTextSize);

        return convertView;

    }

}


