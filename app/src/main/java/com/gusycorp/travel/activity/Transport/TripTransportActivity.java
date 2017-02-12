package com.gusycorp.travel.activity.Transport;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.activity.Trip.TripActivity;
import com.gusycorp.travel.activity.Trip.TripEditActivity;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudUser;


public class TripTransportActivity extends MenuActivity implements OnClickListener{

	private Spinner typeTransport;
	private EditText dateDepart;
	private EditText dateArrival;
	private EditText cityDepart;
	private EditText cityArrival;
	private EditText prize;
	private EditText locator;
	private Button save;
	private ImageButton tripMates;

	private TripTransport tripTransport = new TripTransport();
	private String objectId;

	private Trip currentTrip;


	List<TypeTransport> typeTransportList;

	TravelApplication app;

	private DateTimeFormatter df = DateTimeFormat.forPattern(Constants.DATE_MASK);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transport_trip);

		app = (TravelApplication) getApplication();
		typeTransportList = app.getTransportTypes();
		currentTrip = app.getCurrentTrip();

		typeTransport = (Spinner) findViewById(R.id.type_transport);
		dateDepart = (EditText) findViewById(R.id.date_depart);
		dateArrival = (EditText) findViewById(R.id.date_arrival);
		cityDepart = (EditText) findViewById(R.id.city_depart);
		cityArrival = (EditText) findViewById(R.id.city_arrival);
		prize = (EditText) findViewById(R.id.prize);
		locator = (EditText) findViewById(R.id.locator);
		save = (Button) findViewById(R.id.save_button);
		tripMates = (ImageButton) findViewById(R.id.transport_trip_mates);

		if(!app.isOrganizer()){
			typeTransport.setEnabled(false);
			dateDepart.setEnabled(false);
			dateArrival.setEnabled(false);
			cityDepart.setEnabled(false);
			cityArrival.setEnabled(false);
			prize.setEnabled(false);
			locator.setEnabled(false);
			typeTransport.setFocusable(false);
			dateDepart.setFocusable(false);
			dateArrival.setFocusable(false);
			cityDepart.setFocusable(false);
			cityArrival.setFocusable(false);
			prize.setFocusable(false);
			locator.setFocusable(false);
			save.setVisibility(View.GONE);
		}

		save.setOnClickListener(this);
		tripMates.setOnClickListener(this);

		final ArrayAdapter<TypeTransport> typeTransportAdapter = new ArrayAdapter<TypeTransport>(getBaseContext(),android.R.layout.simple_spinner_item,typeTransportList);
		typeTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeTransport.setAdapter(typeTransportAdapter);
		Bundle bundle = getIntent().getExtras();

		if(bundle!=null){
			objectId = bundle.getString(Constants.OBJECTID);
			if(objectId !=null){
				tripTransport = app.getCurrentTripTransport();
				dateDepart.setText(bundle.getString(Constants.DATEFROM));
				dateArrival.setText(bundle.getString(Constants.DATETO));
				cityDepart.setText(bundle.getString(Constants.FROM));
				cityArrival.setText(bundle.getString(Constants.TO));
				prize.setText(Double.toString(bundle.getDouble(Constants.PRIZE)));
				locator.setText(bundle.getString(Constants.LOCATOR));

				String typeTransportName = bundle.getString(Constants.TYPETRANSPORT);
				int posTypeTransport = 0;
				if (typeTransportName != null) {
					for (int i = 0; i < typeTransportList.size(); i++) {
						if (typeTransportList.get(i).getTransportName()
								.equals(typeTransportName)) {
							posTypeTransport = i;
						}
					}
				}
				typeTransport.setSelection(posTypeTransport);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.save_button:
				if(checkMandatory()){
					try{
						DateTime date = df.parseDateTime(dateDepart.getText().toString());
						tripTransport.setDateFrom(date);
						tripTransport.setDateFrom(dateDepart.getText().toString());
						date = df.parseDateTime(dateArrival.getText().toString());
						tripTransport.setDateTo(date);
						tripTransport.setDateTo(dateArrival.getText().toString());
						tripTransport.setFrom(cityDepart.getText().toString());
						tripTransport.setTo(cityArrival.getText().toString());
						tripTransport.setPrize(Double.parseDouble(prize.getText().toString()));
						tripTransport.setLocator(locator.getText().toString());
						tripTransport.setTripId(currentTrip.getId());
						final TypeTransport typeTransportSelected = (TypeTransport)typeTransport.getSelectedItem();
						tripTransport.setTypeTransport(typeTransportSelected.getTransportName());

						if(objectId!=null){
							new Save().execute(Constants.UPDATE);
						} else {
							new Save().execute(Constants.SAVE);
						}
					} catch (CloudException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else{
					Toast.makeText(this, getString(R.string.field_mandatory), Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.transport_trip_mates:
				Intent intent = new Intent(TripTransportActivity.this, TripTransportMatesActivity.class);
				startActivity(intent);
				break;
		}
	}

	private class Save extends AsyncTask<Integer, Void, Integer> {

		int saveType;
		@Override
		protected Integer doInBackground(Integer... params) {

			saveType = params[0];

			switch(saveType){
				case 0: //Save
					save();
					return 0;
				case 1:  //Update
					update();
					return 1;
			}
			return 2;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result<=2){
				goOK();
			}
		}
	}

	private void save(){
		try {
			tripTransport.getTripTransport().save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject transportSaved, CloudException t) throws CloudException {
					if(transportSaved!=null){
						TripTransport tripTransport = new TripTransport(transportSaved);
						ArrayList<TripTransport> tripTransportsList = currentTrip.getTripTransportList();
						tripTransportsList.add(tripTransport);
						currentTrip.setTripTransportList(tripTransportsList);
						app.setCurrentTrip(currentTrip);
					}else{
						t.printStackTrace();
					}
				}
			});
		} catch (CloudException e) {
			e.printStackTrace();
		}
	}

	private void update() {
		try {
			tripTransport.getTripTransport().save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject tripTransportSaved, CloudException t) throws CloudException {
				}
			});
		} catch (CloudException e) {
			e.printStackTrace();
		}
	}
	private boolean checkMandatory(){
		if(viewIsEmpty(dateDepart) || viewIsEmpty(dateArrival)
				|| viewIsEmpty(cityDepart) || viewIsEmpty(cityArrival)
				|| viewIsEmpty(prize) || viewIsEmpty(locator)){
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
