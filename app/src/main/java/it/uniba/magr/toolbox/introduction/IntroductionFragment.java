package it.uniba.magr.toolbox.introduction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.toolbox.HomeActivity;
import it.uniba.magr.toolbox.R;

import static android.app.Activity.*;

/**
 * Introduction activity that will be shown at the first
 * application startup.
 */
@SuppressWarnings({"squid:S110", "NotNullFieldNotInitialized"})
public class IntroductionFragment extends Fragment {

    public static final String SHARED_COMPLETED_INTRO_KEY = "completed_into";

    private static final boolean SHARED_COMPLETED_INTRO_OPENED = true;
    private static final boolean SHARED_COMPLETED_INTRO_CLOSED = false;

    /**
     * With this component, you can manage the dynamic and horizontal flow scroll
     * of introduction fragments.
     */
    @NotNull
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle bundle) {
        return inflater.inflate(R.layout.fragment_introduction, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        FragmentActivity activity = getActivity();
        assert activity != null;

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        viewPager = activity.findViewById(R.id.intro_pager);
        viewPager.setAdapter(new IntroPagerAdapter(fragmentManager));

        TabLayout tabLayout = activity.findViewById(R.id.intro_tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        ImageView backArrow = activity.findViewById(R.id.intro_arrow_back);
        ImageView nextArrow = activity.findViewById(R.id.intro_arrow_next);

        backArrow.setOnClickListener(view -> backClick());
        nextArrow.setOnClickListener(view -> nextClick());

        Button startButton = activity.findViewById(R.id.intro_button_start);
        startButton.setOnClickListener(view -> startClick());

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        HomeActivity homeActivity = (HomeActivity) getActivity();
        assert homeActivity != null;

        homeActivity.reload();

    }

    /**
     * Perform the back arrow clicking.
     */
    private void backClick() {
        viewPager.arrowScroll(View.FOCUS_LEFT);
    }

    /**
     * Perform the next arrow clicking.
     */
    private void nextClick() {
        viewPager.arrowScroll(View.FOCUS_RIGHT);
    }

    /**
     * Perform the start button clicking.
     */
    private void startClick() {

        FragmentActivity activity = getActivity();
        assert activity != null;

        SharedPreferences.Editor editor = activity.getSharedPreferences(
                SHARED_COMPLETED_INTRO_KEY, MODE_PRIVATE).edit();

        editor.putBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_OPENED);
        editor.apply(); // use apply instead of commit method.

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(this).commit();

    }

    /**
     * @param activity A not-null activity instance to get shared preferences.
     * @return True if introduction activity is already completed.
     */
    public static boolean isCompleted(@NotNull AppCompatActivity activity) {

        SharedPreferences preferences = activity.getSharedPreferences(
                SHARED_COMPLETED_INTRO_KEY, MODE_PRIVATE);

        return preferences.getBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_CLOSED);

    }

}