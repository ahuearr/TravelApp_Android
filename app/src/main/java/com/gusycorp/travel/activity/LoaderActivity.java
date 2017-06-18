package com.gusycorp.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.Accommodation.TripAccommodationListActivity;
import com.gusycorp.travel.activity.Calendar.TripCalendarListActivity;
import com.gusycorp.travel.activity.Map.TripMapActivity;
import com.gusycorp.travel.activity.Mates.TripMatesActivity;
import com.gusycorp.travel.activity.Transport.TripTransportListActivity;
import com.gusycorp.travel.application.TravelApplication;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Arrays;
import java.util.List;

public class LoaderActivity extends Activity{

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