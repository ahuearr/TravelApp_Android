package com.gusycorp.travel.activity;

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

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

public class HomeActivity extends ListActivity {

	CloudUser currentUser;
	TripMate currentTripMate;

	private ListTripAdapter mAdapter;

	private String userObjectId;

	private Button add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

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
		if (currentUser != null) {
			try {
				currentUser.logOut(new CloudUserCallback() {
                    @Override
                    public void done(CloudUser user, CloudException e) throws CloudException {
						Intent in =  new Intent(HomeActivity.this,TripLoginActivity.class);
						startActivity(in);
						finish();
                    }
                });
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
	}

	private class Find extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			CloudQuery queryTrip = new CloudQuery(Constants.TAG_TRIPMODEL);
			queryTrip.equalTo(Constants.USER, currentUser);
			CloudQuery queryTripOrganizer = new CloudQuery(Constants.TAG_TRIPMODEL);
			queryTripOrganizer.equalTo(Constants.ORGANIZERID, userObjectId);

			CloudQuery mainQuery = null;
			try {
				mainQuery = CloudQuery.or(queryTrip, queryTripOrganizer);
				mainQuery.find(new CloudObjectArrayCallback() {
					public void done(CloudObject[] tripListCloudObject, CloudException e) {
						mAdapter.addSectionHeaderItem(getString(R.string.future_trips));
						for (CloudObject tripCloudObject : tripListCloudObject) {
							Trip trip = new Trip(tripCloudObject);
							if (Constants.VALUE_STATUS_FUTURE.equals(trip
									.getStatus())) {
								mAdapter.addItem(trip);
							}
						}
						mAdapter.addSectionHeaderItem(getString(R.string.past_trips));
						for (CloudObject tripCloudObject : tripListCloudObject) {
							Trip trip = new Trip(tripCloudObject);
							if (Constants.VALUE_STATUS_PAST.equals(trip
									.getStatus())) {
								mAdapter.addItem(trip);
							}
						}
					}
				});
			} catch (CloudException e) {
				e.printStackTrace();
				return 1;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result==0){
				setListAdapter(mAdapter);
			}
		}

	}
}
