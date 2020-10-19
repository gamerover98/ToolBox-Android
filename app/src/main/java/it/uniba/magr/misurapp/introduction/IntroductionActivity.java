package it.uniba.magr.misurapp.introduction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import it.uniba.magr.misurapp.MainActivity;
import it.uniba.magr.misurapp.R;

/**
 * Introduction activity that will be shown at the first
 * application startup.
 */
public class IntroductionActivity extends AppCompatActivity {

    private static final String SHARED_SLIDE_KEY = "slide";

    private static final boolean SHARED_SLIDE_OPENED = true;
    private static final boolean SHARED_SLIDE_CLOSED = false;

    /**
     * The viewPager instance that will be defined during the introduction
     * activity creation event.
     */
    private static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_activity);

        if (viewPager == null) {

            //viewPager = findViewById(R.id.ViewPage);
            //viewPager.setAdapter(new SlideViewIntroductionAdapter(this));

        }

        if (isAlreadyOpened()) {

            //Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);

        } else {

            SharedPreferences preferences = getSharedPreferences(SHARED_SLIDE_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean(SHARED_SLIDE_KEY, SHARED_SLIDE_OPENED);
            editor.apply(); // use apply instead of commit method.

        }

    }

    /**
     * @return True if introduction activity is already opened.
     */
    private boolean isAlreadyOpened() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_SLIDE_KEY,MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_SLIDE_KEY, SHARED_SLIDE_CLOSED);

    }

    /**
     * @return Gets the ViewPager instance of this activity.
     */
    @NonNull
    public static ViewPager getViewPager() {
        return viewPager;
    }

}