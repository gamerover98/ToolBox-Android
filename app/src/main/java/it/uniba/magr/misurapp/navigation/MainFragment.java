package it.uniba.magr.misurapp.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;

public class MainFragment extends Fragment {

    private HomeActivity homeActivity;

    private FloatingActionButton buttonAddMeasure;

    private MaterialTextView buttonAddMeasureTextView;

    private Animation animationFabClosing;
    private Animation animationFabOpening;
    private Animation animationRotateBackward;
    private Animation animationRotateForward;

    boolean fabOpened = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {

        homeActivity = (HomeActivity) inflater.getContext();
        fabOpened = false;

        return inflater.inflate(R.layout.fragment_main_layout,
                parent, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {

        super.onActivityCreated(bundle);
        setupFloatingButtons();

        DrawerLayout drawerLayout = homeActivity.getDrawerLayout();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    private void setupFloatingButtons() {

        FloatingActionButton buttonOperation = homeActivity.findViewById(R.id.fab_button_operation);

        buttonAddMeasure = homeActivity.findViewById(R.id.fab_button_add_measure);
        animationFabOpening = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_open);
        animationFabClosing = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_close);
        animationRotateForward  = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_forward);
        animationRotateBackward = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_backward);

        buttonAddMeasureTextView = homeActivity.findViewById(R.id.fab_button_add_measure_text_view);
        buttonAddMeasure.setOnClickListener(this :: addMeasureClick);

        buttonOperation.setOnClickListener(this :: operationButtonClick);

    }

    private void addMeasureClick(@NotNull View view) {

        homeActivity.getToolbar().setTitle(R.string.text_nav_add_measure_item);
        homeActivity.getNavController().navigate(R.id.nav_add_measure_fragment);
        homeActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void operationButtonClick(@NotNull View view) {

        if (fabOpened) {

            view.startAnimation(animationRotateBackward);

            buttonAddMeasure.startAnimation(animationFabClosing);
            buttonAddMeasureTextView.setVisibility(View.INVISIBLE);
            buttonAddMeasure.setClickable(false);

            fabOpened = false;

        } else {

            view.startAnimation(animationRotateForward);

            buttonAddMeasure.startAnimation(animationFabOpening);
            buttonAddMeasureTextView.setVisibility(View.VISIBLE);
            buttonAddMeasure.setClickable(true);

            fabOpened = true;

        }

    }

}
