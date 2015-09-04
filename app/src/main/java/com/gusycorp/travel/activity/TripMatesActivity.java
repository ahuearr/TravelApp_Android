package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripAccommodationAdapter;
import com.gusycorp.travel.adapter.ListTripMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripMatesActivity extends MenuActivity implements View.OnClickListener{

    private Button addMateTrip;
    private TextView tripNameText;
    private EditText mateText;
    private ListView listView;

    private ListTripMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private List<TripMate> tripMates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mates_trip);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        mateText = (EditText) findViewById(R.id.find_username);
        addMateTrip = (Button) findViewById(R.id.add_mate_trip);

        if(!app.isOrganizer()){
            mateText.setVisibility(View.GONE);
            addMateTrip.setVisibility(View.GONE);
        }

        addMateTrip.setOnClickListener(this);

        tripName = currentTrip.getTripName();
        tripNameText.setText(tripName);
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
            case R.id.add_mate_trip:
                String mate = mateText.getText().toString();
                if(!"".equals(mate)){
                    addMateToTrip(mate);
                } else {
                    Toast.makeText(this, getString(R.string.user_not_exists), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripMateAdapter(TripMatesActivity.this,
                R.layout.row_list_mate_trip, new ArrayList<TripMate>());

        HashMap<String,String> itemHeader=new HashMap<String, String>();
        itemHeader.put(getString(R.string.username), getString(R.string.username));
        itemHeader.put(getString(R.string.rol), getString(R.string.rol));
        mAdapter.addSectionHeaderItem(itemHeader);

        ParseRelation<TripMate> tripMate = currentTrip.getRelation(Constants.TRIPMATE);

        tripMate.getQuery().findInBackground(new FindCallback<TripMate>() {
            public void done(List<TripMate> tripMateList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    tripMates.addAll(tripMateList);
                    for (TripMate tripMate : tripMateList) {
                        mAdapter.addItem(tripMate);
                    }
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //TODO Delete Mate
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