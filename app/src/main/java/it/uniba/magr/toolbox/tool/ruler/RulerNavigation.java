package it.uniba.magr.toolbox.tool.ruler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;
import it.uniba.magr.toolbox.navigation.Navigable;
import it.uniba.magr.toolbox.util.GenericUtil;
import lombok.Getter;

public class RulerNavigation implements Navigable {

    /**
     * The navigation bundle length key.
     */
    public static final String BUNDLE_LENGTH_KEY = "length";

    /**
     * The home activity instance.
     */
    private HomeActivity homeActivity;

    /**
     * The fab save button.
     */
    private FloatingActionButton saveButton;

    /**
     * The touch image view.
     * It will be drawn by indicator lines.
     */
    private AppCompatImageView indicatorLineImageView;

    /**
     * The touched line.
     */
    private final Paint indicatorLinePaint = new Paint();

    /**
     * The text up or down the drawn line.
     */
    private final Paint indicatorTextPaint = new Paint();

    /**
     * Gets the length in centimetres calculated from the indicator line.
     */
    @Getter
    private float centimeters;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ruler;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_ruler);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        homeActivity = (HomeActivity) activity;

        saveButton = homeActivity.findViewById(R.id.ruler_fab_button_save);
        saveButton.setOnClickListener(view -> onSaveButtonClick(homeActivity));

        indicatorLineImageView = homeActivity.findViewById(R.id.ruler_indicator_line_image_view);

        indicatorLinePaint.setColor(Color.RED);
        indicatorLinePaint.setStyle(Paint.Style.STROKE);
        indicatorLinePaint.setStrokeWidth(10f);

        indicatorTextPaint.setTextSize(75f);

    }

    @Override
    public void onTouchEvent(@NotNull MotionEvent event) {

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        // due to the thickness of the toolbar, then Y must be subtracted from it.
        Toolbar toolbar = homeActivity.getToolbar();
        float toolbarHeight = toolbar.getHeight();

        // prevents line drawing under the toolbar.
        if (toolbarHeight > y) {
            return;
        }

        y -= toolbarHeight;

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {

            Set<View> touchedViews = getTouchedViews(x, y);
            boolean result = canDrawIndicatorLine(touchedViews);

            if (!result) {
                return;
            }

            // the exact physical pixels per centimeters of the screen in the X dimension.
            float centimeterPixels = GenericUtil.getPixelsSizeY(homeActivity) * 10; //  * 10 -> centimeters
            // gets the distance between the top of the ruler image and the first ruler value (the 0).
            float topMargin = RulerCanvasView.LINE_MARGIN_TOP / centimeterPixels;
            centimeters = y / centimeterPixels - topMargin;

            if (centimeters < 0) {

                centimeters = 0;
                y = RulerCanvasView.LINE_MARGIN_TOP;

            } else {

                int intCentimeters = (int) centimeters;

                // tries to fix the mantissa error with the subtraction of the difference
                // between the integer length and the number of pixels in cm.
                centimeters = centimeters - intCentimeters / centimeterPixels;

                // tries to add another smaller quantity to fix this issue.
                // centimeters = centimeters + (1.0e-15f * intCentimeters)

                // abs(int value - float value) < 10^-1
                // thank you very much for your instruction, teacher Garrappa <3
                if (Math.abs(intCentimeters - centimeters) < 1.0e-1f) {
                    centimeters = intCentimeters;
                }

                // A math trick to trim decimal values from a number.
                centimeters *= 100;
                centimeters = (float) Math.floor(centimeters);
                centimeters /= 100;

            }

            drawIndicatorLine(y);

        }

    }


    private boolean canDrawIndicatorLine(@NotNull Set<View> touchedViews) {

        for (View view : touchedViews) {

            if (view.equals(saveButton)) {
                return false;
            }

        }

        return true;

    }

    /**
     * Draw an horizontal line at this height.
     * @param y The layout y cords.
     */
    private void drawIndicatorLine(final float y) {

        float width  = indicatorLineImageView.getWidth();
        float height = indicatorLineImageView.getHeight();

        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(bitmap, 0, 0, null);

        float textX = width / 2 + 100; // central width + right a padding
        float textY = y + (y > 150 ? -50 : 100); // under the line: -50 | above the line: +100

        canvas.drawText(centimeters + " cm", textX, textY, indicatorTextPaint);
        canvas.drawLine(0, y, width, y, indicatorLinePaint);

        BitmapDrawable drawable = new BitmapDrawable(homeActivity.getResources(), bitmap);
        indicatorLineImageView.setImageDrawable(drawable);

    }

    @NotNull
    private Set<View> getTouchedViews(float x, float y) {

        Set<View> views = new HashSet<>();

        ViewGroup rulerLayoutView = homeActivity.findViewById(R.id.ruler_layout);
        assert rulerLayoutView != null;

        for (int i = 0 ; i < rulerLayoutView.getChildCount() ; i++) {

            View child = rulerLayoutView.getChildAt(i);

            int childLeft   = child.getLeft();
            int childRight  = child.getRight();
            int childTop    = child.getTop();
            int childBottom = child.getBottom();

            if (x > childLeft && x < childRight && y > childTop && y < childBottom) {
                views.add(child);
            }

        }

        return views;

    }

    /**
     * When clicking the save button.
     * @param context The application context.
     */
    private void onSaveButtonClick(@NotNull Context context) {

        HomeActivity activity = (HomeActivity) context;

        Bundle bundle = new Bundle();
        bundle.putFloat(BUNDLE_LENGTH_KEY, centimeters);

        NavController navController = activity.getNavController();
        navController.navigate(R.id.action_nav_save_measure_fragment_to_ruler, bundle);

    }

}
