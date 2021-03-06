package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class TripTransport extends ITObject {

	private static String TABLENAME = Constants.TAG_TRIPTRANSPORTMODEL;
	private CloudObject tripTransport;
	private String dateFrom;
	private String dateTo;

	public TripTransport(){
		tripTransport = new CloudObject(TABLENAME);
	}

	public TripTransport(CloudObject tripTransport){
		this.tripTransport = tripTransport;
	}

	public CloudObject getTripTransport(){
		return tripTransport;
	}

	public void setTripTransport(CloudObject tripTransport){
		this.tripTransport = tripTransport;
	}

	public String getId() {
		return tripTransport.getId();
	}

	public String getDateFrom() throws ParseException {
		String date;
		try{
			date = tripTransport.getString(Constants.DATEFROM);
			return dfTime.print(getDate(date));
		}catch (ClassCastException e){
			date = dateFrom;
			return date;
		}
	}

	public void setDateFrom(String dateFrom){
		this.dateFrom = dateFrom;
	}
	public void setDateFrom(DateTime dateFrom) throws CloudException {
		tripTransport.set(Constants.DATEFROM, dateFrom);
	}

	public String getDateTo() throws ParseException {
		String date;
		try{
			date = tripTransport.getString(Constants.DATETO);
			return dfTime.print(getDate(date));
		}catch (ClassCastException e){
			date = dateTo;
			return date;
		}
	}

	public void setDateTo(String dateTo){
		this.dateTo=dateTo;
	}
	public void setDateTo(DateTime dateTo) throws CloudException {
		tripTransport.set(Constants.DATETO, dateTo);
	}

	public DateTime getDateFromDate() throws ParseException {
		return dfTime.parseDateTime(getDateFrom());
	}

	public DateTime getDateToDate() throws ParseException {
		return dfTime.parseDateTime(getDateTo());
	}

	public String getFrom() {
		return tripTransport.getString(Constants.FROM);
	}

	public void setFrom(String from) throws CloudException {
		tripTransport.set(Constants.FROM, from);
	}

	public String getTo() {
		return tripTransport.getString(Constants.TO);
	}

	public void setTo(String to) throws CloudException {
		tripTransport.set(Constants.TO, to);
	}

	public Double getPrize() {
		try{
			return tripTransport.getDouble(Constants.PRIZE);
		}catch(ClassCastException e){
			return (double) tripTransport.getInteger(Constants.PRIZE);
		}
	}

	public void setPrize(Double prize) throws CloudException {
		tripTransport.set(Constants.PRIZE, prize);
	}

	public String getLocator() {
		return tripTransport.getString(Constants.LOCATOR);
	}

	public void setLocator(String locator) throws CloudException {
		tripTransport.set(Constants.LOCATOR, locator);
	}

	public String getTypeTransport(){
		return tripTransport.getString(Constants.TYPETRANSPORT);
	}

	public void setTypeTransport(String typeTransport) throws CloudException {
		tripTransport.set(Constants.TYPETRANSPORT, typeTransport);
	}

	public Double getLatitudeFrom() {
		try{
			return tripTransport.getDouble(Constants.LATITUDEFROM);
		}catch (ClassCastException e){
			return (double)tripTransport.getInteger(Constants.LATITUDEFROM);
		}
	}

	public void setLatitudeFrom(Double latitudeFrom) throws CloudException {
		tripTransport.set(Constants.LATITUDEFROM, latitudeFrom);
	}

	public Double getLongitudeFrom() {
		try{
			return tripTransport.getDouble(Constants.LONGITUDEFROM);
		}catch (ClassCastException e){
			return (double)tripTransport.getInteger(Constants.LONGITUDEFROM);
		}
	}

	public void setLongitudeFrom(Double longitudeFrom) throws CloudException {
		tripTransport.set(Constants.LONGITUDEFROM, longitudeFrom);
	}

	public Double getLatitudeTo() {
		try{
			return tripTransport.getDouble(Constants.LATITUDETO);
		}catch (ClassCastException e){
			return (double)tripTransport.getInteger(Constants.LATITUDETO);
		}
	}

	public void setLatitudeTo(Double latitudeTo) throws CloudException {
		tripTransport.set(Constants.LATITUDETO, latitudeTo);
	}

	public Double getLongitudeTo() {
		try{
			return tripTransport.getDouble(Constants.LONGITUDETO);
		}catch (ClassCastException e){
			return (double)tripTransport.getInteger(Constants.LONGITUDETO);
		}
	}

	public void setLongitudeTo(Double longitudeTo) throws CloudException {
		tripTransport.set(Constants.LONGITUDETO, longitudeTo);
	}

	public String getTripId(){
		return tripTransport.getString(Constants.TRIPID);
	}

	public void setTripId(String tripId) throws CloudException {
		tripTransport.set(Constants.TRIPID, tripId);
	}

	public List<TripMatePrize> getTripMatePrizeList(){
		Object[] objectArray = tripTransport.getArray(Constants.TRIPMATEPRIZE);
		TripMatePrize[] array = Arrays.copyOf(objectArray, objectArray.length, TripMatePrize[].class);
		return Arrays.asList(array);
	}

	public void setTripMatePrizeList (List<TripMatePrize> tripMatePrizeList) throws CloudException {
		CloudObject[] tripMatePrizeListObjects = new CloudObject[tripMatePrizeList.size()];
		tripMatePrizeList.toArray(tripMatePrizeListObjects);
		tripTransport.set(Constants.TRIPMATEPRIZE, tripMatePrizeListObjects);
	}

	public static void findTripTransportInBackground(String objectId, final CloudObjectCallback callback) throws CloudException {
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
*/

}
