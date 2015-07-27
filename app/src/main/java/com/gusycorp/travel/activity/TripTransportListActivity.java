package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripTransportAdapter;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripTransportListActivity extends MenuActivity implements View.OnClickListener{

    private static final String TAG = Constants.TAG_TRIPTRANSPORTLISTACTIVITY;

    private TextView tripNameText;
    private ListView listView;

    private ListTripTransportAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_trip_list);

        tripNameText = (TextView) findViewById(R.id.text_trip_name);

        Bundle extras = getIntent().getExtras();
        tripObjectId = extras.getString("tripObjectId");
        tripName = extras.getString("tripName");
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.transport_list);

        getTripTransports(tripObjectId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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

        HashMap<String, Object> filter = new HashMap();
        filter.put(Constants.TRIPTRANSPORT_OBJECTIDTRIP, tripObjectId);

        TripTransport.findTripTransportListByFieldsInBackground(filter, new FindCallback<TripTransport>() {
            public void done(List<TripTransport> tripTransportList, ParseException e) {
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
                            intent.putExtra("objectId", tripTransport.getObjectId());
                            intent.putExtra("objectIdTrip", tripTransport.getObjectIdTrip());
                            intent.putExtra("dateFrom", tripTransport.getDateFrom());
                            intent.putExtra("dateTo", tripTransport.getDateTo());
                            intent.putExtra("from", tripTransport.getFrom());
                            intent.putExtra("to", tripTransport.getTo());
                            intent.putExtra("prize", tripTransport.getPrize());
                            intent.putExtra("locator", tripTransport.getLocator());
                            startActivity(intent);
                        }
                    }
                });
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