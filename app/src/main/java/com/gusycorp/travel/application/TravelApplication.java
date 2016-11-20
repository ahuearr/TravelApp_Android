package com.gusycorp.travel.application;

import android.app.Application;

import com.gusycorp.travel.model.ITObjectArrayCallback;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.model.TypeTransport;
import com.gusycorp.travel.util.Constants;
import com.gusycorp.travel.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;

public class TravelApplication extends Application {

	private static final String TAG = Constants.TAG_TRAVELAPPLICACION;
	private List<Integer> menus = new ArrayList<Integer>();
	private List<TypeTransport> transportstype = new ArrayList<TypeTransport>();
	private Trip currentTrip;
	private TripTransport currentTripTransport;
	private TripAccommodation currentTripAccommodation;
	private TripCalendar currentTripCalendar;

	private boolean isOrganizer;

	public void onCreate() {
		getListsSpinners();
	}

	public List<Integer> getMenus() {
		return menus;
	}

	public void setMenus(final List<Integer> menus) {
		this.menus = menus;
	}

	public List<TypeTransport> getTransportTypes() {
		return transportstype;
	}

	public Trip getCurrentTrip(){
		return currentTrip;
	}

	public void setCurrentTrip(Trip currentTrip){
		this.currentTrip=currentTrip;
	}

	public TripTransport getCurrentTripTransport(){
		return currentTripTransport;
	}

	public void setCurrentTripTransport(TripTransport currentTripTransport){
		this.currentTripTransport=currentTripTransport;
	}

	public TripAccommodation getCurrentTripAccommodation(){
		return currentTripAccommodation;
	}

	public void setCurrentTripAccommodation(TripAccommodation currentTripAccommodation){
		this.currentTripAccommodation=currentTripAccommodation;
	}

	public TripCalendar getCurrentTripCalendar(){
		return currentTripCalendar;
	}

	public void setCurrentTripCalendar(TripCalendar currentTripCalendar){
		this.currentTripCalendar=currentTripCalendar;
	}

	public boolean isOrganizer() {
		return isOrganizer;
	}

	public void setIsOrganizer(boolean isOrganizer) {
		this.isOrganizer = isOrganizer;
	}

	void getListsSpinners(){
		HashMap<String, Object> filter = new HashMap();
        try {
            TypeTransport.findTypeTransportListByFieldsInBackground(filter, new ITObjectArrayCallback<TypeTransport>() {
                @Override
                public void done(TypeTransport[] x, CloudException t) throws CloudException {
                    ArrayList<TypeTransport> typeTransportList = new ArrayList<TypeTransport>(Arrays.asList(x));
                    transportstype.addAll(typeTransportList);
                }

                @Override
                public void done(CloudObject[] x, CloudException t) throws CloudException {

                }
            });
        } catch (CloudException e) {
            e.printStackTrace();
        }
    }

}
