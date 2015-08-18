package com.gusycorp.travel.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class TripMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private TravelApplication app;

    private Trip currentTrip;

    private List<TripTransport> tripTransports = new ArrayList<TripTransport>();
    private List<TripAccommodation> tripAccommodations = new ArrayList<TripAccommodation>();
    private List<TripCalendar> tripCalendars = new ArrayList<TripCalendar>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();

        initializeMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    private void initializeMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                getTripCalendars();
            }
        }
    }

    private void getTripCalendars() {

        tripTransports.clear();
        tripAccommodations.clear();
        tripCalendars.clear();

        ParseRelation<TripCalendar> tripCalendar = currentTrip.getRelation(Constants.TRIPCALENDAR);
        ParseRelation<TripTransport> tripTransport = currentTrip.getRelation(Constants.TRIPTRANSPORT);
        ParseRelation<TripAccommodation> tripAccommodation = currentTrip.getRelation(Constants.TRIPACCOMMODATION);

        findTransports(tripCalendar, tripTransport, tripAccommodation);
    }

    private void findTransports(final ParseRelation<TripCalendar> tripCalendar,
                                ParseRelation<TripTransport> tripTransport,
                                final ParseRelation<TripAccommodation> tripAccommodation) {
        tripTransport.getQuery().findInBackground(new FindCallback<TripTransport>() {
            public void done(List<TripTransport> tripTransportList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    tripTransports.addAll(tripTransportList);
                }

                findAccommodations(tripCalendar, tripAccommodation);
            }
        });
    }

    private void findAccommodations(final ParseRelation<TripCalendar> tripCalendar,
                                    ParseRelation<TripAccommodation> tripAccommodation) {
        tripAccommodation.getQuery().findInBackground(new FindCallback<TripAccommodation>() {
            public void done(List<TripAccommodation> tripAccommodationList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    tripAccommodations.addAll(tripAccommodationList);
                }

                findCalendars(tripCalendar);
            }
        });
    }

    private void findCalendars(ParseRelation<TripCalendar> tripCalendar) {
        tripCalendar.getQuery().findInBackground(new FindCallback<TripCalendar>() {
            public void done(List<TripCalendar> tripCalendarList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    tripCalendars.addAll(tripCalendarList);
                    setUpMap();
                }
            }
        });
    }

    private void setUpMap() {
        for(TripTransport item : tripTransports){
            Double latitudeFrom = item.getLatitudeFrom();
            Double longitudeFrom = item.getLongtiudeFrom();
            if(latitudeFrom!=null && latitudeFrom!=0.0 && longitudeFrom!=null && longitudeFrom!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeFrom, longitudeFrom)).title(getString(R.string.transportDepartureFrom) + " " + item.getFrom()));
            } else {

            }
            Double latitudeTo = item.getLatitudeTo();
            Double longitudeTo = item.getLongtiudeTo();
            if(latitudeFrom!=null && latitudeFrom!=0.0 && latitudeTo!=null && longitudeTo!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeTo, longitudeTo)).title(getString(R.string.transportArrivalTo) + " " + item.getFrom()));
            } else {

            }
        }

        for(TripAccommodation item : tripAccommodations){
            Double latitude = item.getLatitude();
            Double longitude = item.getLongtiude();
            if(latitude!=null && latitude!=0.0 && longitude!=null && longitude!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(getString(R.string.transportDepartureFrom) + " " + item.getFrom()));
            } else {

            }
        }

    }
}
