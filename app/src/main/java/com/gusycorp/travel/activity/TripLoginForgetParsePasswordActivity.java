package com.gusycorp.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gusycorp.travel.R;

import io.cloudboost.CloudException;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.CloudUser;

/**
 * Created by agustin.huerta on 25/08/2015.
 */
public class TripLoginForgetParsePasswordActivity extends Activity {
    EditText et_forgetpassword = null;
    Button btn_submitforgetpassword = null;
    String password = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget_password);

        et_forgetpassword = (EditText) findViewById(R.id.et_forgetpassword);
        btn_submitforgetpassword = (Button) findViewById(R.id.btn_submitforgetpassword);

        btn_submitforgetpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                password = et_forgetpassword.getText().toString();
                checkEmailID();
            }
        });

    }

    protected void checkEmailID() {
        if (TextUtils.isEmpty(password)) {
            et_forgetpassword.setError(getString(R.string.error_field_required));
        } else if (!password.contains("@")) {
            et_forgetpassword.setError(getString(R.string.error_invalid_email));
        }
        else
            forgotPassword(password);
    }

    public void forgotPassword(String email) {
        try {
            CloudUser.resetPassword("email", new CloudStringCallback() {
                @Override
                public void done(String msg, CloudException e) {
                    if (msg != null) {
                        //reset password email sent
                        Toast.makeText(getApplicationContext(), getString(R.string.link_password_ok), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (CloudException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.link_password_ko), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent in =  new Intent(TripLoginForgetParsePasswordActivity.this,TripLoginActivity.class);
        startActivity(in);
        finish();
    }

}