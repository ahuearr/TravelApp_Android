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

public class TripAccommodation extends ITObject {

	private static String TABLENAME = Constants.TAG_TRIPACCOMMODATIONMODEL;
	private CloudObject tripAccommodation;
	private String dateFrom;
	private String dateTo;

	public TripAccommodation() {
		tripAccommodation = new CloudObject(TABLENAME);
	}

	public TripAccommodation(CloudObject tripAccommodation){
		this.tripAccommodation = tripAccommodation;
	}

	public CloudObject getTripAccommodation(){
		return tripAccommodation;
	}

	public void setTripAccommodation(CloudObject tripAccommodation){
		this.tripAccommodation = tripAccommodation;
	}

	public String getId() {
		return tripAccommodation.getId();
	}

	public String getDateFrom() throws ParseException {
		String date;
		try{
			date = tripAccommodation.getString(Constants.DATEFROM);
			return dfDate.print(getDate(date));
		}catch (ClassCastException e){
			date = dateFrom;
			return date;
		}
	}

	public void setDateFrom(String dateFrom){
		this.dateFrom = dateFrom;
	}
	public void setDateFrom(DateTime dateFrom) throws CloudException {
		tripAccommodation.set(Constants.DATEFROM, dateFrom);
	}

	public String getDateTo() throws ParseException {
		String date;
		try{
			date = tripAccommodation.getString(Constants.DATETO);
			return dfDate.print(getDate(date));
		}catch (ClassCastException e){
			date = dateTo;
			return date;
		}
	}

	public void setDateTo(String dateTo){
		this.dateTo=dateTo;
	}
	public void setDateTo(DateTime dateTo) throws CloudException {
		tripAccommodation.set(Constants.DATETO, dateTo);
	}

	public DateTime getDateFromDate() throws ParseException {
		return dfDate.parseDateTime(getDateFrom());
	}

	public DateTime getDateToDate() throws ParseException {
		return dfDate.parseDateTime(getDateTo());
	}

	public String getPlace() {
		return tripAccommodation.getString(Constants.PLACE);
	}

	public void setPlace(String place) throws CloudException {
		tripAccommodation.set(Constants.PLACE, place);
	}

	public String getCity() {
		return tripAccommodation.getString(Constants.CITY);
	}

	public void setCity(String city) throws CloudException {
		tripAccommodation.set(Constants.CITY, city);
	}

	public String getAddress() {
		return tripAccommodation.getString(Constants.ADDRESS);
	}

	public void setAddress(String address) throws CloudException {
		tripAccommodation.set(Constants.ADDRESS, address);
	}

	public Integer getNumRooms() {
		return tripAccommodation.getInteger(Constants.NUMROOMS);
	}

	public void setNumRooms(Integer numRooms) throws CloudException {
		tripAccommodation.set(Constants.NUMROOMS, numRooms);
	}

	public Double getPrize() {
		try{
			return tripAccommodation.getDouble(Constants.PRIZE);
		}catch (ClassCastException e){
			return (double)tripAccommodation.getInteger(Constants.PRIZE);
		}
	}

	public void setPrize(Double prize) throws CloudException {
		tripAccommodation.set(Constants.PRIZE, prize);
	}

	public Double getLatitude() {
		try{
			return tripAccommodation.getDouble(Constants.LATITUDE);
		}catch (ClassCastException e){
			return (double)tripAccommodation.getInteger(Constants.LATITUDE);
		}
	}

	public void setLatitude(Double latitude) throws CloudException {
		tripAccommodation.set(Constants.LATITUDE, latitude);
	}

	public Double getLongitude() {
		try{
			return tripAccommodation.getDouble(Constants.LONGITUDE);
		}catch (ClassCastException e){
			return (double)tripAccommodation.getInteger(Constants.LONGITUDE);
		}
	}

	public void setLongitude(Double longitude) throws CloudException {
		tripAccommodation.set(Constants.LONGITUDE, longitude);
	}

	public String getTripId(){
		return tripAccommodation.getString(Constants.TRIPID);
	}

	public void setTripId(String tripId) throws CloudException {
		tripAccommodation.set(Constants.TRIPID, tripId);
	}

	public List<TripMatePrize> getTripMatePrizeList(){
		Object[] objectArray = tripAccommodation.getArray(Constants.TRIPMATEPRIZE);
		TripMatePrize[] array = Arrays.copyOf(objectArray, objectArray.length, TripMatePrize[].class);
		return Arrays.asList(array);
	}

	public void setTripMatePrizeList (List<TripMatePrize> tripMatePrizeList) throws CloudException {
		CloudObject[] tripMatePrizeListObjects = new CloudObject[tripMatePrizeList.size()];
		tripMatePrizeList.toArray(tripMatePrizeListObjects);
		tripAccommodation.set(Constants.TRIPMATEPRIZE, tripMatePrizeList);
	}

	public static void findTripAccommodationInBackground(String objectId, final CloudObjectCallback callback) throws CloudException {
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
	public static void findTripAccommodationListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<TripAccommodation> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<TripAccommodation>() {
			@Override
			public void done(TripAccommodation[] tripAccommodationList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(tripAccommodationList, null);
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

}
