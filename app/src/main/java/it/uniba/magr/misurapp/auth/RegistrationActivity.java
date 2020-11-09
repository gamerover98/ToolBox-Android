package it.uniba.magr.misurapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;

import it.uniba.magr.misurapp.R;

import static it.uniba.magr.misurapp.auth.LoginActivity.*;

@SuppressWarnings("squid:S110")
public class RegistrationActivity extends AppCompatActivity {

    private static final String REGISTRATION_LOG_TAG = "Registration";

    public static final int REQUEST_CODE_REGISTRATION_ACTIVITY = 300;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        MaterialButton loginButton = findViewById(R.id.registration_button);
        loginButton.setOnClickListener(this :: performRegistrationClick);

    }

    private void performRegistrationClick(View view) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            return;
        }

        String name = getName();
        String surname = getSurname();
        String email = getEmail();
        String hashedPassword = getHashedPassword();
        String confirmedHashedPassword = getConfirmHashedPassword();

        if (name.isEmpty()) {

            Toast.makeText(this,
                    getResources().getString(R.string.text_auth_empty_name),
                    Toast.LENGTH_LONG).show();
            return;

        }

        if (surname.isEmpty()) {

            Toast.makeText(this,
                    getResources().getString(R.string.text_auth_empty_surname),
                    Toast.LENGTH_LONG).show();
            return;

        }

        if (email.isEmpty()) {

            Toast.makeText(this,
                    getResources().getString(R.string.text_auth_empty_email),
                    Toast.LENGTH_LONG).show();
            return;

        }

        if (hashedPassword.isEmpty()) {

            Toast.makeText(this,
                    getResources().getString(R.string.text_auth_empty_password),
                    Toast.LENGTH_LONG).show();
            return;

        }

        if (!hashedPassword.equals(confirmedHashedPassword)) {

            Toast.makeText(this,
                    getResources().getString(R.string.text_auth_mismatch_password),
                    Toast.LENGTH_LONG).show();
            return;

        }

        Task<AuthResult> loginTask = firebaseAuth.createUserWithEmailAndPassword(email, hashedPassword);
        loginTask.addOnCompleteListener(this :: completeRegistration);

    }

    private void completeRegistration(@NotNull Task<AuthResult> authResultTask) {

        if (authResultTask.isSuccessful()) {

            String message = getString(R.string.correct_sign_in);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            AuthActivity.setAnonymousUser(this, false);
            Intent result = new Intent();

            result.putExtra(SUCCESSFUL_OPERATION, true);
            setResult(REQUEST_CODE_LOGIN_ACTIVITY, result);
            finish();

        } else {

            Exception exception = authResultTask.getException();
            String message;

            if (exception instanceof FirebaseAuthInvalidCredentialsException) {

                FirebaseAuthInvalidCredentialsException firebaseEx =
                        (FirebaseAuthInvalidCredentialsException) exception;

                String errorCode = firebaseEx.getErrorCode();

                if (errorCode.equals(ERROR_INVALID_EMAIL)) {
                    message = getResources().getString(R.string.text_auth_incorrect_email);
                } else if (errorCode.equals(ERROR_INVALID_PASSWORD)) {
                    message = getResources().getString(R.string.text_auth_incorrect_password);
                } else {

                    Log.w(REGISTRATION_LOG_TAG, "Registration error code: " + errorCode);
                    message = getResources().getString(R.string.incorrect_sign_in);

                }

            } else if (exception instanceof FirebaseAuthInvalidUserException) {
                message = getResources().getString(R.string.text_auth_incorrect_email);
            } else {

                Log.e(REGISTRATION_LOG_TAG, "Error occurred during sign upping", exception);
                message = getResources().getString(R.string.incorrect_sign_in);

            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        }

    }

    /**
     * @return the inserted name.
     */
    @NotNull
    private String getName() {
        return getTextFromInputLayout(this, R.id.registration_input_text_box_name);
    }

    /**
     * @return the inserted name.
     */
    @NotNull
    private String getSurname() {
        return getTextFromInputLayout(this, R.id.registration_input_text_box_surname);
    }

    /**
     * @return the inserted email.
     */
    @NotNull
    private String getEmail() {
        return getTextFromInputLayout(this, R.id.registration_input_text_box_email);
    }

    /**
     * @return The inserted password protected by MD5 hash.
     */
    @NotNull
    private String getHashedPassword() {

        String text = getTextFromInputLayout(this, R.id.registration_input_text_box_password);

        if (text.trim().isEmpty()) {
            return "";
        }

        try {

            return getMD5(text);

        } catch (NoSuchAlgorithmException nsaEx) {
            throw new IllegalStateException("Cannot get the MD5 algorithm");
        }

    }

    /**
     * @return The conformed inserted password protected by MD5 hash.
     */
    @NotNull
    private String getConfirmHashedPassword() {

        String text = getTextFromInputLayout(this, R.id.registration_input_text_box_confirm_password);

        if (text.trim().isEmpty()) {
            return "";
        }

        try {

            return getMD5(text);

        } catch (NoSuchAlgorithmException nsaEx) {
            throw new IllegalStateException("Cannot get the MD5 algorithm");
        }

    }

}
