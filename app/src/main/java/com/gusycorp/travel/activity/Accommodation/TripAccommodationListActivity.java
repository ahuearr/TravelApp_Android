package com.gusycorp.travel.activity.Accommodation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.adapter.ListTripAccommodationAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.util.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
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

        avi= (AVLoadingIndicatorView) findViewById(R.id.loader);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        app.setCurrentTripAccommodation(new TripAccommodation());

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        addAccommodationTrip = (Button) findViewById(R.id.add_accommodation_trip);
        if(!app.isOrganizer()){
            addAccommodationTrip.setVisibility(View.GONE);
        }

        addAccommodationTrip.setOnClickListener(this);

        tripName = currentTrip.getTripName();
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.accommodation_list);

    }

    @Override
    public void onResume() {
        super.onResume();
        getTripAccommodations(currentTrip.getId());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_ONE, getString(R.string.hotel));
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_TWO, getString(R.string.city));
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_THREE, getString(R.string.checkin));
        itemHeader.put(Constants.TRIPACCOMMODATIONLIST_COLUMN_FOUR, getString(R.string.checkout));
        mAdapter.addSectionHeaderItem(itemHeader);

        List<TripAccommodation> tripAccommodationList = currentTrip.getTripAccommodationList();

        for (TripAccommodation tripAccommodation : tripAccommodationList) {
            mAdapter.addItem(tripAccommodation);
        }
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    TripAccommodation tripAccommodation = (TripAccommodation) listView.getAdapter().getItem(position);
                    if (tripAccommodation != null) {
                        app.setCurrentTripAccommodation(tripAccommodation);
                        Intent intent = new Intent(TripAccommodationListActivity.this, TripAccommodationActivity.class);
                        intent.putExtra(Constants.OBJECTID, tripAccommodation.getId());
                        intent.putExtra(Constants.PLACE, tripAccommodation.getPlace());
                        intent.putExtra(Constants.CITY, tripAccommodation.getCity());
                        intent.putExtra(Constants.DATEFROM, tripAccommodation.getDateFrom());
                        intent.putExtra(Constants.DATETO, tripAccommodation.getDateTo());
                        intent.putExtra(Constants.ADDRESS, tripAccommodation.getAddress());
                        intent.putExtra(Constants.NUMROOMS, tripAccommodation.getNumRooms());
                        intent.putExtra(Constants.PRIZE, tripAccommodation.getPrize());
                        startActivity(intent);
                    }
                }catch(ParseException e){
                    e.printStackTrace();
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