package it.uniba.magr.misurapp.tool.ruler;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

public class RulerFragment extends Fragment {

    private View rootView;

    private ListView rulerListView;

    private ListView rulerValueView;

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    @Nullable
    @Override
    @SuppressWarnings("SingleStatementInBlock") // remove after testing
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle bundle) {
        rootView = inflater.inflate(R.layout.fragment_ruler, container, false);

        rulerListView  = rootView.findViewById(R.id.ruler_list_view);
        rulerValueView = rootView.findViewById(R.id.ruler_value_view);

        FloatingActionButton saveButton = rootView.findViewById(R.id.ruler_fab_button_save);
        saveButton.setOnClickListener(view -> onSaveButtonClick());

        ViewGroup.LayoutParams segmentParams = rulerListView.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float millimeterPixel = displayMetrics.xdpi * (1.0f / 25.4f);
        float centimeterPixel = millimeterPixel * 10;

        int density = (int) centimeterPixel;
        float segmentWidth = 2.2f;

        segmentParams.width = (int) (density * segmentWidth);
        rulerListView.setLayoutParams(segmentParams);

        // Add listener to draw ruler after rootView has been drawn in order to obtain proper height
        globalLayoutListener = () -> globalLayoutListener(density);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        if (container != null) {

            HomeActivity homeActivity = (HomeActivity) container.getContext();
            homeActivity.getToolbar().setTitle(R.string.text_ruler);

        }

        return rootView;

    }

    private void onSaveButtonClick() {

        HomeActivity activity = (HomeActivity) getContext();
        assert activity != null;

        NavController navController = activity.getNavController();
        navController.navigate(R.id.action_nav_save_measure_fragment_to_ruler);

    }

    @Override
    public void onDetach() {

        super.onDetach();

        HomeActivity homeActivity = (HomeActivity) getContext();
        assert homeActivity != null;

        homeActivity.getToolbar().setTitle(R.string.text_nav_add_measure_item);

    }

    private void globalLayoutListener(int density) {

        if (rulerListView.getHeight() == 0) {

            RulerAdapter rulerAdapter = new RulerAdapter(
                    getActivity(),
                    R.layout.ruler_item_list_segment,
                    rootView.getHeight(),
                    density);

            // Draw initial ruler with rootView height
            rulerListView.setAdapter(rulerAdapter);

        } else {

            // Remove listener RulerListView has been drawn once
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);

            RulerAdapter rulerAdapter =  new RulerAdapter(
                    getActivity(),
                    R.layout.ruler_item_list_segment,
                    rulerListView.getHeight(),
                    density);

            RulerValueAdapter rulerValueAdapter = new RulerValueAdapter(
                    getActivity(),
                    R.layout.ruler_item_list_value,
                    rulerListView.getHeight(),
                    density);

            // Redraw ruler to maximum length
            rulerListView.setAdapter(rulerAdapter);
            rulerValueView.setAdapter(rulerValueAdapter);

        }

        rulerListView.setEnabled(false);
        rulerValueView.setEnabled(false);

    }

}
