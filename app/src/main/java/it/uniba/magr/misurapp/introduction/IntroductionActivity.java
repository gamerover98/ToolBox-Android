package it.uniba.magr.misurapp.introduction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import it.uniba.magr.misurapp.MainActivity;
import it.uniba.magr.misurapp.R;

/**
 * Introduction activity that will be shown at the first
 * application startup.
 */
public class IntroductionActivity extends AppCompatActivity {

    private static final String SHARED_COMPLETED_INTRO_KEY = "completed_into";

    private static final boolean SHARED_COMPLETED_INTRO_OPENED = true;
    private static final boolean SHARED_COMPLETED_INTRO_CLOSED = false;

    @Nullable
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

        if (isAlreadyCompleted()) {

            openMainActivity();
            return;

        }

        Button startButton = findViewById(R.id.intro_button_start);
        startButton.setOnClickListener(this ::startClick);

    }

    private void backClick(View view) {

        assert viewPager != null;
        viewPager.arrowScroll(View.FOCUS_LEFT);

    }

    private void nextClick(View view) {

        assert viewPager != null;
        viewPager.arrowScroll(View.FOCUS_RIGHT);

    }

    private void startClick(View view) {

        SharedPreferences preferences = getSharedPreferences(SHARED_COMPLETED_INTRO_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_OPENED);
        editor.apply(); // use apply instead of commit method.

        openMainActivity();

    }

    /**
     * @return True if introduction activity is already opened.
     */
    private boolean isAlreadyCompleted() {

        SharedPreferences shared = getSharedPreferences(SHARED_COMPLETED_INTRO_KEY,MODE_PRIVATE);
        return shared.getBoolean(SHARED_COMPLETED_INTRO_KEY, SHARED_COMPLETED_INTRO_CLOSED);

    }

    private void openMainActivity() {

        Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();

    }

}