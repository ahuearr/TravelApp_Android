package com.gusycorp.travel.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.List;


public class TripTransportActivity extends MenuActivity {

	private Spinner typeTransport;
	private EditText dateDepart;
	private EditText dateArrival;
	private EditText cityDepart;
	private EditText cityArrival;
	private EditText prize;
	private EditText locator;

	private static final String TAG = Constants.TAG_TRIPTRANSPORTACTIVITY;
	private TripTransport tripTransport;
	private String objectId;
	private String objectIdTrip;

	List<TypeTransport> typeTransportList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transport_trip);

		TravelApplication app = (TravelApplication) getApplication();
		typeTransportList = app.getTransportTypes();

		typeTransport = (Spinner) findViewById(R.id.type_transport);
		dateDepart = (EditText) findViewById(R.id.date_depart);
		dateArrival = (EditText) findViewById(R.id.date_arrival);
		cityDepart = (EditText) findViewById(R.id.city_depart);
		cityArrival = (EditText) findViewById(R.id.city_arrival);
		prize = (EditText) findViewById(R.id.prize);
		locator = (EditText) findViewById(R.id.locator);

		final ArrayAdapter<TypeTransport> typeTransportAdapter = new ArrayAdapter<TypeTransport>(getBaseContext(),android.R.layout.simple_spinner_item,typeTransportList);
		typeTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeTransport.setAdapter(typeTransportAdapter);
		Bundle bundle = getIntent().getExtras();

		if(bundle!=null){
			objectId = bundle.getString("objectId");
			if(objectId !=null){
				objectIdTrip = bundle.getString("objectIdTrip");
				dateDepart.setText(bundle.getString("dateFrom"));
				dateArrival.setText(bundle.getString("dateTo"));
				cityDepart.setText(bundle.getString("from"));
				cityArrival.setText(bundle.getString("to"));
				prize.setText(bundle.getString("prize"));
				locator.setText(bundle.getString("locator"));
			}
		}

	}
}
