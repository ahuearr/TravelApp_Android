package com.gusycorp.travel.activity.Transport;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.activity.Trip.TripActivity;
import com.gusycorp.travel.activity.Trip.TripEditActivity;
import com.gusycorp.travel.adapter.ListTripActivitiesMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudUser;

public class TripTransportMatesActivity extends MenuActivity implements View.OnClickListener{

    private Button save;
    private Button share;
    private TextView transportNameText;
    private TextView sharePrize;
    private ListView listView;

    private ListTripActivitiesMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private TripTransport currentTripTransport = new TripTransport();
    private ArrayList<String> tripMates;
    ArrayList<String> tripMatefromTripMatePrize = new ArrayList<String>();

    private Double prize = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_mates_trip);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        currentTripTransport = app.getCurrentTripTransport();

        transportNameText = (TextView) findViewById(R.id.text_name);
        sharePrize = (TextView) findViewById(R.id.text_prize);
        save = (Button) findViewById(R.id.save_button);
        share = (Button) findViewById(R.id.share_button);

        if(!app.isOrganizer()) {
            save.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
        }

        save.setOnClickListener(this);
        share.setOnClickListener(this);

        String tripTransportName = currentTripTransport.getFrom() + " - " + currentTripTransport.getTo();
        transportNameText.setText(tripTransportName);
        prize = currentTripTransport.getPrize()==null ? 0 : currentTripTransport.getPrize();
        sharePrize.setText(Double.toString(prize));
        tripMates = new ArrayList<String>();
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
            case R.id.save_button:
                new Save().execute();
                break;
            case R.id.share_button:
                sharePrize();
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripActivitiesMateAdapter(TripTransportMatesActivity.this,
                R.layout.row_list_activities_mate_trip, new ArrayList<TripMatePrize>(), app.isOrganizer());

        HashMap<String,String> itemHeader=new HashMap<String, String>();

        mAdapter.addSectionHeaderItem(itemHeader);

        ArrayList<TripMate> tripMateList = currentTrip.getTripMateList();
        for(TripMate tripMate : tripMateList){
            tripMates.add(tripMate.getId());
        }

        final ArrayList<TripMatePrize> tripTransportMateList = currentTrip.getTripMatePrize(Constants.PARENTTYPE_TRANSPORT, currentTripTransport.getId());

        for(TripMatePrize tripMatePrize : tripTransportMateList){
            mAdapter.addItem(tripMatePrize);
            tripMatefromTripMatePrize.add(tripMatePrize.getTripMateId());
        }

        for(TripMate tripMate : tripMateList){
            if(!tripMatefromTripMatePrize.contains(tripMate.getId())){
                TripMatePrize tripMatePrize = new TripMatePrize();
                try {
                    tripMatePrize.setPrize(BigDecimal.ZERO.doubleValue());
                    tripMatePrize.setTripMateId(tripMate.getId());
                    tripMatePrize.setParentId(currentTripTransport.getId());
                    tripMatePrize.setParentType(Constants.PARENTTYPE_TRANSPORT);
                    tripMatePrize.setTripId(currentTrip.getId());
                    tripMatePrize.setMateUsername(tripMate.getUsername());
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException t) throws CloudException {
                            TripMatePrize tripMatePrize = new TripMatePrize(tripMatePrizeSaved);
                            tripTransportMateList.add(tripMatePrize);
                        }
                    });
                } catch (CloudException e1) {
                    e1.printStackTrace();
                }
                mAdapter.addItem(tripMatePrize);
            }
        }
        listView.setAdapter(mAdapter);
    }

    private class Save extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            return saveTransportMates();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                currentTrip.setTripMatePrizeList(mAdapter.getTripMateList());
                onBackPressed();
            }else{
                Toast.makeText(TripTransportMatesActivity.this, getString(R.string.message_ko), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean saveTransportMates() {

        final boolean[] success = {false};
        try {

            for(final TripMatePrize tripMatePrize : mAdapter.getTripMateList()){
                if(tripMatePrize!=null){
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException e) throws CloudException {
                            if(tripMatePrizeSaved!=null){
                                success[0] =true;
                            }else{
                                success[0] =false;
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (CloudException e) {
            e.printStackTrace();
        }
        return success[0];
    }

    private void sharePrize() {
        int matesNumber = mAdapter.getTripMateList().size()-1;
        Double sharedPrize = Math.floor(prize * 100) / 100;
        if(matesNumber>0){
            sharedPrize = Math.floor((prize/matesNumber)*100)/100;
        }
        for(int i=0;i<mAdapter.getTripMateList().size();i++){
            if (mAdapter.getTripMateList().get(i) != null) {
                try {
                    mAdapter.getTripMateList().get(i).setPrize(sharedPrize);
                } catch (CloudException e) {
                    e.printStackTrace();
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }
}