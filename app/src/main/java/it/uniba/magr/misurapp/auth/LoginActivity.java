package it.uniba.magr.misurapp.auth;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

import it.uniba.magr.misurapp.R;

@SuppressWarnings("squid:S110")
public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN_ACTIVITY = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialTextView regTextView = findViewById(R.id.text_signup);

        // underlined text
        regTextView.setPaintFlags(regTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        regTextView.setOnClickListener(view -> registerWithEmailTextClick());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void registerWithEmailTextClick() {

        Intent result = new Intent();
        setResult(REQUEST_CODE_LOGIN_ACTIVITY, result);

        finish();

    }

}
