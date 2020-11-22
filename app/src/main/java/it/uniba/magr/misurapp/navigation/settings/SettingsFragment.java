package it.uniba.magr.misurapp.navigation.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

/**
 * The application settings fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String SHARED_SETTINGS_KEY = "settings";

    private static final String SHARED_FUNCTIONALITY_KEY = "functionality";

    private static final String COMPLETE_FUNCTIONALITY_KEY = "switch_complete_functionality";
    private static final String ESSENTIALS_FUNCTIONALITY_KEY = "switch_essentials_functionality";

    /**
     * The switch preference button of the complete application features access.
     */
    private SwitchPreferenceCompat completeFunctionalitySwitch;

    /**
     * The switch preference button of the essentials application features access.
     */
    private SwitchPreferenceCompat essentialsFunctionalitySwitch;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.application_settings, rootKey);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle bundle) {


        completeFunctionalitySwitch   = findPreference(COMPLETE_FUNCTIONALITY_KEY);
        essentialsFunctionalitySwitch = findPreference(ESSENTIALS_FUNCTIONALITY_KEY);

        completeFunctionalitySwitch  .setOnPreferenceChangeListener(this :: switchCompleteClick);
        essentialsFunctionalitySwitch.setOnPreferenceChangeListener(this :: switchEssentialsClick);

        assert getContext() != null;

        Context context = getContext();

        if (context instanceof HomeActivity) {

            HomeActivity homeActivity = (HomeActivity) context;
            homeActivity.getToolbar().setTitle(R.string.text_settings);

        }

        Functionality functionality = getFunctionality(context);

        if (functionality == Functionality.COMPLETE) {

            completeFunctionalitySwitch.setChecked(true);
            essentialsFunctionalitySwitch.setChecked(false);

        } else {

            completeFunctionalitySwitch.setChecked(false);
            essentialsFunctionalitySwitch.setChecked(true);

        }

        return super.onCreateView(inflater, container, bundle);

    }

    private boolean switchCompleteClick(Preference preference, Object newValue) {

        assert getContext() != null;
        Functionality functionality;

        if (!completeFunctionalitySwitch.isChecked()) {

            completeFunctionalitySwitch.setChecked(true);
            essentialsFunctionalitySwitch.setChecked(false);
            functionality = Functionality.COMPLETE;

        } else {

            completeFunctionalitySwitch.setChecked(false);
            essentialsFunctionalitySwitch.setChecked(true);
            functionality = Functionality.ESSENTIALS;

        }

        setFunctionality(getContext(), functionality);
        return true;

    }

    private boolean switchEssentialsClick(Preference preference, Object newValue) {

        assert getContext() != null;

        assert getContext() != null;
        Functionality functionality;

        if (!essentialsFunctionalitySwitch.isChecked()) {

            essentialsFunctionalitySwitch.setChecked(true);
            completeFunctionalitySwitch.setChecked(false);
            functionality = Functionality.ESSENTIALS;

        } else {

            essentialsFunctionalitySwitch.setChecked(false);
            completeFunctionalitySwitch.setChecked(true);
            functionality = Functionality.COMPLETE;

        }

        setFunctionality(getContext(), functionality);
        return true;

    }

    /**
     * @param context A not null instance of an activity context.
     * @return The functionality enumeration value.
     */
    @NotNull
    public static Functionality getFunctionality(@NotNull Context context) {

        SharedPreferences preferences = context
                .getSharedPreferences(SHARED_SETTINGS_KEY, Context.MODE_PRIVATE);
        String stringEnumeration = preferences
                .getString(SHARED_FUNCTIONALITY_KEY, Functionality.COMPLETE.name());

        assert stringEnumeration != null;
        return Functionality.valueOf(stringEnumeration.toUpperCase());

    }

    /**
     * @param context A not null instance of an activity context.
     * @param functionality The functionality enumeration value.
     */
    private static void setFunctionality(@NotNull Context context,
                                         @NotNull Functionality functionality) {

        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_SETTINGS_KEY, Context.MODE_PRIVATE).edit();

        editor.putString(SHARED_FUNCTIONALITY_KEY, functionality.name());
        editor.apply();

    }

    /**
     * The application functionality features.
     */
    public enum Functionality {
        COMPLETE, ESSENTIALS
    }

}
