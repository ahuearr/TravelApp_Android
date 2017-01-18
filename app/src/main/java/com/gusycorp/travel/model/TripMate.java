package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

/**
 * Created by agustin.huerta on 27/08/2015.
 */
public class TripMate extends ITObject{

    private static String TABLENAME = Constants.TAG_TRIPMATEMODEL;
    private CloudObject tripMate;

    public TripMate(){
        tripMate = new CloudObject(TABLENAME);
    }

    public TripMate(CloudObject tripMate){
        this.tripMate = tripMate;
    }

    public CloudObject getTripMate(){
        return tripMate;
    }

    public void setTripMate(CloudObject tripMate){
        this.tripMate = tripMate;
    }

    public String getId() {
        return tripMate.getId();
    }

    public String getUserId() {
        return tripMate.getString(Constants.USERID);
    }

    public void setUserId(String userId) throws CloudException {
        tripMate.set(Constants.USERID, userId);
    }

    public String getUsername() {
        return tripMate.getString(Constants.USERNAME);
    }

    public void setUserName(String userName) throws CloudException {
        tripMate.set(Constants.USERNAME, userName);
    }

    public Boolean getOrganizer() {
        return tripMate.getBoolean(Constants.ORGANIZER);
    }

    public void setOrganizer(Boolean organizer) throws CloudException {
        tripMate.set(Constants.ORGANIZER, organizer);
    }

    public String getTripId(){
        return tripMate.getString(Constants.TRIPID);
    }

    public void setTripId(String tripId) throws CloudException {
        tripMate.set(Constants.TRIPID, tripId);
    }

    public static void findTripMateInBackground(String objectId, final CloudObjectCallback callback) throws CloudException {
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
    public static void findTripMateListByFieldsInBackground(
            Map<String, Object> filter, final ITObjectArrayCallback<TripMate> callback) throws CloudException {
        CloudQuery query = new CloudQuery(TABLENAME);
        Iterator it = filter.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            query.equalTo((String) e.getKey(), e.getValue());
        }
        query.find(new ITObjectArrayCallback<TripMate>() {
            @Override
            public void done(TripMate[] tripMateList, CloudException e) throws CloudException {
                if (e == null) {
                    callback.done(tripMateList, null);
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
