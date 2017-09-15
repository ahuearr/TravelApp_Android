package com.gusycorp.travel.activity.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.LoaderActivity;
import com.wang.avi.AVLoadingIndicatorView;

import io.cloudboost.CloudException;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

/**
 * Created by agustin.huerta on 25/08/2015.
 */
public class TripLoginForgetPasswordActivity extends LoaderActivity {
    EditText et_forgetpassword = null;
    Button btn_submitforgetpassword = null;
    String email = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget_password);

        et_forgetpassword = (EditText) findViewById(R.id.et_forgetpassword);
        btn_submitforgetpassword = (Button) findViewById(R.id.btn_submitforgetpassword);

        avi= (AVLoadingIndicatorView) findViewById(R.id.loader);

        btn_submitforgetpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = et_forgetpassword.getText().toString();
                checkEmailID();
            }
        });

    }

    protected void checkEmailID() {
        if (TextUtils.isEmpty(email)) {
            et_forgetpassword.setError(getString(R.string.error_field_required));
        } else if (!email.contains("@")) {
            et_forgetpassword.setError(getString(R.string.error_invalid_email));
        }
        else
            showLoader();
            new ForgotPassword().execute(email);
    }

    @Override
    public void onBackPressed()
    {
        Intent in =  new Intent(TripLoginForgetPasswordActivity.this,TripLoginActivity.class);
        startActivity(in);
        finish();
    }

    private class ForgotPassword extends AsyncTask<String, Void, Integer> {

        private String email;

        @Override
        protected Integer doInBackground(String... params) {
            email=params[0];

            try {
                CloudUser.resetPassword("email", new CloudStringCallback() {
                    @Override
                    public void done(String msg, CloudException e) {
                        if (msg != null) {
                            //reset email email sent
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
            hideLoader();
            if(result==0){
                Toast.makeText(getApplicationContext(), getString(R.string.link_password_ok), Toast.LENGTH_LONG).show();
            }else if(result==1){
                Toast.makeText(getApplicationContext(), getString(R.string.link_password_ko), Toast.LENGTH_LONG).show();
            }
        }
    }
}