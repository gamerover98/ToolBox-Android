package it.uniba.magr.toolbox.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.IdRes;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public final class GenericUtil {

    private GenericUtil() {
        throw new IllegalStateException("This is a static class");
    }

    /**
     * @param context A not null instance of a context.
     * @return The exact physical X axis pixels per millimeters of the screen in the X dimension.
     */
    public static float getPixelsSizeX(@NotNull Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.xdpi * (1.0f / 25.4f);

    }

    /**
     * @param context A not null instance of a context.
     * @return The exact physical Y axis pixels per millimeters of the screen in the X dimension.
     */
    public static float getPixelsSizeY(@NotNull Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.ydpi * (1.0f / 25.4f);

    }

    /**
     * Gets the text of an input text component.
     *
     * @param activity A not null instance of an Activity.
     * @param textInputLayoutID The resource id of the input text layout.
     * @return A not null string that contains the content of the input text.
     */
    @NotNull
    public static String getTextFromInputLayout(@NotNull Activity activity,
                                                @IdRes int textInputLayoutID) {

        TextInputLayout inputLayout = activity.findViewById(textInputLayoutID);
        assert inputLayout != null;

        EditText editText = inputLayout.getEditText();
        assert editText != null;

        return editText.getText().toString().trim();

    }

    /**
     * Gets the int value of an input text component.
     *
     * @param activity A not null instance of an Activity.
     * @param textInputLayoutID The resource id of the input text layout.
     * @return A not null string that contains the content of the input text.
     */
    public static Number getNumberFromInputLayout(@NotNull Activity activity,
                                                  @IdRes int textInputLayoutID) {

        TextInputLayout inputLayout = activity.findViewById(textInputLayoutID);
        assert inputLayout != null;

        EditText editText = inputLayout.getEditText();
        assert editText != null;

        String stringNumber = editText.getText().toString().trim();

        if (stringNumber.isEmpty()) {
            return 0;
        }

        return MathUtil.getNumber(stringNumber);

    }

    /**
     * Gets the text of an input text component.
     *
     * @param activity A not null instance of an Activity.
     * @param textInputLayoutID The resource id of the input text layout.
     * @param text The not null text.
     */
    public static void setTextToInputLayout(@NotNull Activity activity,
                                            @IdRes int textInputLayoutID,
                                            @NotNull String text) {

        TextInputLayout inputLayout = activity.findViewById(textInputLayoutID);
        assert inputLayout != null;

        EditText editText = inputLayout.getEditText();
        assert editText != null;

        editText.setText(text);

    }

    /**
     * Gets the text of an input text component.
     *
     * @param activity A not null instance of an Activity.
     * @param textInputLayoutID The resource id of the input text layout.
     * @param value The long value.
     */
    public static void setNumberToInputLayout(@NotNull Activity activity,
                                              @IdRes int textInputLayoutID,
                                              long value) {

        TextInputLayout inputLayout = activity.findViewById(textInputLayoutID);
        assert inputLayout != null;

        EditText editText = inputLayout.getEditText();
        assert editText != null;

        editText.setText(String.valueOf(value));

    }

    /**
     * Gets the text of an input text component.
     *
     * @param activity A not null instance of an Activity.
     * @param textInputLayoutID The resource id of the input text layout.
     * @param value The decimal value.
     */
    public static void setNumberToInputLayout(@NotNull Activity activity,
                                              @IdRes int textInputLayoutID,
                                              double value) {

        TextInputLayout inputLayout = activity.findViewById(textInputLayoutID);
        assert inputLayout != null;

        EditText editText = inputLayout.getEditText();
        assert editText != null;

        editText.setText(String.valueOf(value));

    }

    /**
     * Set the application as fullscreen.
     * @param window The activity window instance.
     */
    public static void hideSystemUI(@NotNull Window window) {

        View decoder = window.getDecorView();
        decoder.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

    }

    /**
     * Remove the application from the fullscreen view.
     * @param window The activity window instance.
     */
    public static void showSystemUI(@NotNull Window window) {

        View decoder = window.getDecorView();
        decoder.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

    }

    /**
     * Hide the opened user keyboard.
     *
     * @param view The view where the keyboard is opened.
     * @param activity The not null view activity.
     */
    public static void closeKeyboard(@NotNull View view, @NotNull Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}
