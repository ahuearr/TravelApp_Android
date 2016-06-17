package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.cloudboost.CloudObject;

/**
 * Created by Propietario on 15/06/2016.
 */
public class ITObject extends CloudObject{

    private DateFormat df = new SimpleDateFormat(Constants.DATE_MASK);

    public ITObject(String tableName){
        super(tableName);
    }

    public Date getDate(String dateString) throws ParseException {
        return df.parse(dateString);
    }

    public List<String> getListString(String field){
        Object[] objectArray = getArray(field);
        String[] stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
        return Arrays.asList(stringArray);
    }
}
