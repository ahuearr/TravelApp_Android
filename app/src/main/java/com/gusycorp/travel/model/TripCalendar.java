package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudQuery;

public class TripCalendar extends ITObject implements Comparable<TripCalendar> {

	private static String TABLENAME = Constants.TAG_TRIPCALENDARMODEL;

	public TripCalendar(){
		super(TABLENAME);
	}
	public String getDate() {
		return getString(Constants.DATE);
	}

	public DateTime getDateDate() throws ParseException {
		return getDate(getDate());
	}
	public String getActivity() {
		return getString(Constants.ACTIVITY);
	}

	public String getPlace() {
		return getString(Constants.PLACE);
	}

	public String getCity() {
		return getString(Constants.CITY);
	}

	public Double getPrize() { return getDouble(Constants.PRIZE);}

	public Boolean isActivity() { return getBoolean(Constants.ISACTIVITY); }

	public Double getLatitude() { return getDouble(Constants.LATITUDE);}

	public Double getLongitude() { return getDouble(Constants.LONGITUDE);}

	public List<TripMatePrize> getTripMatePrizeList(){
		Object[] objectArray = getArray(Constants.TRIPMATEPRIZE);
		TripMatePrize[] array = Arrays.copyOf(objectArray, objectArray.length, TripMatePrize[].class);
		return Arrays.asList(array);
	}


	public static void findTripCalendarInBackground(String objectId, final ITObjectCallback<TripCalendar> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new ITObjectCallback<TripCalendar>(){
			@Override
			public void done(TripCalendar tripCalendar, CloudException e) throws CloudException {
				if(tripCalendar != null){
					callback.done(tripCalendar, null);
				} else {
					callback.done(null,e);
				}
			}
			@Override
			public void done(CloudObject obj, CloudException e) throws CloudException {
				if(obj != null){
					callback.done(obj, null);
				} else {
					callback.done(null,e);
				}
			}
		});

	}

	public static void findTripCalendarListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<TripCalendar> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<TripCalendar>() {
			@Override
			public void done(TripCalendar[] tripCalendarList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(tripCalendarList, null);
				} else {
					callback.done(null, e);
				}
			}

			@Override
			public void done(CloudObject[] obj, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(obj, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

	@Override
	public int compareTo(TripCalendar another) {
		try {
			return this.getDateDate().compareTo(another.getDateDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
