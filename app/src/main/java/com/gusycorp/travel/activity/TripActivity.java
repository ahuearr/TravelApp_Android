package com.gusycorp.travel.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;
import com.parse.GetCallback;
import com.parse.ParseException;

public class TripActivity extends Activity {

	private TextView tripNameText;
	private TextView dateIniLabel;
	private TextView dateIniText;
	private TextView dateFinLabel;
	private TextView dateFinText;
	private TextView destinyNameLabel;
	private TextView destinyNameText;

	private static final String TAG = Constants.TAG_TRIPACTIVITY;
	private String tripObjectId;
	private Trip trip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);

		tripNameText = (TextView) findViewById(R.id.text_trip_name);
		dateIniLabel = (TextView) findViewById(R.id.label_date_ini);
		dateIniText = (TextView) findViewById(R.id.text_date_ini);
		dateFinLabel = (TextView) findViewById(R.id.label_date_fin);
		dateFinText = (TextView) findViewById(R.id.text_date_fin);
		destinyNameLabel = (TextView) findViewById(R.id.label_destiny_name);
		destinyNameText = (TextView) findViewById(R.id.text_destiny_name);

		Bundle extras = getIntent().getExtras();
		tripObjectId = extras.getString("tripObjectId");

		getTrip(tripObjectId);
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
							destinies += destiny + ", ";
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
}
