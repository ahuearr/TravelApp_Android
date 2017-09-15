package com.gusycorp.travel.activity.Trip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.util.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
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

		avi= (AVLoadingIndicatorView) findViewById(R.id.loader);

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
			getTrip();
			return 0;
		}

		@Override
		protected void onPostExecute(Integer integer) {
			Trip tripFind = app.getCurrentTrip();
			try{
				if(tripFind!=null){
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
			}catch(ParseException e){
				e.printStackTrace();
			}
			hideLoader();
		}
	}

	private void getTrip(){
		CloudQuery query = new CloudQuery(Constants.TAG_TRIPMODEL);
		final Trip[] trip = new Trip[1];
		try{
			query.findById(tripObjectId, new CloudObjectCallback() {
				@Override
				public void done(CloudObject tripRetrieved, CloudException e) throws CloudException {
					if(tripRetrieved!=null){
						trip[0] = new Trip(tripRetrieved);
						CloudQuery query = new CloudQuery(Constants.TAG_TRIPTRANSPORTMODEL);
						query.equalTo(Constants.TRIPID, tripObjectId);
						query.find(new CloudObjectArrayCallback() {
							@Override
							public void done(CloudObject[] tripTransportArrayRetrieved, CloudException e) throws CloudException {
								if(tripTransportArrayRetrieved!=null){
									trip[0].setTripTransportList(tripTransportArrayRetrieved);
									CloudQuery query = new CloudQuery(Constants.TAG_TRIPACCOMMODATIONMODEL);
									query.equalTo(Constants.TRIPID, tripObjectId);
									query.find(new CloudObjectArrayCallback() {
										@Override
										public void done(CloudObject[] tripAccommodationArrayRetrieved, CloudException e) throws CloudException {
											if(tripAccommodationArrayRetrieved!=null){
												trip[0].setTripAccommodationList(tripAccommodationArrayRetrieved);
												CloudQuery query = new CloudQuery(Constants.TAG_TRIPCALENDARMODEL);
												query.equalTo(Constants.TRIPID, tripObjectId);
												query.find(new CloudObjectArrayCallback() {
													@Override
													public void done(CloudObject[] tripCalendarArrayRetrieved, CloudException e) throws CloudException {
														if(tripCalendarArrayRetrieved!=null){
															trip[0].setTripCalendarList(tripCalendarArrayRetrieved);
															CloudQuery query = new CloudQuery(Constants.TAG_TRIPMATEMODEL);
															query.equalTo(Constants.TRIPID, tripObjectId);
															query.find(new CloudObjectArrayCallback() {
																@Override
																public void done(CloudObject[] tripMateArrayRetrieved, CloudException e) throws CloudException {
																	if(tripMateArrayRetrieved!=null){
																		trip[0].setTripMateList(tripMateArrayRetrieved);
																		final HashMap<String,String> mapTripMate = new HashMap<String, String>();
																		for(TripMate tripMate : trip[0].getTripMateList()){
																			mapTripMate.put(tripMate.getId(), tripMate.getUsername());
																		}
																		CloudQuery query = new CloudQuery(Constants.TAG_TRIPMATEPRIZEMODEL);
																		query.equalTo(Constants.TRIPID, tripObjectId);
																		query.find(new CloudObjectArrayCallback() {
																			@Override
																			public void done(CloudObject[] tripMatePrizeArrayRetrieved, CloudException e) throws CloudException {
																				if(tripMatePrizeArrayRetrieved!=null){
																					trip[0].setTripMatePrizeList(tripMatePrizeArrayRetrieved);
																					ArrayList<TripMatePrize> newTripMatePrizeList = new ArrayList<TripMatePrize>();
																					for(TripMatePrize tripMatePrize : trip[0].getTripMatePrizeList()){
																						tripMatePrize.setMateUsername(mapTripMate.get(tripMatePrize.getTripMateId()));
																						newTripMatePrizeList.add(tripMatePrize);
																					}
																					trip[0].setTripMatePrizeList(newTripMatePrizeList);
																					app.setCurrentTrip(trip[0]);
																				}else{
																					e.printStackTrace();
																				}
																			}
																		});
																	}else{
																		e.printStackTrace();
																	}
																}
															});
														}else{
															e.printStackTrace();
														}
													}
												});
											}else{
												e.printStackTrace();
											}
										}
									});
								}else{
									e.printStackTrace();
								}
							}
						});
					}else{
						e.printStackTrace();
					}
				}
			});
		}catch (CloudException e1){
			e1.printStackTrace();
		}
	}
}
