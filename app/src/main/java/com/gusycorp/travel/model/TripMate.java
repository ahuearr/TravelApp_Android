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

/**
 * Created by agustin.huerta on 27/08/2015.
 */
@ParseClassName("TripMate")
public class TripMate extends ParseObject {

    private static String TAG = Constants.TAG_TRIPMATEMODEL;

    private boolean isSelected = false;

    public String getUserId() {
        return getString(Constants.USERID);
    }

    public String getUsername() {
        return getString(Constants.USERNAME);
    }

    public Boolean getOrganizer() {
        return getBoolean(Constants.ORGANIZER);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public static void findTripMateInBackground(String objectId,
                                                         final GetCallback<TripMate> callback) {
        ParseQuery<TripMate> TripMateQuery = ParseQuery.getQuery(TripMate.class);
        TripMateQuery.whereEqualTo(Constants.OBJECTID, objectId);
        TripMateQuery.getFirstInBackground(new GetCallback<TripMate>() {
            @Override
            public void done(TripMate tripMate, ParseException e) {

                if (e == null) {
                    callback.done(tripMate, null);
                } else {
                    callback.done(null, e);
                }
            }
        });
    }

    public static void findTripMateListByFieldsInBackground(
            Map<String, Object> filter, final FindCallback<TripMate> callback) {
        ParseQuery<TripMate> tripMateQuery = ParseQuery.getQuery(TripMate.class);
        Iterator it = filter.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            tripMateQuery.whereEqualTo((String) e.getKey(), e.getValue());
        }
        tripMateQuery.findInBackground(new FindCallback<TripMate>() {
            @Override
            public void done(List<TripMate> tripMateList, ParseException e) {
                if (e == null) {
                    callback.done(tripMateList, null);
                } else {
                    callback.done(null, e);
                }
            }
        });
    }
}
