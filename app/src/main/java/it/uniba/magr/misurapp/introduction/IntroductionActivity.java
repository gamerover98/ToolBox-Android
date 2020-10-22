package it.uniba.magr.misurapp.introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        if (savedInstanceState == null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.introduction_fragment_container, new FragmentPagination());
            fragmentTransaction.commit();

        }

        //if (!isAlreadyOpened()) {

            Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

           //return;

        //}

        /*
        SharedPreferences preferences = getSharedPreferences(SHARED_SLIDE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SHARED_SLIDE_KEY, SHARED_SLIDE_OPENED);
        editor.apply(); // use apply instead of commit method.
         */

    }

    /**
     * @return True if introduction activity is already opened.
     */
    private boolean isAlreadyOpened() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_SLIDE_KEY,MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_SLIDE_KEY, SHARED_SLIDE_CLOSED);

    }

}