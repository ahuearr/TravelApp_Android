package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
public class ITObject {

    public DateTimeFormatter dfDateDatabase = DateTimeFormat.forPattern(Constants.DATE_MASK_DATABASE);
    public DateTimeFormatter dfDate = DateTimeFormat.forPattern(Constants.ONLY_DATE_MASK);
    public DateTimeFormatter dfTime = DateTimeFormat.forPattern(Constants.DATE_MASK);

    public DateTime getDate(String dateString) throws ParseException {
        return dfDateDatabase.parseDateTime(dateString);
    }

    public DateTime getTime(String dateString) throws ParseException {
        return dfTime.parseDateTime(dateString);
    }

    public List<String> getListString(String field, CloudObject table){
        Object[] objectArray;
        String[] stringArray;
        try{
            objectArray = table.getArray(field);
            stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
            return Arrays.asList(stringArray);
        }catch(NullPointerException e){
            stringArray = new String[0];
        }
        return Arrays.asList(stringArray);

    }

    public CloudObject[] getArray (CloudObject table, String field){
        CloudObject[] objectArray;
        try{
            objectArray = table.getCloudObjectArray(field);
        }catch(NullPointerException e){
            //If the list field is empty, cloudboost returns null
            objectArray = new CloudObject[0];
            return objectArray;
        }
        return objectArray;
    }
}
