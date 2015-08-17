package com.gusycorp.travel.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.util.Constants;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TripCalendarActivity extends MenuActivity implements OnClickListener{

	private EditText date;
	private EditText activity;
	private EditText place;
	private EditText city;
	private EditText prize;
	private Button save;

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

		save.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();

		if(bundle!=null){
			date.setText(bundle.getString(Constants.TRIPCALENDAR_DATE));
			objectId = bundle.getString(Constants.OBJECTID);
			tripCalendar = app.getCurrentTripCalendar();
			activity.setText(bundle.getString(Constants.TRIPCALENDAR_ACTIVITY));
			place.setText(bundle.getString(Constants.TRIPACCOMMODATION_PLACE));
			city.setText(bundle.getString(Constants.TRIPACCOMMODATION_CITY));
			prize.setText(Double.toString(bundle.getDouble(Constants.TRIPTRANSPORT_PRIZE)));
			if(!bundle.getBoolean(Constants.TRIPCALENDAR_ISACTIVITY)){
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
						tripCalendar.put(Constants.TRIPCALENDAR_DATE, dateActivity);
						tripCalendar.put(Constants.TRIPCALENDAR_ACTIVITY, activity.getText().toString());
						tripCalendar.put(Constants.TRIPACCOMMODATION_PLACE, place.getText().toString());
						tripCalendar.put(Constants.TRIPACCOMMODATION_CITY, city.getText().toString());
						tripCalendar.put(Constants.TRIPTRANSPORT_PRIZE, Double.parseDouble(prize.getText().toString()));
						tripCalendar.put(Constants.TRIPCALENDAR_ISACTIVITY, true);

						if(objectId!=null){
							update();
						} else {
							save();
						}
					} catch (java.text.ParseException e){
						e.printStackTrace();
					}
				} else{
					Toast.makeText(this, getString(R.string.field_mandatory), Toast.LENGTH_LONG).show();
				}
				break;
		}
	}

	private void save(){
		try {
			tripCalendar.save();
			ParseRelation<TripCalendar> tripCalendarRelation = currentTrip.getRelation(Constants.TRIP_TRIPCALENDAR);
			tripCalendarRelation.add(tripCalendar);
			currentTrip.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		goOK();
	}

	private void update() {
		try {
			tripCalendar.save();
		} catch (ParseException e) {
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
