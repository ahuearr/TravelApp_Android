package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

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
public class TripMatePrize extends ITObject {

    private static String TABLENAME = Constants.TAG_TRIPMATEPRIZEMODEL;
    private CloudObject tripMatePrize;

    public TripMatePrize(){
        tripMatePrize = new CloudObject(TABLENAME);
    }

    public TripMatePrize(CloudObject tripMatePrize){
        this.tripMatePrize = tripMatePrize;
    }

    public CloudObject getTripMatePrize(){
        return tripMatePrize;
    }

    public void setTripMatePrize(CloudObject tripMatePrize){
        this.tripMatePrize = tripMatePrize;
    }

    public String getId() {
        return tripMatePrize.getId();
    }

    public TripMate getTripMate() {
        return new TripMate(tripMatePrize.getCloudObject(Constants.TRIPMATE));
    }

    public void setTripMate(TripMate tripMate) throws CloudException {
        tripMatePrize.set(Constants.TRIPMATE, tripMate.getTripMate());
    }

    public Double getPrize() {
        return tripMatePrize.getDouble(Constants.PRIZE);
    }

    public void setPrize(Double prize) throws CloudException {
        tripMatePrize.set(Constants.PRIZE, prize);
    }

    public static void findTripMatePrizeInBackground(String objectId, final CloudObjectCallback callback) throws CloudException {
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
*/

}
