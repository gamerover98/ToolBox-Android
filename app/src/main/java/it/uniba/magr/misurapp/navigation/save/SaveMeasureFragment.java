package it.uniba.magr.misurapp.navigation.save;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.NavigationFragment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static it.uniba.magr.misurapp.util.GenericUtil.getTextFromInputLayout;
import static it.uniba.magr.misurapp.util.GenericUtil.setTextToInputLayout;

@SuppressWarnings("unused") // unused methods
public abstract class SaveMeasureFragment extends NavigationFragment {

    /**
     * The no parameters rule.
     */
    public static final int NO_PARAMETERS = 0;

    protected SaveMeasureFragment(SaveMeasureNavigable navigable) {
        super(navigable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {

        super.onActivityCreated(bundle);

        SaveMeasureNavigable navigable = (SaveMeasureNavigable) getNavigable();
        int parametersViewId = navigable.getParameterViewId();

        if (parametersViewId == NO_PARAMETERS) {
            return;
        }

        FragmentActivity activity = getActivity();
        assert activity != null;

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.save_parameters_fragment_view,
                new ParametersFragment(parametersViewId, this :: handleParametersCreation));
        fragmentTransaction.commit();

    }

    /**
     * The method that will be executed
     * during the onActivityCreated() lifecycle event.
     */
    protected abstract void handleParametersCreation();

    /**
     * @return the inserted title.
     */
    @NotNull
    public String getTitle() {

        assert getActivity() != null;
        return getTextFromInputLayout(getActivity(), R.id.save_input_text_box_title);

    }

    /**
     * @return the inserted description.
     */
    @NotNull
    public String getDescription() {

        assert getActivity() != null;
        return getTextFromInputLayout(getActivity(), R.id.save_input_text_box_description);

    }

    /**
     * @param title The title text.
     */
    public void setTitle(@NotNull String title) {

        assert getActivity() != null;
        setTextToInputLayout(getActivity(), R.id.save_input_text_box_title, title);

    }

    /**
     * @param description The description text.
     */
    public void setDescription(@NotNull String description) {

        assert getActivity() != null;
        setTextToInputLayout(getActivity(), R.id.save_input_text_box_description, description);

    }

    /**
     * The fragment class that will be placed between the title and description views.
     */
    @RequiredArgsConstructor
    public static class ParametersFragment extends Fragment {

        /**
         * The layout ID.
         */
        @Getter @LayoutRes
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
