package it.uniba.magr.toolbox.tool.barometer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.toolbox.R;

public class BarometerView extends View {

    private static final float THICKNESS    = 50;
    private static final float MAX_PRESSURE = 1100;
    private static final float MIN_PRESSURE = 0;

    private float shellCircleRadius;
    private float shellRectRadius;
    private Paint shellPaint;

    private float areaCircleRadius;
    private float areaRectRadius;
    private Paint areaPaint;

    private float innerCircleRadius;
    private Paint innerPaint;

    private Paint degreePaint;

    private float currentPressure;

    private final RectF shellRect = new RectF();
    private final RectF areaRect  = new RectF();

    public BarometerView(Context context) {

        super(context);
        init(context, null);

    }

    public BarometerView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        init(context, attributeSet);

    }

    public BarometerView(Context context, AttributeSet attributeSet, int defStyleAttr) {

        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        int height = getHeight();
        int width  = getWidth();

        int   x = width / 2;
        float y = height - shellCircleRadius;

        float outerStartY = 0;
        float areaStartY  = outerStartY + 5;

        float innerEffectStartY = areaStartY + areaRectRadius + 10;
        float innerEffectEndY   = y - shellCircleRadius - 10;

        shellRect.left   = x - shellRectRadius;
        shellRect.top    = outerStartY;
        shellRect.right  = x + shellRectRadius;
        shellRect.bottom = y;

        areaRect.left   = x - areaRectRadius;
        areaRect.top    = areaStartY;
        areaRect.right  = x + areaRectRadius;
        areaRect.bottom = y;

        canvas.drawRoundRect(shellRect, shellRectRadius, shellRectRadius, shellPaint);
        canvas.drawCircle(x, y, shellCircleRadius, shellPaint);
        canvas.drawRoundRect(areaRect, areaRectRadius, areaRectRadius, areaPaint);
        canvas.drawCircle(x, y, areaCircleRadius, areaPaint);
        canvas.drawCircle(x, y, innerCircleRadius, innerPaint);
        drawDegrees(canvas, x, innerEffectStartY, innerEffectEndY);

        float ratio = y / MAX_PRESSURE;
        float pressureHeight = y - currentPressure * ratio;

        canvas.drawRect(x - 5f, y, x + 5f, pressureHeight, innerPaint);

    }

    public void setCurrentPressure(float pressure) {

        if (pressure > MAX_PRESSURE) {
            this.currentPressure = MAX_PRESSURE;
        } else if (currentPressure < MIN_PRESSURE) {
            this.currentPressure = MIN_PRESSURE;
        } else {
            this.currentPressure = pressure;
        }

        invalidate();

    }

    public float getCurrentPressure() {
        return this.currentPressure;
    }

    protected void init(@NotNull Context context, @Nullable AttributeSet attributeSet) {

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BarometerView);
        shellCircleRadius = typedArray.getDimension(R.styleable.BarometerView_radius, THICKNESS);

        int shellColor = typedArray.getColor(R.styleable.BarometerView_shellColor,  Color.GRAY);
        int middleColor = typedArray.getColor(R.styleable.BarometerView_middleColor, Color.WHITE);
        int innerColor  = typedArray.getColor(R.styleable.BarometerView_innerColor,  Color.RED);
        typedArray.recycle();

        shellRectRadius = shellCircleRadius / 2;
        shellPaint = new Paint();
        shellPaint.setColor(shellColor);
        shellPaint.setStyle(Paint.Style.FILL);

        areaCircleRadius = shellCircleRadius - 5;
        areaRectRadius   = shellRectRadius - 5;
        areaPaint = new Paint();
        areaPaint.setColor(middleColor);
        areaPaint.setStyle(Paint.Style.FILL);

        innerCircleRadius = areaCircleRadius - 10;
        innerPaint = new Paint();
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.FILL);

        degreePaint = new Paint();
        degreePaint.setStrokeWidth(2);
        degreePaint.setColor(shellColor);
        degreePaint.setStyle(Paint.Style.FILL);

    }

    private void drawDegrees(Canvas canvas, int initX, float maxHeight, float innerEffectEndY) {

        float y = maxHeight;

        while (y <= innerEffectEndY) {

            float x = initX + shellRectRadius;
            canvas.drawLine(x - 15f, y, x, y, degreePaint);

            y += (innerEffectEndY - maxHeight) / 16;

        }

    }

}
