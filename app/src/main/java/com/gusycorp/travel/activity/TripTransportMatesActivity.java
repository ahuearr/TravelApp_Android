package com.gusycorp.travel.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripMateAdapter;
import com.gusycorp.travel.adapter.ListTripTransportMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripTransportMatesActivity extends MenuActivity implements View.OnClickListener{

    private Button save;
    private TextView transportNameText;
    private ListView listView;

    private ListTripTransportMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private TripTransport currentTripTransport = new TripTransport();
    private List<TripMate> tripMates;
    List<TripMate> tripMatefromTripMatePrize = new ArrayList<TripMate>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_mates_trip);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        currentTripTransport = app.getCurrentTripTransport();

        transportNameText = (TextView) findViewById(R.id.text_transport_name);
        save = (Button) findViewById(R.id.save_button);

        save.setOnClickListener(this);

        String tripTransportName = currentTripTransport.getFrom() + " - " + currentTripTransport.getTo();
        transportNameText.setText(tripTransportName);
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
                saveTransportMates();
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripTransportMateAdapter(TripTransportMatesActivity.this,
                R.layout.row_list_transport_mate_trip, new ArrayList<TripMatePrize>());

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
                    final ParseRelation<TripMatePrize> tripTransportMate = currentTripTransport.getRelation(Constants.TRIPMATEPRIZE);
                    tripTransportMate.getQuery().findInBackground(new FindCallback<TripMatePrize>() {
                        @Override
                        public void done(List<TripMatePrize> tripTransportMateList, ParseException e) {

                            for(TripMatePrize tripMatePrize : tripTransportMateList){
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
                                        tripTransportMate.add(tripMatePrize);
                                        currentTripTransport.save();
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

    private void saveTransportMates() {
        try {
            final ParseRelation<TripMatePrize> tripTransportMate = currentTripTransport.getRelation(Constants.TRIPMATEPRIZE);
            for(TripMatePrize tripMatePrize : mAdapter.getTripMateList()){
                if(tripMatePrize!=null){
                    tripTransportMate.add(tripMatePrize);
                }
            }
            currentTripTransport.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

/*
        for(TripMatePrize tripMatePrize : mAdapter.getTripMateList()){
            if(tripMatePrize!=null){
                    tripTransportMate.add(tripMatePrize);
            }
        }
        try {
            currentTripTransport.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }
}