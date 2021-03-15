package it.uniba.magr.misurapp.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import lombok.Getter;

import static it.uniba.magr.misurapp.auth.LoginActivity.*;
import static it.uniba.magr.misurapp.auth.RegistrationActivity.*;

/**
 * The authentication activity.
 *
 * In this context you can select three different choices:
 * Sign in, registration and guest access.
 */
@SuppressWarnings({"squid:S110", "squid:S2696", "NotNullFieldNotInitialized"})
public class AuthActivity extends AppCompatActivity {

    private static final String SHARED_ANONYMOUS_USER_KEY = "anonymous_user";

    /**
     * The google auth request code.
     * <p>It's invented</p>.
     *
     * Requests:
     * - Google:                200
     * - firebase login:        201
     * - firebase registration: 202
     *
     */
    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 200;

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
        noAuthTextView    .setOnClickListener(view -> anonymousClick());

    }

    /**
     * Disable back button event to avoid a loop with
     * the {@link HomeActivity}.
     */
    @Override
    public void onBackPressed() {
        // nothing to do
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {

            case REQUEST_CODE_GOOGLE_SIGN_IN:

                handleGoogleLogin(resultCode);
                break;

            case REQUEST_CODE_LOGIN_ACTIVITY:
            case REQUEST_CODE_REGISTRATION_ACTIVITY:

                if (data.hasExtra(OPEN_REGISTRATION_ACTIVITY)
                        && data.getBooleanExtra(OPEN_REGISTRATION_ACTIVITY, false)) {
                    registrationClick();
                } else if (data.hasExtra(SUCCESSFUL_OPERATION)
                        && data.getBooleanExtra(SUCCESSFUL_OPERATION, false)) {
                    finish();
                }

                break;

            default: break;

        }

    }

    private void handleGoogleLogin(int resultCode) {

        String message;

        if (resultCode == RESULT_OK) {

            message = getString(R.string.correct_sign_in);

            setAnonymousUser(this, false);
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
            startActivityForResult(builder.build(), REQUEST_CODE_GOOGLE_SIGN_IN);

        }

    }

    /**
     * Perform the registration button click event.
     */
    private void registrationClick() {

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTRATION_ACTIVITY);


    }

    /**
     * Perform the anonymous text view click event.
     */
    private void anonymousClick() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && !AuthActivity.isAnonymousUser(this)) {
            return;
        }

        setAnonymousUser(this, true);
        finish();

    }

    /**
     * @param enabled true if you want to enable anonymous user.
     */
    public static void setAnonymousUser(@NotNull Context context, boolean enabled) {

        SharedPreferences.Editor editor = context.getSharedPreferences(
                SHARED_ANONYMOUS_USER_KEY, MODE_PRIVATE).edit();

        editor.putBoolean(SHARED_ANONYMOUS_USER_KEY, enabled);
        editor.apply();

    }

    /**
     * @param context The not null instance of a context.
     * @return True if the user is "logged in" as anonymous.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isAnonymousUser(@NotNull Context context) {

        SharedPreferences preferences = context.getSharedPreferences(
                SHARED_ANONYMOUS_USER_KEY, MODE_PRIVATE);

        return preferences.getBoolean(SHARED_ANONYMOUS_USER_KEY, false);

    }


    /**
     * @return True if user is logged in (also in anonymous user).
     */
    public static boolean isAuthenticated() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() != null;

    }

}