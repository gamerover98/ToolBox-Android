package it.uniba.magr.misurapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.HomeActivity;
import it.uniba.magr.misurapp.R;
import lombok.Getter;

/**
 * The authentication activity.
 *
 * In this context you can select three different choices:
 * Sign in, registration and guest access.
 */
@SuppressWarnings({"squid:S110", "squid:S2696", "NotNullFieldNotInitialized"})
public class AuthActivity extends AppCompatActivity {

    /**
     * Interim and for test purpose field.
     * <p>It will be replaced with a Shared Preference.</p>
     */
    private static boolean bypassAuthentication = false;

    /**
     * Gets the not null instance of the login button.
     */
    @Getter @NotNull
    private MaterialButton loginButton;

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
        registrationButton = findViewById(R.id.auth_registration_button);
        noAuthTextView     = findViewById(R.id.auth_no_authentication_text_view);

        loginButton       .setOnClickListener(view -> loginClick());
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

    /**
     * Perform the login button click event.
     */
    private void loginClick() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

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

        bypassAuthentication = true;
        finish();

    }

    public static boolean canBypassAuthentication() {
        return bypassAuthentication;
    }

}