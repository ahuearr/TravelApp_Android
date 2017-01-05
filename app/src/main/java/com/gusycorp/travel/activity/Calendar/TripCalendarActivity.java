package com.gusycorp.travel.activity.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.util.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;


public class TripCalendarActivity extends MenuActivity implements OnClickListener{

	private EditText date;
	private EditText activity;
	private EditText place;
	private EditText city;
	private EditText prize;
	private Button save;
	private ImageButton tripMates;

	private TripCalendar tripCalendar = new TripCalendar();
	private String objectId;

	private Trip currentTrip;

	TravelApplication app;

	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_trip);

		app = (TravelApplication) getApplication();
		currentTrip = app.getCurrentTrip();

		date = (EditText) findViewById(R.id.date);
		activity = (EditText) findViewById(R.id.text_activity);
		place = (EditText) findViewById(R.id.place);
		city = (EditText) findViewById(R.id.city);
		prize = (EditText) findViewById(R.id.prize);
		save = (Button) findViewById(R.id.save_button);
		tripMates = (ImageButton) findViewById(R.id.calendar_trip_mates);

		if(!app.isOrganizer()){
			date.setEnabled(false);
			activity.setEnabled(false);
			place.setEnabled(false);
			city.setEnabled(false);
			prize.setEnabled(false);
			date.setFocusable(false);
			activity.setFocusable(false);
			place.setFocusable(false);
			city.setFocusable(false);
			prize.setFocusable(false);
			save.setVisibility(View.GONE);
		}

		save.setOnClickListener(this);
		tripMates.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();

		if(bundle!=null){
			date.setText(bundle.getString(Constants.DATE));
			objectId = bundle.getString(Constants.OBJECTID);
			tripCalendar = app.getCurrentTripCalendar();
			activity.setText(bundle.getString(Constants.ACTIVITY));
			place.setText(bundle.getString(Constants.PLACE));
			city.setText(bundle.getString(Constants.CITY));
			prize.setText(Double.toString(bundle.getDouble(Constants.PRIZE)));
			if(!bundle.getBoolean(Constants.ISACTIVITY)){
				save.setEnabled(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.save_button:
				if(checkMandatory()){
					try{
						Date dateActivity = df.parse(date.getText().toString());
						tripCalendar.set(Constants.DATE, dateActivity);
						tripCalendar.set(Constants.ACTIVITY, activity.getText().toString());
						tripCalendar.set(Constants.PLACE, place.getText().toString());
						tripCalendar.set(Constants.CITY, city.getText().toString());
						tripCalendar.set(Constants.PRIZE, Double.parseDouble(prize.getText().toString()));
						tripCalendar.set(Constants.ISACTIVITY, true);

						if(objectId!=null){
							update();
						} else {
							save();
						}
					} catch (CloudException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else{
					Toast.makeText(this, getString(R.string.field_mandatory), Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.calendar_trip_mates:
				Intent intent = new Intent(TripCalendarActivity.this, TripCalendarMatesActivity.class);
				startActivity(intent);
				break;
		}
	}

	private void save(){
		try {
			tripCalendar.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					tripCalendar = (TripCalendar) x;
					List<TripCalendar> tripCalendars = currentTrip.getTripCalendarList();
					tripCalendars.add(tripCalendar);
					currentTrip.set(Constants.TRIPCALENDAR, tripCalendars);
					currentTrip.save(new CloudObjectCallback() {
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							Trip trip = (Trip) x;
							app.setCurrentTrip(trip);
							goOK();
						}
					});
				}
			});
		} catch (CloudException e) {
			e.printStackTrace();
		}

		goOK();
	}

	private void update() {
		try {
			tripCalendar.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					//TODO Con Parse no era necesario actualizar el currentTrip. Con Cloudboost??
					goOK();
				}
			});
		} catch (CloudException e) {
			e.printStackTrace();
		}

		goOK();
	}
	private boolean checkMandatory(){
		if(viewIsEmpty(place) || viewIsEmpty(city)
				||viewIsEmpty(date) || viewIsEmpty(activity)
				|| viewIsEmpty(prize)){
			return false;
		}
		return true;
	}

	private boolean viewIsEmpty(final TextView view) {
		boolean empty = false;
		if (TextUtils.isEmpty(view.getText().toString())) {
			empty = true;
		}
		return empty;
	}

	private void goOK(){
		onBackPressed();
	}
}
