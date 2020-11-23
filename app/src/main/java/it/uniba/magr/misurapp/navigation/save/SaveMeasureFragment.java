package it.uniba.magr.misurapp.navigation.save;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.NavigationFragment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class SaveMeasureFragment extends NavigationFragment {

    public static final int NO_PARAMETERS = 0;

    public SaveMeasureFragment(SaveMeasureNavigable navigable) {
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
                new ParametersFragment(parametersViewId));
        fragmentTransaction.commit();

    }

    @RequiredArgsConstructor
    public static class ParametersFragment extends Fragment {

        @Getter @LayoutRes
        private final int parameterViewId;

        @Nullable
        @Override
        public View onCreateView(@NotNull LayoutInflater inflater,
                                 @Nullable ViewGroup parent,
                                 @Nullable Bundle bundle) {
            return inflater.inflate(parameterViewId, parent, false);
        }

    }

}
