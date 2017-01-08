package com.gusycorp.travel.activity.Mates;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.adapter.ListTripMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;

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
        getTripMates(currentTrip.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_mate_trip:
                String mate = mateText.getText().toString();
                if(!"".equals(mate)){
                    try {
                        addMateToTrip(mate);
                    } catch (CloudException e) {
                        e.printStackTrace();
                    }
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

        List<TripMate> tripMateList = currentTrip.getTripMateList();

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

    private void addMateToTrip(String mate) throws CloudException {
        boolean existsMate = false;
        for(TripMate tripMate : tripMates){
            if(mate.equals(tripMate.getUsername())){
                existsMate = true;
            }
        }
        if(!existsMate){
            CloudQuery query = new CloudQuery("User");
            query.equalTo(Constants.USERNAME, mate);
            query.find(new CloudObjectArrayCallback() {
                public void done(CloudObject[] userList, final CloudException e) {
                    if (userList != null) {
                        CloudUser user = (CloudUser) userList[0];
                        final TripMate tripMate = new TripMate();
                        try {
                            tripMate.setUserId(user.getId());
                            tripMate.setUserName(user.get(Constants.USERNAME).toString());
                            tripMate.setOrganizer(false);
                            tripMate.getTripMate().save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject tripMateSaved, CloudException t) throws CloudException {
                                    final TripMate tripMate = new TripMate(tripMateSaved);
                                    List<TripMate> tripMateList = currentTrip.getTripMateList();
                                    tripMateList.add(tripMate);
                                    currentTrip.setTripMateList(tripMateList);
                                    currentTrip.getTrip().save(new CloudObjectCallback() {
                                        @Override
                                        public void done(CloudObject tripSaved, CloudException t) throws CloudException {
                                            currentTrip = new Trip(tripSaved);
                                            tripMates.add(tripMate);
                                            mAdapter.addItem(tripMate);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                        } catch (CloudException e1) {
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