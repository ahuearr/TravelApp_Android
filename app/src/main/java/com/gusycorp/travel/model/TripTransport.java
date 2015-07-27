package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParseClassName("TripTransport")
public class TripTransport extends ParseObject {

	private static String TAG = Constants.TAG_TRIPTRANSPORTMODEL;
	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

	public String getObjectId() { return getString(Constants.OBJECTID);	}

	public String getObjectIdTrip() { return getString(Constants.TRIPTRANSPORT_OBJECTIDTRIP);	}

	public String getDateFrom() {
		if(getDate(Constants.TRIPTRANSPORT_DATEFROM)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIPTRANSPORT_DATEFROM));
	}

	public String getDateTo() {

		if(getDate(Constants.TRIPTRANSPORT_DATETO)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIPTRANSPORT_DATETO));
	}

	public String getFrom() {
		return getString(Constants.TRIPTRANSPORT_FROM);
	}

	public String getTo() {
		return getString(Constants.TRIPTRANSPORT_TO);
	}

	public Double getPrize() { return getDouble(Constants.TRIPTRANSPORT_PRIZE);}

	public String getLocator() { return getString(Constants.TRIPTRANSPORT_LOCATOR);}

	public static void findTripTransportInBackground(String objectId,
			final GetCallback<TripTransport> callback) {
		ParseQuery<TripTransport> TripTransportQuery = ParseQuery.getQuery(TripTransport.class);
		TripTransportQuery.whereEqualTo(Constants.OBJECTID, objectId);
		TripTransportQuery.getFirstInBackground(new GetCallback<TripTransport>() {
			@Override
			public void done(TripTransport tripTransport, ParseException e) {

				if (e == null) {
					callback.done(tripTransport, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

	public static void findTripTransportListByFieldsInBackground(
			Map<String, Object> filter, final FindCallback<TripTransport> callback) {
		ParseQuery<TripTransport> TripTransportQuery = ParseQuery.getQuery(TripTransport.class);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			TripTransportQuery.whereEqualTo((String) e.getKey(), e.getValue());
		}
		TripTransportQuery.findInBackground(new FindCallback<TripTransport>() {
			@Override
			public void done(List<TripTransport> tripTransportList, ParseException e) {
				if (e == null) {
					callback.done(tripTransportList, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}
}
