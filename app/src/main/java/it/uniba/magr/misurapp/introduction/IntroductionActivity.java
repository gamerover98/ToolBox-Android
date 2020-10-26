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

    @NotNull
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        if (savedInstanceState == null) {

            viewPager = findViewById(R.id.intro_pager);
            viewPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));

            TabLayout tabLayout = findViewById(R.id.intro_tab_layout);
            tabLayout.setupWithViewPager(viewPager, true);

            ImageView backArrow = findViewById(R.id.intro_arrow_back);
            ImageView nextArrow = findViewById(R.id.intro_arrow_next);

            backArrow.setOnClickListener(this :: backClick);
            nextArrow.setOnClickListener(this :: nextClick);

        }

        Button startButton = findViewById(R.id.intro_button_start);
        startButton.setOnClickListener(this :: startClick);

    }

    private void backClick(View view) {
        viewPager.arrowScroll(View.FOCUS_LEFT);
    }

    private void nextClick(View view) {
        viewPager.arrowScroll(View.FOCUS_RIGHT);
    }

    private void startClick(View view) {

        SharedPreferences preferences = getSharedPreferences(SHARED_COMPLETED_INTRO_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_OPENED);
        editor.apply(); // use apply instead of commit method.

        finish();

    }

    /**
     * @return True if introduction activity is already opened.
     */
    public static boolean isAlreadyCompleted(AppCompatActivity activity) {

        SharedPreferences shared = activity.getSharedPreferences(SHARED_COMPLETED_INTRO_KEY,MODE_PRIVATE);
        return shared.getBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_CLOSED);

    }

}