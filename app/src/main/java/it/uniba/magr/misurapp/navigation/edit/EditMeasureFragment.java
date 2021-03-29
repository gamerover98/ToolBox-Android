package it.uniba.magr.misurapp.navigation.edit;

import static it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureDetector.BUNDLE_DESCRIPTION_KEY;
import static it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureDetector.BUNDLE_MEASURE_ID_KEY;
import static it.uniba.magr.misurapp.navigation.main.recycle.MeasureRecyclerGestureDetector.BUNDLE_TITLE_KEY;
import static it.uniba.magr.misurapp.util.GenericUtil.getTextFromInputLayout;
import static it.uniba.magr.misurapp.util.GenericUtil.setTextToInputLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.DatabaseManager;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.database.dao.MeasurementsDao;
import it.uniba.magr.misurapp.navigation.NavigationFragment;
import it.uniba.magr.misurapp.util.GenericUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class EditMeasureFragment extends NavigationFragment {

    /**
     * The no parameters rule.
     */
    public static final int NO_PARAMETERS = 0;

    /**
     * The existing measure id.
     */
    private int measureId;

    protected EditMeasureFragment(@NotNull EditMeasureNavigable navigable) {
        super(navigable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {

        super.onActivityCreated(bundle);

        EditMeasureNavigable navigable = (EditMeasureNavigable) getNavigable();
        int parametersViewId = navigable.getParameterViewId();

        if (parametersViewId == NO_PARAMETERS) {
            return;
        }

        bundle = getArguments();

        if (bundle == null) {
            throw new IllegalStateException("The bundle cannot be null");
        }

        measureId = bundle.getInt(BUNDLE_MEASURE_ID_KEY);

        String title       = bundle.getString(BUNDLE_TITLE_KEY);
        String description = bundle.getString(BUNDLE_DESCRIPTION_KEY);
        assert title != null && description != null;

        FragmentActivity activity = getActivity();
        assert activity != null;

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.edit_parameters_fragment_view,
                new ParametersFragment(parametersViewId, () -> {

                    Bundle navArguments = getArguments();

                    if (navArguments == null) {
                        navArguments = new Bundle();
                    }

                    handleParametersCreation(activity, navArguments);

                }));

        fragmentTransaction.commit();

        MaterialButton editButton = activity.findViewById(R.id.edit_button);
        editButton.setOnClickListener(this :: handleSaveClick);

        setTitle(title);
        setDescription(description);

    }

    /**
     * The method that will be executed
     * during the onActivityCreated() lifecycle event.
     *
     * @param activity The not null fragment activity.
     * @param bundle The not null navigation bundle.
     *
     */
    protected abstract void handleParametersCreation(@NotNull FragmentActivity activity, @NotNull Bundle bundle);

    /**
     * @return the inserted title.
     */
    @NotNull
    public String getTitle() {

        assert getActivity() != null;
        return getTextFromInputLayout(getActivity(), R.id.edit_input_text_box_title).trim();

    }

    /**
     * @return the inserted description.
     */
    @NotNull
    public String getDescription() {

        assert getActivity() != null;
        return getTextFromInputLayout(getActivity(), R.id.edit_input_text_box_description);

    }

    /**
     * @param title The title text.
     */
    public void setTitle(@NotNull String title) {

        assert getActivity() != null;
        setTextToInputLayout(getActivity(), R.id.edit_input_text_box_title, title);

    }

    /**
     * @param description The description text.
     */
    public void setDescription(@NotNull String description) {

        assert getActivity() != null;
        setTextToInputLayout(getActivity(), R.id.edit_input_text_box_description, description);

    }

    /**
     * This method perform the save button click.
     * @param view The not null button view.
     */
    public void handleSaveClick(@NotNull View view) {

        FragmentActivity activity = getActivity();
        assert activity != null;

        // title text check
        String title = getTitle();

        if (title.isEmpty()) {

            Toast.makeText(activity, R.string.text_insert_title, Toast.LENGTH_LONG).show();
            return;

        }

        openLoadingLayout(view);

        Thread thread = new Thread(() -> asyncSaving((HomeActivity) activity));
        thread.start();

    }

    /**
     * Show the elevated loading layout.
     * @param view The not null view.
     */
    private void openLoadingLayout(@NotNull View view) {

        Activity activity = getActivity();
        assert activity != null;

        ConstraintLayout loadingLayout = activity.findViewById(R.id.edit_layout_loading);
        ConstraintLayout fieldsLayout  = activity.findViewById(R.id.edit_layout_fields);
        MaterialButton   editButton    = activity.findViewById(R.id.edit_button);

        loadingLayout.setVisibility(View.VISIBLE);
        setLayoutChildrenClick(fieldsLayout, false);
        editButton.setClickable(false);

        GenericUtil.closeKeyboard(view, activity);

    }

    /**
     * Asynchronously measure saving.
     */
    private void asyncSaving(@NotNull HomeActivity activity) {

        HomeActivity homeActivity = (HomeActivity) getActivity();
        assert homeActivity != null;

        DatabaseManager databaseManager = homeActivity.getDatabaseManager();
        MeasurementsDao measurementsDao = databaseManager.measurementsDao();
        Measure measure = measurementsDao.getMeasure(measureId);

        measure.setTitle(getTitle());
        measure.setDescription(getDescription());

        measurementsDao.updateMeasure(measure);

        NavHostFragment navHostFragment = activity.getNavHostFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();

        List<Fragment> currentFragments = fragmentManager.getFragments();

        if (currentFragments.isEmpty()) {
            return;
        }

        Fragment current = currentFragments.get(0);

        if (this != current) {
            return;
        }

        activity.runOnUiThread(() -> {

            NavController navController = navHostFragment.getNavController();
            navController.navigateUp();

            Toast.makeText(activity, R.string.text_save_complete, Toast.LENGTH_LONG).show();

        });

    }

    /**
     * Disable internal views layout clicking and editing.
     * @param viewGroup The not null view group instance.
     * @param editable True if you want to enable it.
     */
    private static void setLayoutChildrenClick(@NotNull ViewGroup viewGroup, boolean editable) {

        for (int i = 0; i < viewGroup.getChildCount() ; i++) {

            View childView = viewGroup.getChildAt(i);

            if (childView instanceof ViewGroup) {
                setLayoutChildrenClick((ViewGroup) childView, editable);
            }

            childView.setEnabled(editable);

            if (childView instanceof EditText) {

                EditText editText = (EditText) childView;
                editText.setEnabled(editable);

            }

        }

    }

    /**
     * The fragment class that will be placed between the title and description views.
     */
    @RequiredArgsConstructor
    public static class ParametersFragment extends Fragment {

        /**
         * The layout ID.
         */
        @Getter
        @LayoutRes
        private final int parameterViewId;

        /**
         * The method that will be executed
         * during the onActivityCreated() lifecycle event.
         */
        @Getter @NotNull
        private final Runnable behaviourMethod;

        @Nullable
        @Override
        public View onCreateView(@NotNull LayoutInflater inflater,
                                 @Nullable ViewGroup parent,
                                 @Nullable Bundle bundle) {
            return inflater.inflate(parameterViewId, parent, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle bundle) {

            super.onActivityCreated(bundle);
            behaviourMethod.run();

        }

    }

}
