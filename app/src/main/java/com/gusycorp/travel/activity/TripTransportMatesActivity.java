package com.gusycorp.travel.activity;

import android.os.Bundle;
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
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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
                //saveTransportMates();
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripTransportMateAdapter(TripTransportMatesActivity.this,
                R.layout.row_list_transport_mate_trip, new ArrayList<TripMate>());

        HashMap<String,String> itemHeader=new HashMap<String, String>();

        mAdapter.addSectionHeaderItem(itemHeader);

        ParseRelation<TripMate> tripMate = currentTrip.getRelation(Constants.TRIPMATE);

        tripMate.getQuery().findInBackground(new FindCallback<TripMate>() {
            @Override
            public void done(final List<TripMate> tripMateList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    tripMates.addAll(tripMateList);
                    final ParseRelation<TripMate> tripTransportMate = currentTripTransport.getRelation(Constants.TRIPMATE);
                    tripTransportMate.getQuery().findInBackground(new FindCallback<TripMate>() {
                        @Override
                        public void done(List<TripMate> tripTransportMateList, ParseException e) {
                            for (int i = 0; i < tripTransportMateList.size(); i++) {
                                for (int j = 0; j < tripMateList.size(); j++) {
                                    if(tripTransportMateList.get(i).getObjectId().equals(tripMateList.get(j).getObjectId())){
                                        tripMateList.get(j).setIsSelected(true);
                                        break;
                                    }
                                }
                            }
                            for (TripMate tripMate : tripMateList) {
                                mAdapter.addItem(tripMate);
                            }
                            listView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        });
    }

    private void addMateToTrip(String mate) {
        boolean existsMate = false;
        for(TripMate tripMate : tripMates){
            if(mate.equals(tripMate.getUsername())){
                existsMate = true;
            }
        }
        if(!existsMate){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(Constants.USERNAME, mate);
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> userList, ParseException e) {
                    if (e == null) {
                        TripMate tripMate = new TripMate();
                        tripMate.put(Constants.USERID,userList.get(0).getObjectId());
                        tripMate.put(Constants.USERNAME,userList.get(0).get(Constants.USERNAME));
                        tripMate.put(Constants.ORGANIZER, false);
                        try {
                            tripMate.save();
                            ParseRelation<TripMate> tripMateRelation = currentTrip.getRelation(Constants.TRIPMATE);
                            tripMateRelation.add(tripMate);
                            ParseRelation<ParseUser> tripUserRelation = currentTrip.getRelation(Constants.USER);
                            tripUserRelation.add(userList.get(0));
                            currentTrip.save();
                            tripMates.add(tripMate);
                            mAdapter.addItem(tripMate);
                            mAdapter.notifyDataSetChanged();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.user_appened_yet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }
}