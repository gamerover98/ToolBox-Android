package it.uniba.magr.misurapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Manage the language of the application.
 */
public final class LocaleUtil {

    /**
     * The name of the system language for the SharedPreferences.
     */
    public static final String SYSTEM_LANGUAGE = "SYSTEM";

    /**
     * The key of the shared preference.
     */
    private static final String APP_LANGUAGE_KEY = "APP_LANGUAGE";

    /**
     * Current system locale language.
     */
    private static Locale systemLocale;

    /**
     * Gets the locale language of the application context.
     *
     * @param context The not null instance of the application context.
     * @return The current application context.
     */
    @NotNull
    public static Locale getLocale(@NotNull Context context) {

        Locale locale = Locale.getDefault();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.contains(APP_LANGUAGE_KEY)) {

            String localeName = sharedPreferences.getString(APP_LANGUAGE_KEY, locale.getLanguage());
            locale = new Locale(localeName);

        }

        return locale;

    }

    /**
     * Sets the locale language of the application context.
     *
     * @param context The not null instance of the application context.
     * @param locale The not null instance of the new locale.
     * @return The new context with the new language.
     */
    @NotNull
    public static Context setLocale(@NotNull Context context, @NotNull Locale locale) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(APP_LANGUAGE_KEY, locale.getLanguage());
        editor.apply();

        LocaleList localeList = new LocaleList(locale);

        LocaleList.setDefault(localeList);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);

    }

    /**
     * Sets the system locale language of the application context.
     *
     * @param context The not null instance of the application context.
     * @return The new context with the system language.
     */
    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    public static Context setSystemLocale(@NotNull Context context) {
        return setLocale(context, systemLocale);
    }

    /**
     * Find a Language with its name, for instance: en or en_US.
     *
     * @param languageName The not null name language.
     * @return The Locale instance of the language, null if it is not found.
     */
    @Nullable
    public static Locale findLocale(@NotNull String languageName) {

        for (Locale locale : Locale.getAvailableLocales()) {

            String toString = locale.toString(); // en_US
            String name = locale.getLanguage();  // en

            if (languageName.equalsIgnoreCase(toString) || languageName.equalsIgnoreCase(name)) {
                return locale;
            }

        }

        return null;

    }

    /**
     * Update the local systemLocale field with
     * the current system locale language.
     */
    public static void onActivityCreated() {

        Locale current = Resources.getSystem().getConfiguration().getLocales().get(0);
        String name = current.getLanguage();

        if (name.contains("_")) {

            String[] split = name.split("_");
            Locale findIt = findLocale(split[0]);

            if (findIt != null) {
                current = findIt;
            }

        }

        systemLocale = current;

    }

    /**
     * Update the context when the context is attached.
     *
     * @param context The not null instance of the context that must be updated.
     * @return The updated and not null instance of context.
     */
    @NotNull
    public static Context onAttach(@NotNull Context context) {
        return setLocale(context, getLocale(context));
    }

    private LocaleUtil() {
        throw new IllegalStateException("This is a static class");
    }

}
