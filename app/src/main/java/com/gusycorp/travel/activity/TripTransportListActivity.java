package com.gusycorp.travel.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripAdapter;
import com.gusycorp.travel.adapter.ListTripTransportAdapter;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripTransportListActivity extends MenuActivity {

    private static final String TAG = Constants.TAG_TRIPTRANSPORTMODEL;

    private TextView tripNameText;
    private ListView listView;

    private ArrayList<HashMap<String, String>> list;

    private ListTripTransportAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);

        tripNameText = (TextView) findViewById(R.id.text_trip_name);

        Bundle extras = getIntent().getExtras();
        tripObjectId = extras.getString("tripObjectId");
        tripName = extras.getString("tripName");
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.transport_list);

        getTripTransports(tripObjectId);
    }

    private void getTripTransports(String tripObjectId) {

        mAdapter = new ListTripTransportAdapter(TripTransportListActivity.this,
                R.layout.row_list_transport_trip, new ArrayList<HashMap<String,String>>());

        HashMap<String,String> itemHeader=new HashMap<String, String>();
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_ONE, Constants.TRIPTRANSPORTLIST_COLUMN_ONE);
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_TWO, Constants.TRIPTRANSPORTLIST_COLUMN_TWO);
        itemHeader.put(Constants.TRIPTRANSPORTLIST_COLUMN_THREE, Constants.TRIPTRANSPORTLIST_COLUMN_THREE);
        mAdapter.addSectionHeaderItem(itemHeader);

        HashMap<String, Object> filter = new HashMap();
        filter.put(Constants.TRIPTRANSPORT_OBJECTIDTRIP, tripObjectId);

        list=new ArrayList<HashMap<String,String>>();

        TripTransport.findTripTransportListByFieldsInBackground(filter, new FindCallback<TripTransport>() {
            public void done(List<TripTransport> tripTransportList, ParseException e) {
                for (TripTransport tripTransport : tripTransportList) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put(Constants.TRIPTRANSPORTLIST_COLUMN_ONE, tripTransport.getDateFrom());
                    item.put(Constants.TRIPTRANSPORTLIST_COLUMN_TWO, tripTransport.getFrom());
                    item.put(Constants.TRIPTRANSPORTLIST_COLUMN_THREE, tripTransport.getTo());
                    mAdapter.addItem(item);
                }
                listView.setAdapter(mAdapter);
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