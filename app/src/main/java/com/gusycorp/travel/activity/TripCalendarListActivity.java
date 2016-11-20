package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripCalendarAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;

public class TripCalendarListActivity extends MenuActivity{

    private Button addCalendarTrip;
    private TextView tripNameText;
    private DatePicker datePicker;
    private ListView listView;

    private ListTripCalendarAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;

    private List<TripTransport> tripTransports = new ArrayList<TripTransport>();
    private List<TripAccommodation> tripAccommodations = new ArrayList<TripAccommodation>();
    private List<TripCalendar> tripCalendars = new ArrayList<TripCalendar>();

    boolean isRunning = false;

    private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_trip_list);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        app.setCurrentTripCalendar(new TripCalendar());

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        addCalendarTrip = (Button) findViewById(R.id.add_calendar_trip);
        datePicker = (DatePicker) findViewById(R.id.calendar_date_picker);
        if(!app.isOrganizer()){
            addCalendarTrip.setVisibility(View.GONE);
        }
        addCalendarTrip.setOnClickListener(this);

        tripName = currentTrip.getTripName();
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.calendar_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        Bundle extras = getIntent().getExtras();
        int year = extras.getInt("year");
        int month = extras.getInt("month");
        int day = extras.getInt("day");
        if(year == 0 && month == 0 && day == 0){
            Date dateIni = null;
            try {
                dateIni = currentTrip.getDateIniDate();
                calendar.setTime(dateIni);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (!isRunning) {
                    isRunning=true;
                    getTripCalendars(year, monthOfYear, dayOfMonth);
                }
            }
        });
        getTripCalendars(year, month, day);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_calendar_trip:
                Intent intent = new Intent(TripCalendarListActivity.this, TripCalendarActivity.class);
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Calendar calendar = new GregorianCalendar(year, month, day);
                intent.putExtra(Constants.DATE, df.format(calendar.getTime()));
                intent.putExtra(Constants.ISACTIVITY, true);
                startActivity(intent);
                break;
        }
    }

    private void getTripCalendars(final int year, final int month, final int day) {

        mAdapter = new ListTripCalendarAdapter(TripCalendarListActivity.this,
                R.layout.row_list_calendar_trip, new ArrayList<TripCalendar>());

        tripTransports.clear();
        tripAccommodations.clear();
        tripCalendars.clear();
        HashMap<String,String> itemHeader=new HashMap<String, String>();
        itemHeader.put(Constants.TRIPCALENDARLIST_COLUMN_ONE, getString(R.string.date));
        itemHeader.put(Constants.TRIPCALENDARLIST_COLUMN_TWO, getString(R.string.activity));
        mAdapter.addSectionHeaderItem(itemHeader);

        findTransports(year, month, day);
    }

    private void findTransports(final int year, final int month, final int day) {

        List<TripTransport> tripTransportList = currentTrip.getTripTransportList();

        for (TripTransport tripTransport : tripTransportList) {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            try {
                calendar1.setTime(tripTransport.getDateFromDate());
                calendar2.setTime(tripTransport.getDateToDate());
                if ((calendar1.get(Calendar.DAY_OF_MONTH) == day
                        && calendar1.get(Calendar.MONTH) == month
                        && calendar1.get(Calendar.YEAR) == year)
                        || (calendar2.get(Calendar.DAY_OF_MONTH) == day
                        && calendar2.get(Calendar.MONTH) == month
                        && calendar2.get(Calendar.YEAR) == year))
                {
                    tripTransports.add(tripTransport);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        findAccommodations(year, month, day);
    }

    private void findAccommodations(final int year, final int month, final int day) {

        List<TripAccommodation> tripAccommodationList = currentTrip.getTripAccommodationList();
        for (TripAccommodation tripAccommodation : tripAccommodationList) {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            try {
                calendar1.setTime(tripAccommodation.getDateFromDate());
                calendar2.setTime(tripAccommodation.getDateToDate());
                if ((calendar1.get(Calendar.DAY_OF_MONTH) == day
                        && calendar1.get(Calendar.MONTH) == month
                        && calendar1.get(Calendar.YEAR) == year)
                        || (calendar2.get(Calendar.DAY_OF_MONTH) == day
                        && calendar2.get(Calendar.MONTH) == month
                        && calendar2.get(Calendar.YEAR) == year)) {
                    tripAccommodations.add(tripAccommodation);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        findAndManageCalendarActivities(year, month, day);
    }

    private void findAndManageCalendarActivities(final int year, final int month, final int day) {
        List<TripCalendar> tripCalendarList = currentTrip.getTripCalendarList();

        try {
            for (TripCalendar tripCalendar : tripCalendarList) {
                Calendar calendar = Calendar.getInstance();
                    calendar.setTime(tripCalendar.getDateDate());
                if (calendar.get(Calendar.DAY_OF_MONTH) == day
                        && calendar.get(Calendar.MONTH) == month
                        && calendar.get(Calendar.YEAR) == year) {
                    tripCalendars.add(tripCalendar);
                }
            }

            for(TripTransport itemTransport: tripTransports) {
                TripCalendar itemCalendar = new TripCalendar();
                itemCalendar.set(Constants.ISACTIVITY, false);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(itemTransport.getDateFromDate());
                if (calendar.get(Calendar.DAY_OF_MONTH) == day
                        && calendar.get(Calendar.MONTH) == month
                        && calendar.get(Calendar.YEAR) == year) {
                    itemCalendar.set(Constants.DATE, itemTransport.getDateFromDate());
                    itemCalendar.set(Constants.ACTIVITY, getString(R.string.transportDepartureFrom) + " " + itemTransport.getFrom());
                    itemCalendar.set(Constants.PLACE, itemTransport.getFrom());
                    itemCalendar.set(Constants.PRIZE, itemTransport.getPrize());
                    tripCalendars.add(itemCalendar);
                    itemCalendar = new TripCalendar();
                    itemCalendar.set(Constants.ISACTIVITY, false);
                }
                calendar.setTime(itemTransport.getDateToDate());
                if (calendar.get(Calendar.DAY_OF_MONTH) == day
                        && calendar.get(Calendar.MONTH) == month
                        && calendar.get(Calendar.YEAR) == year) {
                    itemCalendar.set(Constants.DATE, itemTransport.getDateToDate());
                    itemCalendar.set(Constants.ACTIVITY, getString(R.string.transportArrivalTo) + " " + itemTransport.getTo());
                    itemCalendar.set(Constants.PLACE, itemTransport.getTo());
                    itemCalendar.set(Constants.PRIZE, itemTransport.getPrize());
                    tripCalendars.add(itemCalendar);
                }
            }

            for(TripAccommodation itemAccommodation: tripAccommodations) {
                TripCalendar itemCalendar = new TripCalendar();
                itemCalendar.set(Constants.ISACTIVITY, false);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(itemAccommodation.getDateFromDate());
                if (calendar.get(Calendar.DAY_OF_MONTH) == day
                        && calendar.get(Calendar.MONTH) == month
                        && calendar.get(Calendar.YEAR) == year) {
                    itemCalendar.set(Constants.DATE, itemAccommodation.getDateFromDate());
                    itemCalendar.set(Constants.ACTIVITY, getString(R.string.accommodationArrivalTo) + " " + itemAccommodation.getPlace());
                    itemCalendar.set(Constants.PLACE, itemAccommodation.getPlace());
                    itemCalendar.set(Constants.CITY, itemAccommodation.getCity());
                    itemCalendar.set(Constants.PRIZE, itemAccommodation.getPrize());
                    tripCalendars.add(itemCalendar);
                    itemCalendar = new TripCalendar();
                    itemCalendar.set(Constants.ISACTIVITY, false);
                }
                calendar.setTime(itemAccommodation.getDateToDate());
                if (calendar.get(Calendar.DAY_OF_MONTH) == day
                        && calendar.get(Calendar.MONTH) == month
                        && calendar.get(Calendar.YEAR) == year) {
                    itemCalendar.set(Constants.DATE, itemAccommodation.getDateToDate());
                    itemCalendar.set(Constants.ACTIVITY, getString(R.string.accommodationDepartureFrom) + " " + itemAccommodation.getPlace());
                    itemCalendar.set(Constants.PLACE, itemAccommodation.getPlace());
                    itemCalendar.set(Constants.CITY, itemAccommodation.getCity());
                    itemCalendar.set(Constants.PRIZE, itemAccommodation.getPrize());
                    tripCalendars.add(itemCalendar);
                }
            }

            Collections.sort(tripCalendars);
            for(TripCalendar item : tripCalendars){
                mAdapter.addItem(item);
            }
            listView.setAdapter(mAdapter);
            isRunning=false;

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TripCalendar tripCalendar = (TripCalendar) listView.getAdapter().getItem(position);
                    if (tripCalendar != null) {
                        app.setCurrentTripCalendar(tripCalendar);
                        Intent intent = new Intent(TripCalendarListActivity.this, TripCalendarActivity.class);
                        intent.putExtra(Constants.OBJECTID, tripCalendar.getId());
                        intent.putExtra(Constants.DATE, tripCalendar.getDate());
                        intent.putExtra(Constants.ACTIVITY, tripCalendar.getActivity());
                        intent.putExtra(Constants.PLACE, tripCalendar.getPlace());
                        intent.putExtra(Constants.CITY, tripCalendar.getCity());
                        intent.putExtra(Constants.ISACTIVITY, tripCalendar.isActivity());
                        startActivity(intent);
                    }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CloudException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }

}
