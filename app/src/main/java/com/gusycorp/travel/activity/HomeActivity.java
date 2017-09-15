package com.gusycorp.travel.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.Login.TripLoginActivity;
import com.gusycorp.travel.activity.Trip.TripActivity;
import com.gusycorp.travel.activity.Trip.TripEditActivity;
import com.gusycorp.travel.adapter.ListTripAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

public class HomeActivity extends LoaderListActivity {

	CloudUser currentUser;
	TripMate currentTripMate;

	private ListTripAdapter mAdapter;

	private String userObjectId;

	private Button add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		avi= (AVLoadingIndicatorView) findViewById(R.id.loader);

		currentUser = CloudUser.getcurrentUser();
		userObjectId = currentUser.getId();

		add = (Button) findViewById(R.id.add_transport_trip);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, TripEditActivity.class);
				startActivity(intent);
			}
		});
		TravelApplication app = (TravelApplication) getApplication();
		app.setCurrentTrip(new Trip());

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		showLoader();
		getTrips();
	}

	private void getTrips() {
		mAdapter = new ListTripAdapter(HomeActivity.this,
				R.layout.row_list_trip, new ArrayList<Trip>());
		new Find().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		super.onListItemClick(l, v, position, id);
		Trip trip = (Trip) l.getAdapter().getItem(position);
		if (trip != null) {
			Intent intent = new Intent(this, TripActivity.class);
			intent.putExtra("tripObjectId", trip.getId());
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed()
	{
		showLoader();
		new LogOut().execute();
	}

	private class LogOut extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			if (currentUser != null) {
				try {
					currentUser.logOut(new CloudUserCallback() {
						@Override
						public void done(CloudUser user, CloudException e) throws CloudException {
						}
					});
				} catch (CloudException e) {
					e.printStackTrace();
					return 1;
				}
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			hideLoader();
			if(result==0){
				Intent in =  new Intent(HomeActivity.this,TripLoginActivity.class);
				startActivity(in);
				finish();
			}
		}

	}

	private class Find extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			CloudQuery queryTripMate = new CloudQuery(Constants.TAG_TRIPMATEMODEL);
			queryTripMate.equalTo(Constants.USERID, userObjectId);

			try {
				final ArrayList<Trip> tripArrayList = new ArrayList<>();
				queryTripMate.find(new CloudObjectArrayCallback() {
					public void done(CloudObject[] tripMateListCloudObject, CloudException e) throws CloudException {
						for(CloudObject tripMateObject: tripMateListCloudObject){
							TripMate tripMate = new TripMate(tripMateObject);
                            CloudQuery queryTrip = new CloudQuery(Constants.TAG_TRIPMODEL);
                            queryTrip.equalTo(Constants.ID, tripMate.getTripId());
                            queryTrip.find(new CloudObjectArrayCallback() {
                                public void done(CloudObject[] tripListCloudObject, CloudException e) {
                                    for (CloudObject tripCloudObject : tripListCloudObject) {
                                        Trip trip = new Trip(tripCloudObject);
										tripArrayList.add(trip);
                                    }
                                }
                            });
						}
					}
				});
				mAdapter.addSectionHeaderItem(getString(R.string.future_trips));
				for(Trip trip : tripArrayList){
					if (Constants.VALUE_STATUS_FUTURE.equals(trip
							.getStatus())) {
						mAdapter.addItem(trip);
					}
				}
				mAdapter.addSectionHeaderItem(getString(R.string.past_trips));
				for(Trip trip : tripArrayList){
					if (Constants.VALUE_STATUS_PAST.equals(trip
							.getStatus())) {
						mAdapter.addItem(trip);
					}
				}
			} catch (CloudException e) {
				e.printStackTrace();
				return 1;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			hideLoader();
			if(result==0){
				setListAdapter(mAdapter);
			}
		}

	}
}
