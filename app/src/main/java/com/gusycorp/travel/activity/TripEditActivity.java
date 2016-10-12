package com.gusycorp.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudUser;


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

			trip = app.getCurrentTrip();
			tripNameText.setText(trip.getTripName());
			if (trip.getDateIni() != null) {
				dateIniText.setText(trip.getDateIni());
			} else {
				dateIniText.setText(getString(R.string.date_empty));
			}
			if (trip.getDateFin() != null) {
				dateFinText.setText(trip.getDateFin());
			} else {
				dateFinText.setText(getString(R.string.date_empty));
			}
			List<String> destinyList = trip.getDestinyName();
			String destinies = "";
			if (destinyList != null) {
				if (destinyList.size() > 0) {
					for (String destiny : destinyList) {
						destinies += destiny + ",";
					}
					destinies = destinies.substring(0,
							destinies.length() - 1);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.save_button:
				if(checkMandatory()) {
					try {
						trip.set(Constants.TRIPNAME, tripNameText.getText().toString());
						Date date = df.parse(dateIniText.getText().toString());
						trip.set(Constants.DATEINI, date);
						date = df.parse(dateFinText.getText().toString());
						trip.set(Constants.DATEFIN, date);
						List<String> destinyList = Arrays.asList(destinyNameText.getText().toString().split(","));
						List<String> destinyListTrimmed = new ArrayList<>();
						for(String destiny : destinyList){
							destinyListTrimmed.add(destiny.trim());
						}
						trip.set(Constants.DESTINYNAME, destinyListTrimmed);
						trip.set(Constants.STATUS, Constants.VALUE_STATUS_FUTURE);
						trip.set(Constants.ORGANIZERID, CloudUser.getcurrentUser().getId());
						if (tripObjectId != null) {
							trip.save(new CloudObjectCallback() {
								@Override
								public void done(CloudObject x, CloudException t) throws CloudException {
									trip = (Trip) x;
									app.setCurrentTrip(trip);
									onBackPressed();
								}
							});
						} else {
							TripMate tripMate = new TripMate();
							tripMate.set(Constants.USERID, CloudUser.getcurrentUser().getId());
							tripMate.set(Constants.USERNAME, CloudUser.getcurrentUser().get(Constants.USERNAME));
							tripMate.set(Constants.ORGANIZER, true);
							tripMate.save(new CloudObjectCallback() {
								@Override
								public void done(CloudObject x, CloudException t) throws CloudException {
									TripMate tripMate = (TripMate) x;
									trip.set(Constants.TRIPMATE, tripMate);
									trip.save(new CloudObjectCallback() {
										@Override
										public void done(CloudObject x, CloudException t) throws CloudException {
											trip = (Trip) x;
											app.setCurrentTrip(trip);
											Intent intent = new Intent(TripEditActivity.this, TripActivity.class);
											intent.putExtra("tripObjectId", trip.getId());
											startActivity(intent);
											finish();
										}
									});
								}

							});
						}
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					} catch (CloudException e) {
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
