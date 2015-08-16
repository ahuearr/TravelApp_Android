package com.gusycorp.travel.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.util.Constants;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TripCalendarEditActivity extends MenuActivity implements OnClickListener{

	private EditText place;
	private EditText city;
	private EditText dateDepart;
	private EditText dateArrival;
	private EditText address;
	private EditText numRooms;
	private EditText prize;
	private Button save;

	private TripAccommodation tripAccommodation = new TripAccommodation();
	private String objectId;

	private Trip currentTrip;

	TravelApplication app;

	private DateFormat df = new SimpleDateFormat(Constants.ONLY_DATE_MASK);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accommodation_trip);

		app = (TravelApplication) getApplication();
		currentTrip = app.getCurrentTrip();

		place = (EditText) findViewById(R.id.place);
		city = (EditText) findViewById(R.id.city);
		dateDepart = (EditText) findViewById(R.id.date_depart);
		dateArrival = (EditText) findViewById(R.id.date_arrival);
		address = (EditText) findViewById(R.id.address);
		numRooms = (EditText) findViewById(R.id.numRooms);
		prize = (EditText) findViewById(R.id.prize);
		save = (Button) findViewById(R.id.save_button);

		save.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();

		if(bundle!=null){
			objectId = bundle.getString(Constants.OBJECTID);
			if(objectId !=null){
				tripAccommodation = app.getCurrentTripAccommodation();
				place.setText(bundle.getString(Constants.TRIPACCOMMODATION_PLACE));
				city.setText(bundle.getString(Constants.TRIPACCOMMODATION_CITY));
				dateArrival.setText(bundle.getString(Constants.TRIPTRANSPORT_DATEFROM));
				dateDepart.setText(bundle.getString(Constants.TRIPTRANSPORT_DATETO));
				address.setText(bundle.getString(Constants.TRIPACCOMMODATION_ADDRESS));
				numRooms.setText(Integer.toString(bundle.getInt(Constants.TRIPACCOMMODATION_NUMROOMS)));
				prize.setText(Double.toString(bundle.getDouble(Constants.TRIPTRANSPORT_PRIZE)));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.save_button:
				if(checkMandatory()){
					try{
						Date date = df.parse(dateArrival.getText().toString());
						tripAccommodation.put(Constants.TRIPTRANSPORT_DATEFROM, date);
						date = df.parse(dateDepart.getText().toString());
						tripAccommodation.put(Constants.TRIPTRANSPORT_DATETO, date);
						tripAccommodation.put(Constants.TRIPACCOMMODATION_PLACE, place.getText().toString());
						tripAccommodation.put(Constants.TRIPACCOMMODATION_CITY, city.getText().toString());
						tripAccommodation.put(Constants.TRIPACCOMMODATION_ADDRESS, address.getText().toString());
						tripAccommodation.put(Constants.TRIPACCOMMODATION_NUMROOMS, Integer.parseInt(numRooms.getText().toString()));
						tripAccommodation.put(Constants.TRIPTRANSPORT_PRIZE, Double.parseDouble(prize.getText().toString()));

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
			tripAccommodation.save();
			ParseRelation<TripAccommodation> tripAccommodationRelation = currentTrip.getRelation(Constants.TRIP_TRIPACCOMMODATION);
			tripAccommodationRelation.add(tripAccommodation);
			currentTrip.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		goOK();
	}

	private void update() {
		try {
			tripAccommodation.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		goOK();
	}
	private boolean checkMandatory(){
		if(viewIsEmpty(place) || viewIsEmpty(city)
				||viewIsEmpty(dateDepart) || viewIsEmpty(dateArrival)
				|| viewIsEmpty(address) || viewIsEmpty(numRooms)
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
