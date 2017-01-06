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
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class TripCalendar extends ITObject implements Comparable<TripCalendar> {

	private static String TABLENAME = Constants.TAG_TRIPCALENDARMODEL;
	private CloudObject tripCalendar;

	public TripCalendar(){
		tripCalendar = new CloudObject(TABLENAME);
	}

	public TripCalendar(CloudObject tripCalendar){
		this.tripCalendar = tripCalendar;
	}

	public CloudObject getTripCalendar(){
		return tripCalendar;
	}

	public void setTripCalendar(CloudObject tripCalendar){
		this.tripCalendar = tripCalendar;
	}

	public String getId() {
		return tripCalendar.getId();
	}

	public String getDate() {
		return tripCalendar.getString(Constants.DATE);
	}

	public void setDate(DateTime date) throws CloudException {
		tripCalendar.set(Constants.DATE, date);
	}

	public DateTime getDateDate() throws ParseException {
		return getDate(getDate());
	}
	public String getActivity() {
		return tripCalendar.getString(Constants.ACTIVITY);
	}

	public void setActivity(String activity) throws CloudException {
		tripCalendar.set(Constants.ACTIVITY, activity);
	}

	public String getPlace() {
		return tripCalendar.getString(Constants.PLACE);
	}

	public void setPlace(String place) throws CloudException {
		tripCalendar.set(Constants.PLACE, place);
	}

	public String getCity() {
		return tripCalendar.getString(Constants.CITY);
	}

	public void setCity(String city) throws CloudException {
		tripCalendar.set(Constants.CITY, city);
	}

	public Double getPrize() {
		return tripCalendar.getDouble(Constants.PRIZE);
	}

	public void setPrize(Double prize) throws CloudException {
		tripCalendar.set(Constants.PRIZE, prize);
	}

	public Boolean isActivity() {
		return tripCalendar.getBoolean(Constants.ISACTIVITY);
	}

	public void setIsActivity(Boolean isActivity) throws CloudException {
		tripCalendar.set(Constants.ISACTIVITY, isActivity);
	}

	public Double getLatitude() {
		return tripCalendar.getDouble(Constants.LATITUDE);
	}

	public void setLatitude(Double latitude) throws CloudException {
		tripCalendar.set(Constants.LATITUDE, latitude);
	}

	public Double getLongitude() {
		return tripCalendar.getDouble(Constants.LONGITUDE);
	}

	public void setLongitude(Double longitude) throws CloudException {
		tripCalendar.set(Constants.LONGITUDE, longitude);
	}

	public List<TripMatePrize> getTripMatePrizeList(){
		Object[] objectArray = tripCalendar.getArray(Constants.TRIPMATEPRIZE);
		TripMatePrize[] array = Arrays.copyOf(objectArray, objectArray.length, TripMatePrize[].class);
		return Arrays.asList(array);
	}

	public void setTripMatePrizeList (CloudObject[] tripMatePrizeList) throws CloudException {
		tripCalendar.set(Constants.TRIPMATEPRIZE, tripMatePrizeList);
	}

	public static void findTripCalendarInBackground(String objectId, final CloudObjectCallback callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new CloudObjectCallback(){
			@Override
			public void done(CloudObject obj, CloudException e) throws CloudException {
				if(obj != null){
					callback.done(obj, e);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

/*
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
*/

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
