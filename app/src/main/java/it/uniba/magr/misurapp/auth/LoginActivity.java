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

    public static final String OPEN_REGISTRATION_ACTIVITY = "open_registration";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialTextView regTextView = findViewById(R.id.text_signup);

        // underlined text
        regTextView.setPaintFlags(regTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        regTextView.setOnClickListener(view -> registerWithEmailTextClick());

    }

    private void registerWithEmailTextClick() {

        Intent result = new Intent();

        result.putExtra(OPEN_REGISTRATION_ACTIVITY, true);
        setResult(REQUEST_CODE_LOGIN_ACTIVITY, result);

        finish();

    }

}
