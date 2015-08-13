package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripAccommodationAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripAccommodationListActivity extends MenuActivity implements View.OnClickListener{

    private Button addAccommodationTrip;
    private TextView tripNameText;
    private ListView listView;

    private ListTripAccommodationAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_trip_list);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        app.setCurrentTripAccommodation(new TripAccommodation());

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        addAccommodationTrip = (Button) findViewById(R.id.add_accommodation_trip);
        addAccommodationTrip.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        tripName = extras.getString("tripName");
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.accommodation_list);

    }

    @Override
    public void onResume() {
        super.onResume();
        getTripAccommodations(currentTrip.getObjectId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_accommodation_trip:
                Intent intent = new Intent(TripAccommodationListActivity.this, TripAccommodationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getTripAccommodations(String tripObjectId) {

        mAdapter = new ListTripAccommodationAdapter(TripAccommodationListActivity.this,
                R.layout.row_list_accommodation_trip, new ArrayList<TripAccommodation>());

        HashMap<String,String> itemHeader=new HashMap<String, String>();
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_ONE, Constants.TRIPACCOMMODATIONLIST_COLUMN_ONE);
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_TWO, Constants.TRIPACCOMMODATIONLIST_COLUMN_TWO);
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_THREE, Constants.TRIPACCOMMODATIONLIST_COLUMN_THREE);
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_FOUR, Constants.TRIPACCOMMODATIONLIST_COLUMN_FOUR);
        mAdapter.addSectionHeaderItem(itemHeader);

        ParseRelation<TripAccommodation> tripAccommodation = currentTrip.getRelation(Constants.TRIP_TRIPACCOMMODATION);

        tripAccommodation.getQuery().findInBackground(new FindCallback<TripAccommodation>() {
            public void done(List<TripAccommodation> tripAccommodationList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    for (TripAccommodation tripAccommodation : tripAccommodationList) {
                        mAdapter.addItem(tripAccommodation);
                    }
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TripAccommodation tripAccommodation = (TripAccommodation) listView.getAdapter().getItem(position);
                            if (tripAccommodation != null) {
                                app.setCurrentTripAccommodation(tripAccommodation);
                                Intent intent = new Intent(TripAccommodationListActivity.this, TripAccommodationActivity.class);
                                intent.putExtra(Constants.OBJECTID, tripAccommodation.getObjectId());
                                intent.putExtra(Constants.TRIPACCOMMODATION_PLACE, tripAccommodation.getPlace());
                                intent.putExtra(Constants.TRIPACCOMMODATION_CITY, tripAccommodation.getCity());
                                intent.putExtra(Constants.TRIPTRANSPORT_DATEFROM, tripAccommodation.getDateFrom());
                                intent.putExtra(Constants.TRIPTRANSPORT_DATETO, tripAccommodation.getDateTo());
                                intent.putExtra(Constants.TRIPACCOMMODATION_ADDRESS, tripAccommodation.getAddress());
                                intent.putExtra(Constants.TRIPACCOMMODATION_NUMROOMS, tripAccommodation.getNumRooms());
                                intent.putExtra(Constants.TRIPTRANSPORT_PRIZE, tripAccommodation.getPrize());
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }
}