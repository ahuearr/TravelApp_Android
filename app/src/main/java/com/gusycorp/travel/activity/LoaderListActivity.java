package com.gusycorp.travel.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;

public class LoaderListActivity extends ListActivity {

    protected AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showLoader(){
        avi.show();
    }

    protected void hideLoader(){
        avi.hide();
    }

}