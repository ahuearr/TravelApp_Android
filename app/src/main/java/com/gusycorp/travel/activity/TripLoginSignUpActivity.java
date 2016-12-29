package com.gusycorp.travel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.util.ConnectionDetector;
import java.util.Locale;

import io.cloudboost.CloudException;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

/**
 * Created by agustin.huerta on 25/08/2015.
 */
public class TripLoginSignUpActivity extends Activity implements View.OnClickListener {
    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mCreateAccountButton;

    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());


        mUserNameEditText = (EditText) findViewById(R.id.etUsername);
        mEmailEditText = (EditText) findViewById(R.id.etEmail);
        mPasswordEditText = (EditText) findViewById(R.id.etPassword);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.etPasswordConfirm);

        mCreateAccountButton = (Button) findViewById(R.id.btnCreateAccount);
        mCreateAccountButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAccount:
                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();
                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    createAccount();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(getApplicationContext(), getString(R.string.no_connection_1),
                            getString(R.string.no_connection_2), false);
                }


                break;

            default:
                break;
        }
    }

    private void createAccount(){
        clearErrors();

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        mEmail = mEmailEditText.getText().toString();
        mUsername = mUserNameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();

        // Check for a valid confirm password.
        if (TextUtils.isEmpty(mConfirmPassword)) {
            mConfirmPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mConfirmPasswordEditText;
            cancel = true;
        } else if (mPassword != null && !mConfirmPassword.equals(mPassword)) {
            mPasswordEditText.setError(getString(R.string.error_invalid_confirm_password));
            focusView = mPasswordEditText;
            cancel = true;
        }
        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailEditText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Toast.makeText(getApplicationContext(),getString(R.string.signUp), Toast.LENGTH_SHORT).show();
            new SignUp().execute(mUsername.toLowerCase(Locale.getDefault()), mEmail, mPassword);
            //signUp(mUsername.toLowerCase(Locale.getDefault()), mEmail, mPassword);

        }

    }

    private class SignUp extends AsyncTask <String, Void, Void> {

        private String mUsername;
        private String mEmail;
        private String mPassword;

        @Override
        protected Void doInBackground(String... params) {
            mUsername = params[0];
            mEmail = params[1];
            mPassword = params[2];
            CloudUser user = new CloudUser();
            user.setUserName(mUsername);
            user.setPassword(mPassword);
            user.setEmail(mEmail);

            try {
                user.signUp(new CloudUserCallback() {
                    @Override
                    public void done(CloudUser user, CloudException e) throws CloudException {
                        if (user != null) {
                            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(in);
                        }
                    }
                });
            } catch (CloudException e) {
                e.printStackTrace();
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                signUpMsg(getString(R.string.cuenta_existente));
            }
            return null;
        }

    }
    private void signUp(final String mUsername, String mEmail, String mPassword) {
        Toast.makeText(getApplicationContext(), mUsername + " - " + mEmail, Toast.LENGTH_SHORT).show();
        CloudUser user = new CloudUser();
        user.setUserName(mUsername);
        user.setPassword(mPassword);
        user.setEmail(mEmail);

        try {
            user.signUp(new CloudUserCallback() {
                @Override
                public void done(CloudUser user, CloudException e) throws CloudException {
                    if (user != null) {
                        signUpMsg(getString(R.string.cuenta_creada));
                        Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(in);
                    }
                }
            });
        } catch (CloudException e) {
            e.printStackTrace();
            // Sign up didn't succeed. Look at the ParseException
            // to figure out what went wrong
            signUpMsg(getString(R.string.cuenta_existente));
        }
    }

    protected void signUpMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void clearErrors(){
        mEmailEditText.setError(null);
        mUserNameEditText.setError(null);
        mPasswordEditText.setError(null);
        mConfirmPasswordEditText.setError(null);
    }

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed()
    {
        Intent in =  new Intent(TripLoginSignUpActivity.this,TripLoginActivity.class);
        startActivity(in);
        finish();
    }

}