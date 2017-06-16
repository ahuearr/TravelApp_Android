package com.gusycorp.travel.activity.Accommodation;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.activity.Transport.TripTransportMatesActivity;
import com.gusycorp.travel.adapter.ListTripActivitiesMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.util.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;

public class TripAccommodationMatesActivity extends Activity implements View.OnClickListener{

    private Button save;
    private Button share;
    private TextView activityNameText;
    private TextView sharePrize;
    private ListView listView;

    private ListTripActivitiesMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private TripAccommodation currentTripAcommodation = new TripAccommodation();
    private List<String> tripMates;
    ArrayList<String> tripMatefromTripMatePrize = new ArrayList<String>();

    private Double prize = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_mates_trip);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        currentTripAcommodation = app.getCurrentTripAccommodation();

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

        String tripAccommodationName = currentTripAcommodation.getPlace() + " - " + currentTripAcommodation.getCity();
        activityNameText.setText(tripAccommodationName);
        prize = currentTripAcommodation.getPrize()==null ? 0 : currentTripAcommodation.getPrize();
        sharePrize.setText(Double.toString(prize));
        tripMates = new ArrayList<String>();
        listView=(ListView)findViewById(R.id.mates_list);

    }

    @Override
    public void onResume() {
        super.onResume();
        getTripMates(currentTrip.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                new Save().execute();
                break;
            case R.id.share_button:
                sharePrize();
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripActivitiesMateAdapter(TripAccommodationMatesActivity.this,
                R.layout.row_list_activities_mate_trip, new ArrayList<TripMatePrize>(), app.isOrganizer());

        HashMap<String,String> itemHeader=new HashMap<String, String>();

        mAdapter.addSectionHeaderItem(itemHeader);

        ArrayList<TripMate> tripMateList = currentTrip.getTripMateList();
        for(TripMate tripMate : tripMateList){
            tripMates.add(tripMate.getId());
        }

        final ArrayList<TripMatePrize> tripAccommodationMateList = currentTrip.getTripMatePrize(Constants.PARENTTYPE_ACCOMODATION, currentTripAcommodation.getId());

        for(TripMatePrize tripMatePrize : tripAccommodationMateList){
            mAdapter.addItem(tripMatePrize);
            tripMatefromTripMatePrize.add(tripMatePrize.getTripMateId());
        }

        for(TripMate tripMate : tripMateList){
            if(!tripMatefromTripMatePrize.contains(tripMate.getId())){
                TripMatePrize tripMatePrize = new TripMatePrize();
                try {
                    tripMatePrize.setPrize(BigDecimal.ZERO.doubleValue());
                    tripMatePrize.setTripMateId(tripMate.getId());
                    tripMatePrize.setParentId(currentTripAcommodation.getId());
                    tripMatePrize.setParentType(Constants.PARENTTYPE_ACCOMODATION);
                    tripMatePrize.setTripId(currentTrip.getId());
                    tripMatePrize.setMateUsername(tripMate.getUsername());
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException t) throws CloudException {
                            TripMatePrize tripMatePrize = new TripMatePrize(tripMatePrizeSaved);
                            tripAccommodationMateList.add(tripMatePrize);
                        }
                    });
                } catch (CloudException e) {
                    e.printStackTrace();
                }
                mAdapter.addItem(tripMatePrize);
            }
        }
        listView.setAdapter(mAdapter);
    }

    private class Save extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            return saveAccommodationMates();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                currentTrip.setTripMatePrizeList(mAdapter.getTripMateList());
                onBackPressed();
            }else{
                Toast.makeText(TripAccommodationMatesActivity.this, getString(R.string.message_ko), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean saveAccommodationMates() {

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