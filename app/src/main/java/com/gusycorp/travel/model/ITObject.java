package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

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

    private DateTimeFormatter df = DateTimeFormat.forPattern(Constants.DATE_MASK);

    public DateTime getDate(String dateString) throws ParseException {
        return df.parseDateTime(dateString);
    }

    public List<String> getListString(String field, CloudObject object){
        Object[] objectArray = object.getArray(field);
        String[] stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
        return Arrays.asList(stringArray);
    }

}
