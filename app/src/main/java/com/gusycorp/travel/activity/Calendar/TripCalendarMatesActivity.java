package com.gusycorp.travel.activity.Calendar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.LoaderActivity;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.activity.Transport.TripTransportMatesActivity;
import com.gusycorp.travel.adapter.ListTripActivitiesMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.util.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;

public class TripCalendarMatesActivity extends LoaderActivity implements View.OnClickListener{

    private Button save;
    private Button share;
    private TextView activityNameText;
    private TextView sharePrize;
    private ListView listView;

    private ListTripActivitiesMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private TripCalendar currentTripCalendar = new TripCalendar();
    private ArrayList<String> tripMates;
    ArrayList<String> tripMatefromTripMatePrize = new ArrayList<String>();

    private Double prize = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_mates_trip);

        avi= (AVLoadingIndicatorView) findViewById(R.id.loader);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        currentTripCalendar = app.getCurrentTripCalendar();

        activityNameText = (TextView) findViewById(R.id.text_name);
        sharePrize = (TextView) findViewById(R.id.text_prize);
        save = (Button) findViewById(R.id.save_button);
        share = (Button) findViewById(R.id.share_button);

        if(!app.isOrganizer()) {
            save.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
        }

        save.setOnClickListener(this);
        share.setOnClickListener(this);

        String tripCalendarName = currentTripCalendar.getActivity();
        activityNameText.setText(tripCalendarName);
        prize = currentTripCalendar.getPrize()==null ? 0 : currentTripCalendar.getPrize();
        sharePrize.setText(Double.toString(prize));
        tripMates = new ArrayList<String>();
        listView=(ListView)findViewById(R.id.mates_list);

    }

    @Override
    public void onResume() {
        super.onResume();
        showLoader();
        getTripMates(currentTrip.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                showLoader();
                new Save().execute();
                break;
            case R.id.share_button:
                sharePrize();
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripActivitiesMateAdapter(TripCalendarMatesActivity.this,
                R.layout.row_list_activities_mate_trip, new ArrayList<TripMatePrize>(), app.isOrganizer());

        HashMap<String,String> itemHeader=new HashMap<String, String>();

        mAdapter.addSectionHeaderItem(itemHeader);

        ArrayList<TripMate> tripMateList = currentTrip.getTripMateList();
        for(TripMate tripMate : tripMateList){
            tripMates.add(tripMate.getId());
        }

        final ArrayList<TripMatePrize> tripCalendarMateList = currentTrip.getTripMatePrize(Constants.PARENTTYPE_CALENDAR, currentTripCalendar.getId());

        for(TripMatePrize tripMatePrize : tripCalendarMateList){
            mAdapter.addItem(tripMatePrize);
            tripMatefromTripMatePrize.add(tripMatePrize.getTripMateId());
        }

        for(TripMate tripMate : tripMateList){
            if(!tripMatefromTripMatePrize.contains(tripMate.getId())){
                TripMatePrize tripMatePrize = new TripMatePrize();
                try {
                    tripMatePrize.setPrize(BigDecimal.ZERO.doubleValue());
                    tripMatePrize.setTripMateId(tripMate.getId());
                    tripMatePrize.setParentId(currentTripCalendar.getId());
                    tripMatePrize.setParentType(Constants.PARENTTYPE_CALENDAR);
                    tripMatePrize.setTripId(currentTrip.getId());
                    tripMatePrize.setMateUsername(tripMate.getUsername());
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException t) throws CloudException {
                            TripMatePrize tripMatePrize = new TripMatePrize(tripMatePrizeSaved);
                            tripCalendarMateList.add(tripMatePrize);
                        }
                    });
                } catch (CloudException e1) {
                    e1.printStackTrace();
                }
                mAdapter.addItem(tripMatePrize);
            }
        }
        listView.setAdapter(mAdapter);
        hideLoader();
    }

    private class Save extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            return saveCalendarMates();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                currentTrip.setTripMatePrizeList(mAdapter.getTripMateList());
                onBackPressed();
            }else{
                Toast.makeText(TripCalendarMatesActivity.this, getString(R.string.message_ko), Toast.LENGTH_LONG).show();
            }
            hideLoader();
        }
    }

    private boolean saveCalendarMates() {

        final boolean[] success = {false};
        try {

            for(final TripMatePrize tripMatePrize : mAdapter.getTripMateList()){
                if(tripMatePrize!=null){
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException e) throws CloudException {
                            if(tripMatePrizeSaved!=null){
                                success[0] =true;
                            }else{
                                success[0] =false;
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (CloudException e) {
            e.printStackTrace();
        }
        return success[0];
    }

    private void sharePrize() {
        int matesNumber = mAdapter.getTripMateList().size()-1;
        Double sharedPrize = Math.floor(prize * 100) / 100;
        if(matesNumber>0){
            sharedPrize = Math.floor((prize/matesNumber)*100)/100;
        }
        for(int i=0;i<mAdapter.getTripMateList().size();i++){
            if (mAdapter.getTripMateList().get(i) != null) {
                try {
                    mAdapter.getTripMateList().get(i).setPrize(sharedPrize);
                } catch (CloudException e) {
                    e.printStackTrace();
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}