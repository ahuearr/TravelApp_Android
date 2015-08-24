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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParseClassName("TripTransport")
public class TripTransport extends ParseObject {

	private static String TAG = Constants.TAG_TRIPTRANSPORTMODEL;
	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	public String getDateFrom() {
		if(getDate(Constants.DATEFROM)==null){
			return null;
		}
		return df.format(getDate(Constants.DATEFROM));
	}

	public String getDateTo() {

		if(getDate(Constants.DATETO)==null){
			return null;
		}
		return df.format(getDate(Constants.DATETO));
	}

	public Date getDateFromDate() {
		return getDate(Constants.DATEFROM);
	}

	public Date getDateToDate() {

		return getDate(Constants.DATETO);
	}

	public String getFrom() {
		return getString(Constants.FROM);
	}

	public String getTo() {
		return getString(Constants.TO);
	}

	public Double getPrize() { return getDouble(Constants.PRIZE);}

	public String getLocator() { return getString(Constants.LOCATOR);}

	public TypeTransport getTypeTransport(){
		return (TypeTransport) getParseObject(Constants.TYPETRANSPORT);
	}

	public Double getLatitudeFrom() { return getDouble(Constants.LATITUDEFROM);}

	public Double getLongtiudeFrom() { return getDouble(Constants.LONGITUDEFROM);}

	public Double getLatitudeTo() { return getDouble(Constants.LATITUDETO);}

	public Double getLongtiudeTo() { return getDouble(Constants.LONGITUDETO);}

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
