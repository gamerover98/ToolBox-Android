package it.uniba.magr.misurapp.tool.ruler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

public class RulerFragment extends Fragment {

    @Override
    public void onAttach(@NotNull Context context) {

        super.onAttach(context);

        Activity activity = (Activity) context;
        Window window = activity.getWindow();
        View decoder = window.getDecorView();

        int uiProperties = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decoder.setSystemUiVisibility(uiProperties);

    }

    @Nullable
    @Override
    @SuppressWarnings("SingleStatementInBlock") // remove after testing
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle bundle) {

        View rootView = inflater.inflate(R.layout.fragment_ruler, parent, false);

        if (parent != null) {

            FloatingActionButton saveButton = rootView.findViewById(R.id.ruler_fab_button_save);
            saveButton.setOnClickListener(view -> onSaveButtonClick());

        }

        return rootView;

    }

    @Override
    public void onDetach() {

        super.onDetach();

        HomeActivity homeActivity = (HomeActivity) getContext();
        assert homeActivity != null;

        Window window = homeActivity.getWindow();
        View decoder = window.getDecorView();

        int uiProperties = View.SYSTEM_UI_FLAG_VISIBLE;
        decoder.setSystemUiVisibility(uiProperties);

    }

    private void onSaveButtonClick() {

        HomeActivity activity = (HomeActivity) getContext();
        assert activity != null;

        NavController navController = activity.getNavController();
        navController.navigate(R.id.action_nav_save_measure_fragment_to_ruler);

    }

}
