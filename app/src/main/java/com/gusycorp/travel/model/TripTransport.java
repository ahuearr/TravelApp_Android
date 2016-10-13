package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

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

public class TripTransport extends ITObject {

	private static String TABLENAME = Constants.TAG_TRIPTRANSPORTMODEL;

	public TripTransport(){
		super(TABLENAME);
	}
	public String getDateFrom() {
		return getString(Constants.DATEFROM);
	}

	public String getDateTo() {
		return getString(Constants.DATETO);
	}

	public Date getDateFromDate() throws ParseException {
		return getDate(getDateFrom());
	}

	public Date getDateToDate() throws ParseException {
		return getDate(getDateTo());
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
		return (TypeTransport) getCloudObject(Constants.TYPETRANSPORT);
	}

	public Double getLatitudeFrom() { return getDouble(Constants.LATITUDEFROM);}

	public Double getLongtiudeFrom() { return getDouble(Constants.LONGITUDEFROM);}

	public Double getLatitudeTo() { return getDouble(Constants.LATITUDETO);}

	public Double getLongitudeTo() { return getDouble(Constants.LONGITUDETO);}

	public List<TripMatePrize> getTripMatePrizeList(){
		Object[] objectArray = getArray(Constants.TRIPMATEPRIZE);
		TripMatePrize[] array = Arrays.copyOf(objectArray, objectArray.length, TripMatePrize[].class);
		return Arrays.asList(array);
	}

	public static void findTripTransportInBackground(String objectId, final ITObjectCallback<TripTransport> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new ITObjectCallback<TripTransport>(){
			@Override
			public void done(TripTransport tripTransport, CloudException e) {
				if(tripTransport != null){
					callback.done(tripTransport, null);
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

	public static void findTripTransportListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<TripTransport> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<TripTransport>() {
			@Override
			public void done(TripTransport[] tripTransportList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(tripTransportList, null);
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

}
