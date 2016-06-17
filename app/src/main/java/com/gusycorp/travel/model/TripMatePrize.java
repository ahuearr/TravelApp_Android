package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudQuery;

/**
 * Created by agustin.huerta on 27/08/2015.
 */
public class TripMatePrize extends ITObject {

    private static String TABLENAME = Constants.TAG_TRIPMATEPRIZEMODEL;

    public TripMatePrize(){
        super(TABLENAME);
    }
    public TripMate getTripMate() {
        return (TripMate) getCloudObject(Constants.TRIPMATE);
    }

    public Double getPrize() { return getDouble(Constants.PRIZE);}

    public static void findTripMatePrizeInBackground(String objectId, final ITObjectCallback<TripMatePrize> callback) throws CloudException {
        CloudQuery query = new CloudQuery(TABLENAME);
        query.findById(objectId, new ITObjectCallback<TripMatePrize>(){
            @Override
            public void done(TripMatePrize tripMatePrize, CloudException e) {
                if(tripMatePrize != null){
                    callback.done(tripMatePrize, null);
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

    public static void findTripMatePrizeListByFieldsInBackground(
            Map<String, Object> filter, final ITObjectArrayCallback<TripMatePrize> callback) throws CloudException {
        CloudQuery query = new CloudQuery(TABLENAME);
        Iterator it = filter.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            query.equalTo((String) e.getKey(), e.getValue());
        }
        query.find(new ITObjectArrayCallback<TripMatePrize>() {
            @Override
            public void done(TripMatePrize[] tripMatePrizeList, CloudException e) throws CloudException {
                if (e == null) {
                    callback.done(tripMatePrizeList, null);
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
