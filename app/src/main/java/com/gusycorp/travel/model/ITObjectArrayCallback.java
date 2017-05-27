package com.gusycorp.travel.model;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;

/**
 * Created by Propietario on 16/06/2016.
 */
public interface ITObjectArrayCallback<T extends ITObject> extends CloudObjectArrayCallback {
    void done(T[]  x, CloudException t) throws CloudException;
}
