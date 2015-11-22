package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by agustin.huerta on 27/08/2015.
 */
@ParseClassName("TripMatePrize")
public class TripMatePrize extends ParseObject {

    private static String TAG = Constants.TAG_TRIPMATEPRIZEMODEL;

    private boolean isSelected = false;

    public TripMate getTripMate() {
        return (TripMate) getParseObject(Constants.TRIPMATE);
    }

    public String getPrize() {
        return getString(Constants.PRIZE);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public static void findTripMatePrizeInBackground(String objectId,
                                                         final GetCallback<TripMatePrize> callback) {
        ParseQuery<TripMatePrize> TripMatePrizeQuery = ParseQuery.getQuery(TripMatePrize.class);
        TripMatePrizeQuery.whereEqualTo(Constants.OBJECTID, objectId);
        TripMatePrizeQuery.getFirstInBackground(new GetCallback<TripMatePrize>() {
            @Override
            public void done(TripMatePrize tripMatePrize, ParseException e) {

                if (e == null) {
                    callback.done(tripMatePrize, null);
                } else {
                    callback.done(null, e);
                }
            }
        });
    }

    public static void findTripMatePrizeListByFieldsInBackground(
            Map<String, Object> filter, final FindCallback<TripMatePrize> callback) {
        ParseQuery<TripMatePrize> tripMatePrizeQuery = ParseQuery.getQuery(TripMatePrize.class);
        Iterator it = filter.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            tripMatePrizeQuery.whereEqualTo((String) e.getKey(), e.getValue());
        }
        tripMatePrizeQuery.findInBackground(new FindCallback<TripMatePrize>() {
            @Override
            public void done(List<TripMatePrize> tripMatePrizeList, ParseException e) {
                if (e == null) {
                    callback.done(tripMatePrizeList, null);
                } else {
                    callback.done(null, e);
                }
            }
        });
    }
}
