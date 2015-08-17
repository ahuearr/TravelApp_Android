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

@ParseClassName("TripCalendar")
public class TripCalendar extends ParseObject {

	private static String TAG = Constants.TAG_TRIPCALENDARMODEL;
	private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

	public String getDate() {
		if(getDate(Constants.TRIPCALENDAR_DATE)==null){
			return null;
		}
		return df.format(getDate(Constants.TRIPCALENDAR_DATE));
	}

	public Date getDateDate(){
		return getDate(Constants.TRIPCALENDAR_DATE);
	}
	public String getActivity() {
		return getString(Constants.TRIPCALENDAR_ACTIVITY);
	}

	public String getPlace() {
		return getString(Constants.TRIPACCOMMODATION_PLACE);
	}

	public String getCity() {
		return getString(Constants.TRIPACCOMMODATION_CITY);
	}

	public Boolean getIsActivity() { return getBoolean(Constants.TRIPCALENDAR_ISACTIVITY); }
	public static void findTripCalendarInBackground(String objectId,
			final GetCallback<TripCalendar> callback) {
		ParseQuery<TripCalendar> tripCalendar = ParseQuery.getQuery(TripCalendar.class);
		tripCalendar.whereEqualTo(Constants.OBJECTID, objectId);
		tripCalendar.getFirstInBackground(new GetCallback<TripCalendar>() {
			@Override
			public void done(TripCalendar tripCalendar, ParseException e) {

				if (e == null) {
					callback.done(tripCalendar, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}

	public static void findTripCalendarListByFieldsInBackground(
			Map<String, Object> filter, final FindCallback<TripCalendar> callback) {
		ParseQuery<TripCalendar> tripCalendar = ParseQuery.getQuery(TripCalendar.class);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			tripCalendar.whereEqualTo((String) e.getKey(), e.getValue());
		}
		tripCalendar.findInBackground(new FindCallback<TripCalendar>() {
			@Override
			public void done(List<TripCalendar> tripCalendarList, ParseException e) {
				if (e == null) {
					callback.done(tripCalendarList, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}
}
