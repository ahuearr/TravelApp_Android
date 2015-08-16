package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripCalendarAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TripCalendarActivity extends MenuActivity{
    private CalendarView calendar;

    private Button addCalendarTrip;
    private TextView tripNameText;

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

        Date dateIni = currentTrip.getDateIniDate();

        initializeCalendar(dateIni);
    }

    public void initializeCalendar(Date date) {
        calendar = (CalendarView) findViewById(R.id.calendar);

        if(date!=null){
            calendar.setDate(date.getTime());
        }
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
                Intent intent = new Intent(TripCalendarActivity.this, TripCalendarListActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dat", day);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        int year = extras.getInt("year");
        int month = extras.getInt("month");
        int day = extras.getInt("day");
        if(year!=0 && day!=0){
            Date date = new Date(year, month, day);
            initializeCalendar(date);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_calendar_trip:
                Intent intent = new Intent(TripCalendarActivity.this, TripCalendarActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        menus.clear();
    }

}
