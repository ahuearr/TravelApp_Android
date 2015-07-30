package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripTransportAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripTransportListActivity extends MenuActivity implements View.OnClickListener{

    private Button addTransportTrip;
    private TextView tripNameText;
    private ListView listView;

    private ListTripTransportAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_trip_list);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        addTransportTrip = (Button) findViewById(R.id.add_transport_trip);
        addTransportTrip.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        tripName = extras.getString("tripName");
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.transport_list);

        getTripTransports(currentTrip.getObjectId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_transport_trip:
                Intent intent = new Intent(TripTransportListActivity.this, TripTransportActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getTripTransports(String tripObjectId) {

        mAdapter = new ListTripTransportAdapter(TripTransportListActivity.this,
                R.layout.row_list_transport_trip, new ArrayList<TripTransport>());

        HashMap<String,String> itemHeader=new HashMap<String, String>();
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_ONE, Constants.TRIPTRANSPORTLIST_COLUMN_ONE);
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_TWO, Constants.TRIPTRANSPORTLIST_COLUMN_TWO);
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_THREE, Constants.TRIPTRANSPORTLIST_COLUMN_THREE);
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_FOUR, Constants.TRIPTRANSPORTLIST_COLUMN_FOUR);
        mAdapter.addSectionHeaderItem(itemHeader);

        ParseRelation<TripTransport> tripTransport = currentTrip.getRelation(Constants.TRIPTRANSPORT_TRIPTRANSPORT);

        tripTransport.getQuery().findInBackground(new FindCallback<TripTransport>() {
            public void done(List<TripTransport> tripTransportList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    for (TripTransport tripTransport : tripTransportList) {
                        mAdapter.addItem(tripTransport);
                    }
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TripTransport tripTransport = (TripTransport) listView.getAdapter().getItem(position);
                            if(tripTransport!=null){
                                Intent intent = new Intent(TripTransportListActivity.this, TripTransportActivity.class);
                                intent.putExtra(Constants.OBJECTID, tripTransport.getObjectId());
                                intent.putExtra(Constants.TRIPTRANSPORT_DATEFROM, tripTransport.getDateFrom());
                                intent.putExtra(Constants.TRIPTRANSPORT_DATETO, tripTransport.getDateTo());
                                intent.putExtra(Constants.TRIPTRANSPORT_FROM, tripTransport.getFrom());
                                intent.putExtra(Constants.TRIPTRANSPORT_TO, tripTransport.getTo());
                                intent.putExtra(Constants.TRIPTRANSPORT_PRIZE, tripTransport.getPrize());
                                intent.putExtra(Constants.TRIPTRANSPORT_LOCATOR, tripTransport.getLocator());
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