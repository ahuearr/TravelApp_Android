package com.gusycorp.travel.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gusycorp.travel.util.Constants;

import org.joda.time.DateTime;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class Trip extends ITObject{

	private static String TABLENAME = Constants.TAG_TRIPMODEL;
	private CloudObject trip;

	public Trip() {
		trip = new CloudObject(TABLENAME);
	}

	public String getTripName() {
		return trip.getString(Constants.TRIPNAME);
	}

	public void setTripName(String tripName) throws CloudException {
		trip.set(Constants.TRIPNAME, tripName);
	}

	public String getDateIni() {
		return trip.getString(Constants.DATEINI);
	}

	public void setDateIni(DateTime dateIni) throws CloudException {
		trip.set(Constants.DATEINI, dateIni);
	}

	public String getDateFin() {
		return trip.getString(Constants.DATEFIN);
	}

	public void setDateFin(DateTime dateFin) throws CloudException {
		trip.set(Constants.DATEFIN, dateFin);
	}

	public DateTime getDateIniDate() throws ParseException {
		return getDate(getDateIni());
	}

	public DateTime getDateFinDate() throws ParseException {
		return getDate(getDateFin());
	}

	public List<String> getDestinyName() {
		return getListString(Constants.DESTINYNAME, trip);
	}

	public void setDestinyName(List<String> destinyName) throws CloudException {
		trip.set(Constants.DESTINYNAME, destinyName);
	}

	public String getStatus() {
		return trip.getString(Constants.STATUS);
	}

	public void setStatus(String status) throws CloudException {
		trip.set(Constants.STATUS, status);
	}

	public String getOrganizerId() {
		return trip.getString(Constants.ORGANIZERID);
	}

	public void setOrganizerId(String organizerId) throws CloudException {
		trip.set(Constants.ORGANIZERID, organizerId);
	}

	//TODO Set all lists of CloudObjects
	public List<TripAccommodation> getTripAccommodationList(){
		Object[] objectArray = trip.getArray(Constants.TRIPACCOMMODATION);
		TripAccommodation[] array = Arrays.copyOf(objectArray, objectArray.length, TripAccommodation[].class);
		return Arrays.asList(array);
	}

	public List<TripTransport> getTripTransportList(){
		Object[] objectArray = trip.getArray(Constants.TRIPTRANSPORT);
		TripTransport[] array = Arrays.copyOf(objectArray, objectArray.length, TripTransport[].class);
		return Arrays.asList(array);
	}

	public List<TripCalendar> getTripCalendarList(){
		Object[] objectArray = trip.getArray(Constants.TRIPCALENDAR);
		TripCalendar[] array = Arrays.copyOf(objectArray, objectArray.length, TripCalendar[].class);
		return Arrays.asList(array);
	}

	public List<TripMate> getTripMateList(){
		Object[] objectArray = trip.getArray(Constants.TRIPMATE);
		TripMate[] array = Arrays.copyOf(objectArray, objectArray.length, TripMate[].class);
		return Arrays.asList(array);
	}

	public void findTripInBackground(String objectId) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new CloudObjectCallback(){
			@Override
			public void done(CloudObject obj, CloudException e) throws CloudException {
				if(obj != null){
					trip = obj;
				} else {
					e.printStackTrace();
				}
			}
		});

	}

	public void save() throws CloudException {
		trip.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject obj, CloudException e) throws CloudException {
				if(obj != null){
					trip = obj;
				} else {
					e.printStackTrace();
				}
			}
		});
	}

/*
	public static void findTripListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<Trip> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<Trip>() {
			@Override
			public void done(Trip[] tripList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(tripList, null);
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
