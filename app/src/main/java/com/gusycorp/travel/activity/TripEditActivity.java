package com.gusycorp.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class TripEditActivity extends Activity implements View.OnClickListener{

	private EditText tripNameText;
	private EditText dateIniText;
	private EditText dateFinText;
	private EditText destinyNameText;
	private Button save;

	private TravelApplication app;

	private String tripObjectId;
	private Trip trip = new Trip();

	private DateFormat df = new SimpleDateFormat(Constants.ONLY_DATE_MASK);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_edit);

		app = (TravelApplication) getApplication();

		tripNameText = (EditText) findViewById(R.id.text_trip_name);
		dateIniText = (EditText) findViewById(R.id.text_date_depart);
		dateFinText = (EditText) findViewById(R.id.text_date_arrival);
		destinyNameText = (EditText) findViewById(R.id.text_destiny_name);
		save = (Button) findViewById(R.id.save_button);

		save.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			tripObjectId = extras.getString("tripObjectId");

			getTrip(tripObjectId);
		}
	}

	private void getTrip(String tripObjectId) {
		Trip.findTripInBackground(tripObjectId, new GetCallback<Trip>() {

			@Override
			public void done(Trip tripFind, ParseException e) {
				trip = tripFind;
				tripNameText.setText(tripFind.getTripName());
				if (tripFind.getDateIni() != null) {
					dateIniText.setText(tripFind.getDateIni());
				} else {
					dateIniText.setText(getString(R.string.date_empty));
				}
				if (tripFind.getDateFin() != null) {
					dateFinText.setText(tripFind.getDateFin());
				} else {
					dateFinText.setText(getString(R.string.date_empty));
				}
				List<String> destinyList = tripFind.getDestinyName();
				String destinies = "";
				if (destinyList != null) {
					if (destinyList.size() > 0) {
						for (String destiny : destinyList) {
							destinies += destiny + ",";
						}
						destinies = destinies.substring(0,
								destinies.length() - 2);
						destinyNameText.setText(destinies);
					} else {
						destinyNameText
								.setText(getString(R.string.destiny_name_empty));
					}
				} else {
					destinyNameText
							.setText(getString(R.string.destiny_name_empty));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.save_button:
				if(checkMandatory()) {
					try {
						trip.put(Constants.TRIP_TRIPNAME, tripNameText.getText().toString());
						Date date = df.parse(dateIniText.getText().toString());
						trip.put(Constants.TRIP_DATEINI,date);
						date = df.parse(dateFinText.getText().toString());
						trip.put(Constants.TRIP_DATEFIN,date);
						trip.addAllUnique(Constants.TRIP_DESTINYNAME, Arrays.asList(destinyNameText.getText().toString().split(",")));
						trip.put(Constants.TRIP_STATUS, Constants.TRIP_VALUE_STATUS_FUTURE);
						trip.put(Constants.TRIP_ORGANIZERID, "1");
						try {
							trip.save();
							if(tripObjectId!=null){
								onBackPressed();
							} else {
								Intent intent = new Intent(TripEditActivity.this, TripActivity.class);
								intent.putExtra("tripObjectId", trip.getObjectId());
								startActivity(intent);
								finish();
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(this, getString(R.string.field_mandatory), Toast.LENGTH_LONG).show();
				}
				break;
		}
	}

	private void save(){

	}

	private void update(){

	}

	private boolean checkMandatory(){
		if(viewIsEmpty(tripNameText) || viewIsEmpty(dateIniText)
				|| viewIsEmpty(dateFinText) || viewIsEmpty(destinyNameText)){
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

}
