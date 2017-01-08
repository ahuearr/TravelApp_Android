package com.gusycorp.travel.activity.Accommodation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.adapter.ListTripActivitiesMateAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.util.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;

public class TripAccommodationMatesActivity extends MenuActivity implements View.OnClickListener{

    private Button save;
    private Button share;
    private TextView activityNameText;
    private TextView sharePrize;
    private ListView listView;

    private ListTripActivitiesMateAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;
    private TripAccommodation currentTripAcommodation = new TripAccommodation();
    private List<TripMate> tripMates;
    List<TripMate> tripMatefromTripMatePrize = new ArrayList<TripMate>();

    private Double prize = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_mates_trip);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        currentTripAcommodation = app.getCurrentTripAccommodation();

        activityNameText = (TextView) findViewById(R.id.text_name);
        sharePrize = (TextView) findViewById(R.id.text_prize);
        save = (Button) findViewById(R.id.save_button);
        share = (Button) findViewById(R.id.share_button);

        if(!app.isOrganizer()) {
            save.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
        }

        save.setOnClickListener(this);
        share.setOnClickListener(this);

        String tripAccommodationName = currentTripAcommodation.getPlace() + " - " + currentTripAcommodation.getCity();
        activityNameText.setText(tripAccommodationName);
        prize = currentTripAcommodation.getPrize()==null ? 0 : currentTripAcommodation.getPrize();
        sharePrize.setText(Double.toString(prize));
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
            case R.id.save_button:
                saveAccommodationMates();
                break;
            case R.id.share_button:
                sharePrize();
                break;
        }
    }

    private void getTripMates(String tripObjectId) {

        mAdapter = new ListTripActivitiesMateAdapter(TripAccommodationMatesActivity.this,
                R.layout.row_list_activities_mate_trip, new ArrayList<TripMatePrize>(), app.isOrganizer());

        HashMap<String,String> itemHeader=new HashMap<String, String>();

        mAdapter.addSectionHeaderItem(itemHeader);

        List<TripMate> tripMateList = currentTrip.getTripMateList();

        tripMates.addAll(tripMateList);

        final List<TripMatePrize> tripAccommodationMateList = currentTripAcommodation.getTripMatePrizeList();

        for(TripMatePrize tripMatePrize : tripAccommodationMateList){
            mAdapter.addItem(tripMatePrize);
            tripMatefromTripMatePrize.add(tripMatePrize.getTripMate());
        }

        for(TripMate tripMate : tripMateList){
            if(!tripMatefromTripMatePrize.contains(tripMate)){
                TripMatePrize tripMatePrize = new TripMatePrize();
                try {
                    tripMatePrize.setTripMate(tripMate);
                    tripMatePrize.setPrize(BigDecimal.ZERO.doubleValue());
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException t) throws CloudException {
                            TripMatePrize tripMatePrize = new TripMatePrize(tripMatePrizeSaved);
                            tripAccommodationMateList.add(tripMatePrize);
                            //TODO La siguiente linea añadira el amigo o hara un append de la lista entera con el nuevo amigo duplicando los existentes?
                            currentTripAcommodation.setTripMatePrizeList(tripAccommodationMateList);
                            currentTripAcommodation.getTripAccommodation().save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject tripAccommodationSaved, CloudException t) throws CloudException {
                                }
                            });
                        }
                    });
                } catch (CloudException e) {
                    e.printStackTrace();
                }
                mAdapter.addItem(tripMatePrize);
            }
        }
        listView.setAdapter(mAdapter);
    }

    private void saveAccommodationMates() {
        try {
            //TODO No deberia ser necesario coger y actualizar la lista de nuevo
//            final List<TripMatePrize> tripAccommodationMateList = currentTripAcommodation.getTripMatePrizeList();
            for(TripMatePrize tripMatePrize : mAdapter.getTripMateList()){
                if(tripMatePrize!=null){
                    tripMatePrize.getTripMatePrize().save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject tripMatePrizeSaved, CloudException t) throws CloudException {
                            //TODO Grabacion correcta de amigos? Se actualizará solo? Hara falta hacer algo?
/*
                            TripMatePrize tripMatePrize = new TripMatePrize(tripMatePrizeSaved);
                            tripAccommodationMateList.add(tripMatePrize);
*/
                        }
                    });
                }
            }
            //TODO Es necesario actualizar el alojamiento con la nueva lista de tripmateprizes? Yo creo que no
/*
            currentTripAcommodation.save(new CloudObjectCallback() {
                @Override
                public void done(CloudObject x, CloudException t) throws CloudException {
                }
            });
*/
        } catch (CloudException e) {
            e.printStackTrace();
        }

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