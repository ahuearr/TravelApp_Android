package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripCalendarAdapter;
import com.gusycorp.travel.adapter.ListTripTransportAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TripCalendarListActivity extends MenuActivity{
    private CalendarView calendar;

    private Button addCalendarTrip;
    private TextView tripNameText;
    private ListView listView;

    private ListTripCalendarAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_trip);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();
        app.setCurrentTripCalendar(new TripCalendar());

        tripNameText = (TextView) findViewById(R.id.text_trip_name);
        addCalendarTrip = (Button) findViewById(R.id.add_calendar_trip);
        addCalendarTrip.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        tripName = extras.getString("tripName");
        tripNameText.setText(tripName);

        listView=(ListView)findViewById(R.id.calendar_list);

        initializeCalendar();
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.backgroundMenu));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.backgroundMenuSelected);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                getTripCalendars(currentTrip.getObjectId(), new Date(year, month, day));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripCalendars(currentTrip.getObjectId(), new Date(calendar.getDate()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_calendar_trip:
                Intent intent = new Intent(TripCalendarListActivity.this, TripCalendarActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getTripCalendars(String tripObjectId, final Date date) {

        mAdapter = new ListTripCalendarAdapter(TripCalendarListActivity.this,
                R.layout.row_list_calendar_trip, new ArrayList<TripCalendar>());

        HashMap<String,String> itemHeader=new HashMap<String, String>();
        itemHeader.put(Constants.TRIPCALENDARLIST_COLUMN_ONE, Constants.TRIPCALENDARLIST_COLUMN_ONE);
        itemHeader.put(Constants.TRIPCALENDARLIST_COLUMN_TWO, Constants.TRIPCALENDARLIST_COLUMN_TWO);
        mAdapter.addSectionHeaderItem(itemHeader);

        ParseRelation<TripCalendar> tripCalendar = currentTrip.getRelation(Constants.TRIP_TRIPCALENDAR);

        tripCalendar.getQuery().findInBackground(new FindCallback<TripCalendar>() {
            public void done(List<TripCalendar> tripCalendarList, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    for (TripCalendar tripCalendar : tripCalendarList) {
                        if(tripCalendar.getDateDate().getDay()==date.getDay()
                                && tripCalendar.getDateDate().getMonth()==date.getMonth()
                                && tripCalendar.getDateDate().getYear()==date.getYear()){
                            mAdapter.addItem(tripCalendar);
                        }
                    }
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TripCalendar tripCalendar = (TripCalendar) listView.getAdapter().getItem(position);
                            if (tripCalendar != null) {
                                app.setCurrentTripCalendar(tripCalendar);
                                Intent intent = new Intent(TripCalendarListActivity.this, TripCalendarActivity.class);
                                intent.putExtra(Constants.OBJECTID, tripCalendar.getObjectId());
                                intent.putExtra(Constants.TRIPCALENDAR_DATE, tripCalendar.getDate());
                                intent.putExtra(Constants.TRIPCALENDAR_ACTIVITY, tripCalendar.getActivity());
                                intent.putExtra(Constants.TRIPACCOMMODATION_PLACE, tripCalendar.getPlace());
                                intent.putExtra(Constants.TRIPACCOMMODATION_CITY, tripCalendar.getCity());
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
