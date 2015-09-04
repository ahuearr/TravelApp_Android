package com.gusycorp.travel.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TripTransportActivity extends MenuActivity implements OnClickListener{

	private Spinner typeTransport;
	private EditText dateDepart;
	private EditText dateArrival;
	private EditText cityDepart;
	private EditText cityArrival;
	private EditText prize;
	private EditText locator;
	private Button save;

	private TripTransport tripTransport = new TripTransport();
	private String objectId;

	private Trip currentTrip;


	List<TypeTransport> typeTransportList;

	TravelApplication app;

	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

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
						Date date = df.parse(dateDepart.getText().toString());
						tripTransport.put(Constants.DATEFROM, date);
						date = df.parse(dateArrival.getText().toString());
						tripTransport.put(Constants.DATETO, date);
						tripTransport.put(Constants.FROM, cityDepart.getText().toString());
						tripTransport.put(Constants.TO, cityArrival.getText().toString());
						tripTransport.put(Constants.PRIZE, Double.parseDouble(prize.getText().toString()));
						tripTransport.put(Constants.LOCATOR, locator.getText().toString());

						final TypeTransport typeTransportSelected = (TypeTransport) typeTransport.getSelectedItem();
						tripTransport.put(Constants.TYPETRANSPORT, typeTransportSelected);

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
			tripTransport.save();
			ParseRelation<TripTransport> tripTransportRelation = currentTrip.getRelation(Constants.TRIPTRANSPORT);
			tripTransportRelation.add(tripTransport);
			currentTrip.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		goOK();
	}

	private void update() {
		try {
			tripTransport.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		goOK();
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
