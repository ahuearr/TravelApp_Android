package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TripCalendarListActivity extends MenuActivity{

    private Button addCalendarTrip;
    private TextView tripNameText;
    private DatePicker datePicker;
    private ListView listView;

    private ListTripCalendarAdapter mAdapter;

    private TravelApplication app;

    private Trip currentTrip;


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
        Log.e("TAG", year + "");
        Log.e("TAG", month + "");
        Log.e("TAG", day + "");
        if(year == 0 && month == 0 && day == 0){
            Date dateIni = currentTrip.getDateIniDate();
            calendar.setTime(dateIni);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        Log.e("TAG", year + "");
        Log.e("TAG", month + "");
        Log.e("TAG", day + "");

        datePicker.updateDate(year, month, day);

        getTripCalendars(currentTrip.getObjectId(), year, month, day);
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

    private void getTripCalendars(String tripObjectId, final int year, final int month, final int day) {

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
                        if(tripCalendar.getDateDate().getDay()==day
                                && tripCalendar.getDateDate().getMonth()==month
                                && tripCalendar.getDateDate().getYear()==year){
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
