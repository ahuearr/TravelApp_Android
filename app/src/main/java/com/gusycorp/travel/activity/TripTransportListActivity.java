package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;

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
        app.setCurrentTripTransport(new TripTransport());

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        addTransportTrip = (Button) findViewById(R.id.add_transport_trip);
        if(!app.isOrganizer()){
            addTransportTrip.setVisibility(View.GONE);
        }

        addTransportTrip.setOnClickListener(this);

        tripName = currentTrip.getTripName();
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.transport_list);

    }

    @Override
    public void onResume() {
        super.onResume();
        getTripTransports(currentTrip.getId());
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
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_ONE, getString(R.string.date));
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_TWO, getString(R.string.from));
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_THREE, getString(R.string.to));
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_FOUR, getString(R.string.mean_of_transport));
        mAdapter.addSectionHeaderItem(itemHeader);

        List<TripTransport> tripTransportList = currentTrip.getTripTransportList();

        for (TripTransport tripTransport : tripTransportList) {
            mAdapter.addItem(tripTransport);
        }
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TripTransport tripTransport = (TripTransport) listView.getAdapter().getItem(position);
                if (tripTransport != null) {
                    app.setCurrentTripTransport(tripTransport);
                    Intent intent = new Intent(TripTransportListActivity.this, TripTransportActivity.class);
                    intent.putExtra(Constants.OBJECTID, tripTransport.getId());
                    intent.putExtra(Constants.DATEFROM, tripTransport.getDateFrom());
                    intent.putExtra(Constants.DATETO, tripTransport.getDateTo());
                    intent.putExtra(Constants.FROM, tripTransport.getFrom());
                    intent.putExtra(Constants.TO, tripTransport.getTo());
                    intent.putExtra(Constants.PRIZE, tripTransport.getPrize());
                    intent.putExtra(Constants.LOCATOR, tripTransport.getLocator());
                    TypeTransport typeTransport = tripTransport.getTypeTransport();
                    intent.putExtra(Constants.TYPETRANSPORT, typeTransport.getTransportName());
                    startActivity(intent);
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