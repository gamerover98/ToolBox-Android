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

    private FloatingActionButton buttonCreateCard;
    private FloatingActionButton buttonFastMeasure;

    private MaterialTextView buttonCreateCardTextView;
    private MaterialTextView buttonFastMeasureTextView;

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

        buttonCreateCard  = homeActivity.findViewById(R.id.fab_button_create_card);
        buttonFastMeasure = homeActivity.findViewById(R.id.fab_button_fast_measure);
        animationFabOpening = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_open);
        animationFabClosing = AnimationUtils.loadAnimation(homeActivity, R.anim.fab_close);
        animationRotateForward  = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_forward);
        animationRotateBackward = AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_backward);

        buttonCreateCardTextView  = homeActivity.findViewById(R.id.fab_button_create_card_text_view);
        buttonFastMeasureTextView = homeActivity.findViewById(R.id.fab_button_fast_measure_text_view);

        buttonOperation.setOnClickListener(this :: operationButtonClick);

    }

    private void operationButtonClick(@NotNull View view) {

        if (fabOpened) {

            view.startAnimation(animationRotateBackward);

            buttonCreateCard.startAnimation(animationFabClosing);
            buttonFastMeasure.startAnimation(animationFabClosing);

            buttonCreateCardTextView.setVisibility(View.INVISIBLE);
            buttonFastMeasureTextView.setVisibility(View.INVISIBLE);

            buttonCreateCard.setClickable(false);
            buttonFastMeasure.setClickable(false);

            fabOpened = false;

        } else {

            view.startAnimation(animationRotateForward);
            buttonCreateCard.startAnimation(animationFabOpening);
            buttonFastMeasure.startAnimation(animationFabOpening);

            buttonCreateCardTextView.setVisibility(View.VISIBLE);
            buttonFastMeasureTextView.setVisibility(View.VISIBLE);

            buttonCreateCard.setClickable(true);
            buttonFastMeasure.setClickable(true);

            fabOpened = true;

        }

    }

}
