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

@ParseClassName("TypeTransport")
public class TypeTransport extends ParseObject {

	private static String TAG = Constants.TAG_TRIPMODEL;

	public String getTransportName() {
		return getString(Constants.TYPETRANSPORT_TRANSPORTNAME);
	}
	public String getTransportImageName() {
		return getString(Constants.TYPETRANSPORT_TRANSPORTIMAGENAME);
	}

	public static void findTripInBackground(String objectId,
			final GetCallback<TypeTransport> callback) {
		ParseQuery<TypeTransport> TypeTransportQuery = ParseQuery.getQuery(TypeTransport.class);
		TypeTransportQuery.whereEqualTo(Constants.OBJECTID, objectId);
		TypeTransportQuery.getFirstInBackground(new GetCallback<TypeTransport>() {
			@Override
			public void done(TypeTransport trip, ParseException e) {
				if (e == null) {
					callback.done(trip, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

	public static void findTripListByFieldsInBackground(
			Map<String, Object> filter, final FindCallback<TypeTransport> callback) {
		ParseQuery<TypeTransport> TypeTransportQuery = ParseQuery.getQuery(TypeTransport.class);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			TypeTransportQuery.whereEqualTo((String) e.getKey(), e.getValue());
		}
		TypeTransportQuery.findInBackground(new FindCallback<TypeTransport>() {
			@Override
			public void done(List<TypeTransport> tripList, ParseException e) {
				if (e == null) {
					callback.done(tripList, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}
}
