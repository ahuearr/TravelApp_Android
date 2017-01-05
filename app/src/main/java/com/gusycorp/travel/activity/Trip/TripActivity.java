package com.gusycorp.travel.activity.Trip;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.MenuActivity;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.ITObjectCallback;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;


public class TripActivity extends MenuActivity {

	private ImageView edit;
	private TextView tripNameText;
	private TextView dateIniText;
	private TextView dateFinText;
	private TextView destinyNameText;

	private TravelApplication app;

	String tripObjectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);

		app = (TravelApplication) getApplication();

		if(menus!=null)		menus.clear();

		edit = (ImageView) findViewById(R.id.edit_trip);
		tripNameText = (TextView) findViewById(R.id.text_trip_name);
		dateIniText = (TextView) findViewById(R.id.text_date_depart);
		dateFinText = (TextView) findViewById(R.id.text_date_arrival);
		destinyNameText = (TextView) findViewById(R.id.text_destiny_name);

		Bundle extras = getIntent().getExtras();
		tripObjectId = extras.getString("tripObjectId");

		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TripActivity.this, TripEditActivity.class);
				intent.putExtra("tripObjectId", tripObjectId);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetTrip().execute();
	}

	private class GetTrip extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			try{
				Trip.findTripInBackground(tripObjectId, new ITObjectCallback<Trip>() {
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {

					}

					@Override
					public void done(Trip tripFind, CloudException e) {
						app.setCurrentTrip(tripFind);
						if(CloudUser.getcurrentUser().getId().equals(tripFind.getOrganizerId())){
							app.setIsOrganizer(true);
							edit.setVisibility(View.VISIBLE);
						}else{
							app.setIsOrganizer(false);
						}
						tripNameText.setText(tripFind.getTripName());
						tripName = tripFind.getTripName();
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
				});
			}catch (CloudException e){
				e.printStackTrace();
				return 1;
			}
			return 0;
		}

	}

}
