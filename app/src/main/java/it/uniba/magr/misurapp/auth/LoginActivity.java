package it.uniba.magr.misurapp.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.uniba.magr.misurapp.R;

@SuppressWarnings("squid:S110")
public class LoginActivity extends AppCompatActivity {

    static final String MD5_DIGEST = "MD5";

    static final int MD5_POSITIVE_SIGNUM_DIGEST = 1;
    static final int MD5_CHAR_LENGTH            = 16;
    static final int MD5_LENGTH                 = 32;

    static final String ERROR_INVALID_EMAIL    = "ERROR_INVALID_EMAIL";
    static final String ERROR_INVALID_PASSWORD = "ERROR_WRONG_PASSWORD";

    private static final String LOGIN_LOG_TAG = "Login";

    public static final int REQUEST_CODE_LOGIN_ACTIVITY = 200;

    public static final String OPEN_REGISTRATION_ACTIVITY = "open_registration";

    public static final String SUCCESSFUL_OPERATION = "successful";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialTextView regTextView = findViewById(R.id.text_signup);

        // underlined text
        regTextView.setPaintFlags(regTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        regTextView.setOnClickListener(view -> registerWithEmailTextClick());

        MaterialButton loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this :: performLoginClick);

    }

    private void performLoginClick(View view) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            return;
        }

        String email = getEmail();
        String hashedPassword = getHashedPassword();

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

        Task<AuthResult> loginTask = firebaseAuth.signInWithEmailAndPassword(email, hashedPassword);
        loginTask.addOnCompleteListener(this :: completeLogin);

    }

    private void completeLogin(@NotNull Task<AuthResult> authResultTask) {

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

                    Log.w(LOGIN_LOG_TAG, "Login error code: " + errorCode);
                    message = getResources().getString(R.string.incorrect_sign_in);

                }

            } else if (exception instanceof FirebaseAuthInvalidUserException) {
                message = getResources().getString(R.string.text_auth_incorrect_email);
            } else {

                Log.e(LOGIN_LOG_TAG, "Error occurred during logging in", exception);
                message = getResources().getString(R.string.incorrect_sign_in);

            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        }

    }

    /**
     * @return the inserted email.
     */
    @NotNull
    private String getEmail() {
        return getTextFromInputLayout(this, R.id.login_input_text_box_email);
    }

    /**
     * @return The inserted password protected by MD5 hash.
     */
    @NotNull
    private String getHashedPassword() {

        String text = getTextFromInputLayout(this, R.id.login_input_text_box_password);

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
     * Return the intent result to the caller to bring the
     * parent activity to open the registration activity layout.
     */
    private void registerWithEmailTextClick() {

        Intent result = new Intent();

        result.putExtra(OPEN_REGISTRATION_ACTIVITY, true);
        setResult(REQUEST_CODE_LOGIN_ACTIVITY, result);

        finish();

    }

    static String getTextFromInputLayout(@NotNull Activity activity, @IdRes int textInputLayoutID) {

        TextInputLayout surnameInput = activity.findViewById(textInputLayoutID);
        assert surnameInput != null;

        EditText editText = surnameInput.getEditText();
        assert editText != null;

        return editText.getText().toString().trim();

    }

    /**
     * @param text the text that must be converted into md5.
     * @return The 32 chars md5 string of argument text.
     * @throws NoSuchAlgorithmException If MD5 digest doesn't be found.
     */
    @NotNull
    public static String getMD5(@NotNull String text) throws NoSuchAlgorithmException {

        MessageDigest md5Digest = MessageDigest.getInstance(MD5_DIGEST);
        byte[] digest = md5Digest.digest(text.getBytes());

        BigInteger bigInteger = new BigInteger(MD5_POSITIVE_SIGNUM_DIGEST, digest);
        StringBuilder result = new StringBuilder(bigInteger.toString(MD5_CHAR_LENGTH));

        while (result.length() < MD5_LENGTH) {
            result.insert(0, "0");
        }

        return result.toString();

    }

}
