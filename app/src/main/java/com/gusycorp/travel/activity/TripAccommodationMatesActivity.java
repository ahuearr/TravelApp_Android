package com.gusycorp.travel.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripActivitiesMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripAccommodationMatesActivity extends MenuActivity implements View.OnClickListener{

    private Button save;
    private Button share;
    private TextView activityNameText;
    private TextView sharePrize;
    private ListView listView;

    private ListTripActivitiesMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private TripAccommodation currentTripAcommodation = new TripAccommodation();
    private List<TripMate> tripMates;
    List<TripMate> tripMatefromTripMatePrize = new ArrayList<TripMate>();

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
        tripMates = new ArrayList<TripMate>();
        listView=(ListView)findViewById(R.id.mates_list);

    }

    @Override
    public void onResume() {
        super.onResume();
        getTripMates(currentTrip.getObjectId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                saveAccommodationMates();
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

        ParseRelation<TripMate> tripMateRelation = currentTrip.getRelation(Constants.TRIPMATE);

        tripMateRelation.getQuery().findInBackground(new FindCallback<TripMate>() {
            @Override
            public void done(final List<TripMate> tripMateList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    tripMates.addAll(tripMateList);
                    final ParseRelation<TripMatePrize> tripAccommodationMate = currentTripAcommodation.getRelation(Constants.TRIPMATEPRIZE);
                    tripAccommodationMate.getQuery().findInBackground(new FindCallback<TripMatePrize>() {
                        @Override
                        public void done(List<TripMatePrize> tripAccommodationMateList, ParseException e) {

                            for(TripMatePrize tripMatePrize : tripAccommodationMateList){
                                mAdapter.addItem(tripMatePrize);
                                tripMatefromTripMatePrize.add(tripMatePrize.getTripMate());
                            }

                            for(TripMate tripMate : tripMateList){
                                if(!tripMatefromTripMatePrize.contains(tripMate)){
                                    TripMatePrize tripMatePrize = new TripMatePrize();
                                    tripMatePrize.put(Constants.TRIPMATE, tripMate);
                                    tripMatePrize.put(Constants.PRIZE, BigDecimal.ZERO);
                                    try {
                                        tripMatePrize.save();
                                        tripAccommodationMate.add(tripMatePrize);
                                        currentTripAcommodation.save();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    mAdapter.addItem(tripMatePrize);
                                }
                            }
                            listView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        });
    }

    private void saveAccommodationMates() {
        try {
            final ParseRelation<TripMatePrize> tripAccommodationMate = currentTripAcommodation.getRelation(Constants.TRIPMATEPRIZE);
            for(TripMatePrize tripMatePrize : mAdapter.getTripMateList()){
                if(tripMatePrize!=null){
                    tripMatePrize.save();
                    tripAccommodationMate.add(tripMatePrize);
                }
            }
            currentTripAcommodation.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void sharePrize() {
        int matesNumber = mAdapter.getTripMateList().size()-1;
        Double sharedPrize = Math.floor(prize * 100) / 100;
        if(matesNumber>0){
            sharedPrize = Math.floor((prize/matesNumber)*100)/100;
        }
        for(int i=0;i<mAdapter.getTripMateList().size();i++){
            if (mAdapter.getTripMateList().get(i) != null) {
                mAdapter.getTripMateList().get(i).put(Constants.PRIZE, sharedPrize);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }
}