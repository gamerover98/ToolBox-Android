package it.uniba.magr.misurapp.introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;

/**
 * Introduction activity that will be shown at the first
 * application startup.
 */
@SuppressWarnings({"squid:S110", "NotNullFieldNotInitialized"})
public class IntroductionActivity extends AppCompatActivity {

    public static final String SHARED_COMPLETED_INTRO_KEY = "completed_into";

    private static final boolean SHARED_COMPLETED_INTRO_OPENED = true;
    private static final boolean SHARED_COMPLETED_INTRO_CLOSED = false;

    /**
     * With this component, you can manage the dynamic and horizontal flow scroll
     * of introduction fragments.
     */
    @NotNull
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_introduction);

        viewPager = findViewById(R.id.intro_pager);
        viewPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.intro_tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        ImageView backArrow = findViewById(R.id.intro_arrow_back);
        ImageView nextArrow = findViewById(R.id.intro_arrow_next);

        backArrow.setOnClickListener(view -> backClick());
        nextArrow.setOnClickListener(view -> nextClick());

        Button startButton = findViewById(R.id.intro_button_start);
        startButton.setOnClickListener(view -> startClick());

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

        SharedPreferences.Editor editor = getSharedPreferences(
                SHARED_COMPLETED_INTRO_KEY, MODE_PRIVATE).edit();

        editor.putBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_OPENED);
        editor.apply(); // use apply instead of commit method.

        finish();

    }

    /**
     * @return True if introduction activity is already completed.
     */
    public static boolean isAlreadyCompleted(AppCompatActivity activity) {

        SharedPreferences preferences = activity.getSharedPreferences(
                SHARED_COMPLETED_INTRO_KEY, MODE_PRIVATE);

        return preferences.getBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_CLOSED);

    }

}