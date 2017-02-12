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
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class Trip extends ITObject{

	private static String TABLENAME = Constants.TAG_TRIPMODEL;
	private CloudObject trip;
	private ArrayList<TripTransport> tripTransportList;
	private ArrayList<TripAccommodation> tripAccommodationList;
	private ArrayList<TripCalendar> tripCalendarList;
	private ArrayList<TripMate> tripMateList;
	private ArrayList<TripMatePrize> tripMatePrizeList;
	String dateIni;
	String dateFin;

	public Trip() {
		trip = new CloudObject(TABLENAME);
	}

	public Trip(CloudObject trip){
		this.trip = trip;
	}

	public CloudObject getTrip(){
		return trip;
	}

	public void setTrip(CloudObject trip){
		this.trip = trip;
	}

	public String getId() {
		return trip.getId();
	}

	public String getTripName() {
		return trip.getString(Constants.TRIPNAME);
	}

	public void setTripName(String tripName) throws CloudException {
		trip.set(Constants.TRIPNAME, tripName);
	}

	public String getDateIni() throws ParseException {
		String date;
		try{
			date = trip.getString(Constants.DATEINI);
			return dfDate.print(getDate(date));
		}catch (ClassCastException e){
			date = dateIni;
			return date;
		}
	}

	public void setDateIni(String dateIni){
		this.dateIni = dateIni;
	}
	public void setDateIni(DateTime dateIni) throws CloudException {
		trip.set(Constants.DATEINI, dateIni);
	}

	public String getDateFin() throws ParseException {
		String date;
		try{
			date = trip.getString(Constants.DATEFIN);
			return dfDate.print(getDate(date));
		}catch (ClassCastException e){
			date = dateFin;
			return date;
		}
	}

	public void setDateFin(String dateFin){
		this.dateFin = dateFin;
	}
	public void setDateFin(DateTime dateFin) throws CloudException {
		trip.set(Constants.DATEFIN, dateFin);
	}

	public DateTime getDateIniDate() throws ParseException {
        return dfDate.parseDateTime(getDateIni());
	}

	public DateTime getDateFinDate() throws ParseException {
        return dfDate.parseDateTime(getDateFin());
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

	public ArrayList<TripAccommodation> getTripAccommodationList(){
		return tripAccommodationList;
	}

	public void setTripAccommodationList (ArrayList<TripAccommodation> tripAccommodationList) {
		this.tripAccommodationList = tripAccommodationList;
	}

	public void setTripAccommodationList (CloudObject[] tripAccommodationArray) {
		ArrayList<TripAccommodation> tripAccommodationList = new ArrayList<TripAccommodation>();
		for(int i=0;i<tripAccommodationArray.length;i++){
			tripAccommodationList.add(new TripAccommodation(tripAccommodationArray[i]));
		}
		this.tripAccommodationList = tripAccommodationList;
	}

	public ArrayList<TripTransport> getTripTransportList(){
		return tripTransportList;
	}

	public void setTripTransportList (ArrayList<TripTransport> tripTransportList) {
		this.tripTransportList = tripTransportList;
	}

	public void setTripTransportList (CloudObject[] tripTransportArray) {
		ArrayList<TripTransport> tripTransportList = new ArrayList<TripTransport>();
		for(int i=0;i<tripTransportArray.length;i++){
			tripTransportList.add(new TripTransport(tripTransportArray[i]));
		}
		this.tripTransportList = tripTransportList;
	}

	public ArrayList<TripCalendar> getTripCalendarList(){
		return tripCalendarList;
	}

	public void setTripCalendarList (ArrayList<TripCalendar> tripCalendarList){
		this.tripCalendarList = tripCalendarList;
	}

	public void setTripCalendarList (CloudObject[] tripCalendarArray) {
		ArrayList<TripCalendar> tripCalendarList = new ArrayList<TripCalendar>();
		for(int i=0;i<tripCalendarArray.length;i++){
			tripCalendarList.add(new TripCalendar(tripCalendarArray[i]));
		}
		this.tripCalendarList = tripCalendarList;
	}

	public ArrayList<TripMate> getTripMateList(){
		return tripMateList;
	}

	public void setTripMateList (ArrayList<TripMate> tripMateList) {
		this.tripMateList = tripMateList;
	}

	public void setTripMateList (CloudObject[] tripMateArray) {
		ArrayList<TripMate> tripMateList = new ArrayList<TripMate>();
		for(int i=0;i<tripMateArray.length;i++){
			tripMateList.add(new TripMate(tripMateArray[i]));
		}
		this.tripMateList = tripMateList;
	}

	public ArrayList<TripMatePrize> getTripMatePrizeList(){
		return tripMatePrizeList;
	}

	public void setTripMatePrizeList (ArrayList<TripMatePrize> tripMatePrizeList) {
		this.tripMatePrizeList = tripMatePrizeList;
	}

	public void setTripMatePrizeList (CloudObject[] tripMatePrizeArray) {
		ArrayList<TripMatePrize> tripMatePrizeList = new ArrayList<TripMatePrize>();
		for(int i=0;i<tripMatePrizeArray.length;i++){
			tripMatePrizeList.add(new TripMatePrize(tripMatePrizeArray[i]));
		}
		this.tripMatePrizeList = tripMatePrizeList;
	}

	public ArrayList<String> getTripMateIds(){
		ArrayList<String> tripMateIdsList = new ArrayList<String>();
		for(TripMate tripMate : this.getTripMateList()){
			tripMateIdsList.add(tripMate.getId());
		}
		return tripMateIdsList;
	}

	public ArrayList<String> getTripMatePrizeIds(String parentType, String parentId){
		ArrayList<String> tripMatePrizeIdsList = new ArrayList<String>();
		for(TripMatePrize tripMatePrize : this.getTripMatePrizeList()){
			if(parentType.equals(tripMatePrize.getParentType()) && parentId.equals(tripMatePrize.getParentId())){
				tripMatePrizeIdsList.add(tripMatePrize.getId());
			}
		}
		return tripMatePrizeIdsList;
	}

	public ArrayList<TripMatePrize> getTripMatePrize(String parentType, String parentId){
		ArrayList<TripMatePrize> tripMatePrizeList = new ArrayList<TripMatePrize>();

		for(TripMatePrize tripMatePrize : this.tripMatePrizeList){
			if(tripMatePrize!=null){
				if(parentType.equals(tripMatePrize.getParentType()) && parentId.equals(tripMatePrize.getParentId())){
					tripMatePrizeList.add(tripMatePrize);
				}
			}
		}
		return tripMatePrizeList;
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
