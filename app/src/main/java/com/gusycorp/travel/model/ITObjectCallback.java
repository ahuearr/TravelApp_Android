package com.gusycorp.travel.model;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;

/**
 * Created by Propietario on 16/06/2016.
 */
public interface ITObjectCallback <T extends ITObject> extends CloudObjectCallback {
    void done(T object, CloudException e);
}
