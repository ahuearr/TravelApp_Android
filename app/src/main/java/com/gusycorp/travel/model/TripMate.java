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
import io.cloudboost.CloudQuery;

/**
 * Created by agustin.huerta on 27/08/2015.
 */
public class TripMate extends ITObject {

    private static String TABLENAME = Constants.TAG_TRIPMATEMODEL;

    public TripMate(){
        super(TABLENAME);
    }

    public String getUserId() {
        return getString(Constants.USERID);
    }

    public String getUsername() {
        return getString(Constants.USERNAME);
    }

    public Boolean getOrganizer() {
        return getBoolean(Constants.ORGANIZER);
    }

    public static void findTripMateInBackground(String objectId, final ITObjectCallback<TripMate> callback) throws CloudException {
        CloudQuery query = new CloudQuery(TABLENAME);
        query.findById(objectId, new ITObjectCallback<TripMate>(){
            @Override
            public void done(TripMate tripMate, CloudException e) {
                if(tripMate != null){
                    callback.done(tripMate, null);
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

}
