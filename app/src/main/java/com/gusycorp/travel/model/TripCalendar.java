package com.gusycorp.travel.model;

import android.util.Log;

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
	String dateC;

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

	public String getDateC() throws ParseException {
		String dateC;
		try{
			dateC = tripCalendar.getString(Constants.DATE);
			return dfTime.print(getDate(dateC));
		}catch (ClassCastException e){
			dateC = this.dateC;
            String dateWithHour = dateC;
            dateWithHour = dateWithHour.split(" ")[0];
            if(dateC.equals(dateWithHour)){
                dateC=dateC+" 00:00";
            }
			return dateC;
		}
	}

	public void setDate(String dateC){ this.dateC=dateC;}
	public void setDate(DateTime date) throws CloudException {
		tripCalendar.set(Constants.DATE, date);
	}

	public DateTime getDateDate() throws ParseException {
		return dfTime.parseDateTime(getDateC());
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
		try{
			return tripCalendar.getDouble(Constants.PRIZE);
		}catch (ClassCastException e){
			return (double)tripCalendar.getInteger(Constants.PRIZE);
		}
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
		try{
			return tripCalendar.getDouble(Constants.LATITUDE);
		}catch (ClassCastException e){
			return (double)tripCalendar.getInteger(Constants.LATITUDE);
		}
	}

	public void setLatitude(Double latitude) throws CloudException {
		tripCalendar.set(Constants.LATITUDE, latitude);
	}

	public Double getLongitude() {
		try{
			return tripCalendar.getDouble(Constants.LONGITUDE);
		}catch (ClassCastException e){
			return (double)tripCalendar.getInteger(Constants.LONGITUDE);
		}
	}

	public void setLongitude(Double longitude) throws CloudException {
		tripCalendar.set(Constants.LONGITUDE, longitude);
	}

	public List<TripMatePrize> getTripMatePrizeList(){
		Object[] objectArray = tripCalendar.getArray(Constants.TRIPMATEPRIZE);
		TripMatePrize[] array = Arrays.copyOf(objectArray, objectArray.length, TripMatePrize[].class);
		return Arrays.asList(array);
	}

	public String getTripId(){
		return tripCalendar.getString(Constants.TRIPID);
	}

	public void setTripId(String tripId) throws CloudException {
		tripCalendar.set(Constants.TRIPID, tripId);
	}

	public void setTripMatePrizeList (List<TripMatePrize> tripMatePrizeList) throws CloudException {
		CloudObject[] tripMatePrizeListObjects = new CloudObject[tripMatePrizeList.size()];
		tripMatePrizeList.toArray(tripMatePrizeListObjects);
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
