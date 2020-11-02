package it.uniba.magr.misurapp.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import lombok.Getter;

import static it.uniba.magr.misurapp.auth.LoginActivity.*;

/**
 * The authentication activity.
 *
 * In this context you can select three different choices:
 * Sign in, registration and guest access.
 */
@SuppressWarnings({"squid:S110", "squid:S2696", "NotNullFieldNotInitialized"})
public class AuthActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 123;

    /**
     * Gets the not null instance of the login button.
     */
    @Getter @NotNull
    private MaterialButton loginButton;

    /**
     * Gets the not null instance of the Google authentication button.
     */
    @Getter @NotNull
    private SignInButton googleAuthButton;

    /**
     * Gets the not null instance of the registration button.
     */
    @Getter @NotNull
    private MaterialButton registrationButton;

    /**
     * Gets the not null instance of the no authentication
     * text view.
     */
    @Getter @NotNull
    private MaterialTextView noAuthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loginButton        = findViewById(R.id.auth_login_button);
        googleAuthButton   = findViewById(R.id.auth_google_button);

        registrationButton = findViewById(R.id.auth_registration_button);
        noAuthTextView     = findViewById(R.id.auth_no_authentication_text_view);

        loginButton       .setOnClickListener(view -> loginClick());
        googleAuthButton  .setOnClickListener(view -> googleAuthClick());
        registrationButton.setOnClickListener(view -> registrationClick());
        noAuthTextView    .setOnClickListener(view -> noAuthClick());

    }

    /**
     * Disable back button event to avoid a loop with
     * the {@link HomeActivity}.
     */
    @Override
    public void onBackPressed() {
        // noting to do
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:        handleGoogleLogin(resultCode); break;
            case REQUEST_CODE_LOGIN_ACTIVITY: registrationClick();           break;
            default: break;
        }

    }

    private void handleGoogleLogin(int resultCode) {

        String message;

        if (resultCode == RESULT_OK) {

            message = getString(R.string.correct_sign_in);
            finish();

        } else {
            message = getString(R.string.incorrect_sign_in);
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    /**
     * Perform the login button click event.
     */
    private void loginClick() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_ACTIVITY);

    }

    /**
     * Perform the authentication from google button click event.
     */
    private void googleAuthClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {

            AuthUI.IdpConfig googleAuthUI = new AuthUI.IdpConfig.GoogleBuilder().build();
            AuthUI.SignInIntentBuilder builder = AuthUI.getInstance().createSignInIntentBuilder();

            builder.setAvailableProviders(Collections.singletonList(googleAuthUI));
            startActivityForResult(builder.build(), REQUEST_CODE_SIGN_IN);

        }

    }

    /**
     * Perform the registration button click event.
     */
    private void registrationClick() {

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);

    }

    /**
     * Perform the no authentication text view click event.
     */
    private void noAuthClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {

            Task<AuthResult> authTask = firebaseAuth.signInAnonymously();

            authTask.addOnCompleteListener(this, task -> {

                if (task.isSuccessful()) {
                    finish();
                } else {

                    if (task.getException() != null) {
                        Log.e("pelopelo", "login error", task.getException());
                    }

                    String message = getResources().getString(R.string.incorrect_sign_in);
                    Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();

                }

            });



        }

    }

    /**
     * @return True if user is logged in (also in anonymous user).
     */
    public static boolean isAuthenticated() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() != null;

    }

}