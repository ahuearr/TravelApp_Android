package com.gusycorp.travel.activity.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.HomeActivity;
import com.gusycorp.travel.util.ConnectionDetector;
import com.gusycorp.travel.util.Utils;

import java.util.Locale;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

/**
 * Created by agustin.huerta on 25/08/2015.
 */
public class TripLoginActivity extends Activity {
    Button btn_LoginIn = null;
    Button btn_SignUp = null;
    Button btn_ForgetPass = null;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing Cloudboost
        initCloudBoost();

        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());

        btn_LoginIn = (Button) findViewById(R.id.btn_login);
        btn_SignUp = (Button) findViewById(R.id.btn_signup);
        btn_ForgetPass = (Button) findViewById(R.id.btn_ForgetPass);
        mUserNameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);


        btn_LoginIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();
                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    attemptLogin();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(TripLoginActivity.this, getString(R.string.no_connection_1),
                            getString(R.string.no_connection_2), false);
                }

            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in =  new Intent(TripLoginActivity.this,TripLoginSignUpActivity.class);
                startActivity(in);
                finish();
            }
        });

        btn_ForgetPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in =  new Intent(TripLoginActivity.this,TripLoginForgetPasswordActivity.class);
                startActivity(in);
                finish();
            }
        });



    }

    public void initCloudBoost() {
        CloudApp.init(Utils.APP_ID, Utils.CLIENT_KEY);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_forgot_password:
                forgotPassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void forgotPassword(){
		/*
		FragmentManager fm = getSupportFragmentManager();
	     ForgotPasswordDialogFragment forgotPasswordDialog = new ForgotPasswordDialogFragment();
	     forgotPasswordDialog.show(fm, null);
		 */
    }

    public void attemptLogin() {

        clearErrors();

        // Store values at the time of the login attempt.
        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email.
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        } else if (password.length() < 4) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView =mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUserNameEditText.setError(getString(R.string.error_field_required));
            focusView = mUserNameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            new Login().execute(username.toLowerCase(Locale.getDefault()), password);
        }
    }

    protected void loginSuccessful(CloudUser user) {
        Intent in =  new Intent(TripLoginActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }
    protected void loginUnSuccessful() {
        // TODO Auto-generated method stub
        showAlertDialog(TripLoginActivity.this,getString(R.string.login), getString(R.string.login_password_incorrect), false);
    }

    private void clearErrors(){
        mUserNameEditText.setError(null);
        mPasswordEditText.setError(null);
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

    private class Login extends AsyncTask<String, Void, Integer> {

        private String username;
        private String password;


        @Override
        protected Integer doInBackground(String... params) {
            username=params[0];
            password=params[1];

            CloudUser user = new CloudUser();
            user.setUserName(username);
            user.setPassword(password);
            try {
                user.logIn(new CloudUserCallback() {
                    @Override
                    public void done(CloudUser user, CloudException e) throws CloudException {
                        if (user != null) {
                            loginSuccessful(user);
                        }
                    }
                });
            } catch (CloudException e) {
                return 1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result==1){
                loginUnSuccessful();
            }
        }
    }



}