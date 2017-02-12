package com.gusycorp.travel.activity.Trip;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.gusycorp.travel.model.ITObjectCallback;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
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

	private DateTimeFormatter df = DateTimeFormat.forPattern(Constants.ONLY_DATE_MASK);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
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
		}catch(ParseException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.save_button:
				if(checkMandatory()) {
					try {
						trip.setTripName(tripNameText.getText().toString());
						DateTime date = df.parseDateTime(dateIniText.getText().toString().replaceAll("/", "-"));
						trip.setDateIni(date);
						trip.setDateIni(dateIniText.getText().toString().replaceAll("/", "-"));
						date = df.parseDateTime(dateFinText.getText().toString().replaceAll("/", "-"));
						trip.setDateFin(date);
						trip.setDateFin(dateFinText.getText().toString().replaceAll("/", "-"));
						List<String> destinyList = Arrays.asList(destinyNameText.getText().toString().split(","));
						List<String> destinyListTrimmed = new ArrayList<>();
						for(String destiny : destinyList){
							destinyListTrimmed.add(destiny.trim());
						}
						trip.setDestinyName(destinyListTrimmed);
						trip.setStatus(Constants.VALUE_STATUS_FUTURE);
						trip.setOrganizerId(CloudUser.getcurrentUser().getId());
						new Save().execute();
					} catch (CloudException e) {
						e.printStackTrace();
					} catch (Exception e){
						e.printStackTrace();
					}
				} else {
					Toast.makeText(this, getString(R.string.field_mandatory), Toast.LENGTH_LONG).show();
				}
				break;
		}
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

	private class Save extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			try{
				if (tripObjectId != null) {
					trip.getTrip().save(new CloudObjectCallback() {
						@Override
						public void done(CloudObject tripSaved, CloudException t) throws CloudException {
							trip = new Trip(tripSaved);
							app.setCurrentTrip(trip);
						}
					});
					return 2;
				} else {
					trip.getTrip().save(new CloudObjectCallback() {
						@Override
						public void done(CloudObject tripSaved, CloudException e) {
							if(tripSaved!=null){
								trip = new Trip(tripSaved);
								TripMate tripMate = new TripMate();
								try{
									tripMate.setUserId(CloudUser.getcurrentUser().getId());
									tripMate.setUserName(CloudUser.getcurrentUser().getString(Constants.USERNAME));
									tripMate.setOrganizer(true);
									tripMate.setTripId(trip.getId());
									tripMate.getTripMate().save(new CloudObjectCallback() {
										@Override
										public void done(CloudObject tripMateSaved, CloudException e) throws CloudException {
											if(tripMateSaved!=null){
												TripMate tripMate = new TripMate(tripMateSaved);
												ArrayList<TripMate> tripMateList = new ArrayList<TripMate>();
												tripMateList.add(tripMate);
												trip.setTripMateList(tripMateList);
												app.setCurrentTrip(trip);
												Intent intent = new Intent(TripEditActivity.this, TripActivity.class);
												intent.putExtra("tripObjectId", trip.getId());
												startActivity(intent);
												finish();
											}else{
												e.printStackTrace();
											}
										}
									});
								}catch(CloudException e1){
									e1.printStackTrace();
								}
							}else{
								e.printStackTrace();
							}
						}
					});
				}
			} catch (CloudException e) {
				e.printStackTrace();
				return 1;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result==2){
				onBackPressed();
			}
		}
	}


}
