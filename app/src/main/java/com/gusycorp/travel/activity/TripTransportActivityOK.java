package com.gusycorp.travel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.List;


public class TripTransportActivityOK extends MenuActivity {

	Button back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transport_trip_ok);

		back = (Button) findViewById(R.id.back_button);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goTripTransportList();
			}
		});
	}

	@Override
	public void onBackPressed() {
		goTripTransportList();
	}

	private void goTripTransportList(){
		Intent intent = new Intent(this, TripTransportListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra("tripName", tripName);
		startActivity(intent);
		finish();
	}
}
