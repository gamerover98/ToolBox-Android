package it.uniba.magr.misurapp.tool.ruler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import it.uniba.magr.misurapp.util.GenericUtil;
import lombok.Getter;

public class RulerCanvasView extends View {

    /**
     * The background corner line thickness.
     */
    private static final float BACKGROUND_CORNER_LINE_FORCE = 20f;

    /**
     * The ruler background color.
     */
    private static final int BACKGROUND_COLOR = Color.rgb(255, 201, 0);

    /**
     * The ruler edge and thickness line color.
     */
    private static final int LINE_COLOR = Color.BLACK;

    /**
     * The centimeter measure line thickness.
     */
    private static final float PRIMARY_MEASURE_LINE_FORCE = 10f;

    /**
     * The smaller measures line thickness.
     */
    private static final float MEASURE_LINE_FORCE = 6f;

    /**
     * The distance between the first ruler line and the top of the view.
     */
    public static final int LINE_MARGIN_TOP = 40;

    /**
     * The right margin value.
     */
    private static final int VALUE_TEXT_RIGHT_MARGIN = 20;

    /**
     * The top margin value.
     * This value moves down the text to centre the centimetre line.
     */
    private static final int VALUE_TEXT_TOP_MARGIN = 15;

    /**
     * The value text size.
     */
    private static final float VALUE_TEXT_SIZE = 48f;

    /**
     * Gets the Canvas.
     * It will be initialized when onDraw() method is called.
     */
    @Getter @Nullable
    private Canvas canvas;

    public RulerCanvasView(@NotNull Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        this.canvas = canvas;

        drawRuler();

    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {

        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

    }

    /**
     * Draw ruler
     */
    protected void drawRuler() {

        assert canvas != null;

        drawBackground();
        drawLines();

    }

    /**
     * Draw the ruler background.
     */
    protected void drawBackground() {

        assert canvas != null;

        Paint backgroundPaint = new Paint();
        Rect backgroundRectangle = new Rect(0, 0, getWidth(), getHeight());

        Paint linePaint = new Paint();

        backgroundPaint.setColor(BACKGROUND_COLOR);
        backgroundPaint.setStyle(Paint.Style.FILL);

        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.MITER);
        linePaint.setStrokeWidth(BACKGROUND_CORNER_LINE_FORCE);

        canvas.drawRect(backgroundRectangle, backgroundPaint);

        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), linePaint);
        canvas.drawLine(0, 0, getWidth(), 0, linePaint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), linePaint);

    }

    /**
     * Draw the ruler lines.
     */
    protected void drawLines() {

        Paint linePaint = new Paint();
        Paint textPaint = new Paint();

        linePaint.setColor(LINE_COLOR);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.MITER);

        int maxLineLength = getWidth() / 2;

        // 1/1 centimeter lines
        drawCentimeterLines(linePaint, textPaint,
                maxLineLength, 1);

        // 1/2 centimeter lines
        drawCentimeterLines(linePaint, null,
                maxLineLength / 2, 2);

        // 1/4 centimeter lines
        drawCentimeterLines(linePaint, null,
                maxLineLength / 4, 4);

        // 1/8 centimeter lines
        drawCentimeterLines(linePaint, null,
                maxLineLength / 8, 8);

    }

    /**
     * Draw the centimeter line and sub-line.
     *
     * @param linePaint A not null instance for line Paint.
     * @param textPaint A not null instance for text Paint.
     * @param lineLength The line length that will be drawn.
     * @param lineNumber The line number.
     */
    private void drawCentimeterLines(@NotNull Paint linePaint, @Nullable Paint textPaint,
                                     int lineLength, int lineNumber) {

        assert canvas != null;
        linePaint.setStrokeWidth(lineNumber == 1 ? PRIMARY_MEASURE_LINE_FORCE : MEASURE_LINE_FORCE);

        // the exact physical pixels per centimeters of the screen in the Y dimension.
        float centimetersPixel = GenericUtil.getPixelsSizeY(getContext()) * 10; //  * 10 -> centimeters
        int displayHeight = getHeight();

        if (lineNumber > 1) {
            centimetersPixel /= (float) lineNumber;
        }

        int drawValuePosition = lineLength + VALUE_TEXT_RIGHT_MARGIN;
        double value = 0;

        if (textPaint != null) {

            textPaint.setAntiAlias(true);
            textPaint.setTextSize(VALUE_TEXT_SIZE / lineNumber);

        }

        for (float height = LINE_MARGIN_TOP ; height < displayHeight ; height += centimetersPixel) {

            canvas.drawLine(0, height, lineLength, height, linePaint);

            if (textPaint != null) {

                String textValue = value == (long) value ?
                        String.format(Locale.getDefault(), "%d", (long) value)
                        : String.valueOf(value);

                canvas.drawText(textValue, drawValuePosition,
                        height + VALUE_TEXT_TOP_MARGIN, textPaint);

            }

            value += (1f / (float) lineNumber);

        }

    }

}
