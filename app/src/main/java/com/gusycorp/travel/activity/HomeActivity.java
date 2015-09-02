package com.gusycorp.travel.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.adapter.ListTripAdapter;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomeActivity extends ListActivity {

	ParseUser currentUser;
	TripMate currentTripMate;

	private ListTripAdapter mAdapter;

	private String userObjectId;

	private Button add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		currentUser = ParseUser.getCurrentUser();
		userObjectId = currentUser.getObjectId();

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
		List<ParseQuery<Trip>> parseQueryList = new ArrayList<>();
		ParseQuery<Trip> queryTripMates = ParseQuery.getQuery(Constants.TAG_TRIPMODEL);
		queryTripMates.whereEqualTo(Constants.USER, currentUser);
		parseQueryList.add(queryTripMates);
		ParseQuery<Trip> queryTripOrganizer = ParseQuery.getQuery(Constants.TAG_TRIPMODEL);
		queryTripOrganizer.whereEqualTo(Constants.ORGANIZERID, userObjectId);
		parseQueryList.add(queryTripOrganizer);

		ParseQuery<Trip> mainQuery = ParseQuery.or(parseQueryList);
		mainQuery.findInBackground( new FindCallback<Trip>() {
			public void done(List<Trip> tripList, ParseException e) {
				mAdapter.addSectionHeaderItem(getString(R.string.future_trips));
				for (Trip trip : tripList) {
					if (Constants.VALUE_STATUS_FUTURE.equals(trip
							.getStatus())) {
						mAdapter.addItem(trip);
					}
				}
				mAdapter.addSectionHeaderItem(getString(R.string.past_trips));
				for (Trip trip : tripList) {
					if (Constants.VALUE_STATUS_PAST.equals(trip
							.getStatus())) {
						mAdapter.addItem(trip);
					}
				}
				setListAdapter(mAdapter);
			}
		});
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
			intent.putExtra("tripObjectId", trip.getObjectId());
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed()
	{
		if (currentUser != null) {
			currentUser.logOut();
			Intent in =  new Intent(HomeActivity.this,TripLoginActivity.class);
			startActivity(in);
			finish();
		}
	}
}
