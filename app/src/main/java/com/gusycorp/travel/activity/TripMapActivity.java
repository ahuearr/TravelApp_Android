package com.gusycorp.travel.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.gusycorp.travel.R;
import com.gusycorp.travel.application.TravelApplication;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;

public class TripMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private TravelApplication app;

    private Trip currentTrip;

    private List<TripTransport> tripTransports = new ArrayList<TripTransport>();
    private List<TripAccommodation> tripAccommodations = new ArrayList<TripAccommodation>();
    private List<TripCalendar> tripCalendars = new ArrayList<TripCalendar>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);

        app = (TravelApplication) getApplication();
        currentTrip = app.getCurrentTrip();

        initializeMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    private void initializeMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                String place = currentTrip.getTripName();
                Address address = getAddressFromString(place);
                if (address != null) {
                    LatLng placePosition = new LatLng(address.getLatitude(),
                            address.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placePosition, 3));
                } else {
                }
                getTripCalendars();
            }
        }
    }

    private void getTripCalendars() {

        tripTransports.clear();
        tripAccommodations.clear();
        tripCalendars.clear();

        findAll();
    }

    private void findAll() {
        List<TripTransport> tripTransportList = currentTrip.getTripTransportList();
        tripTransports.addAll(tripTransportList);
        List<TripAccommodation> tripAccommodationList = currentTrip.getTripAccommodationList();
        tripAccommodations.addAll(tripAccommodationList);
        List<TripCalendar> tripCalendarList = currentTrip.getTripCalendarList();
        tripCalendars.addAll(tripCalendarList);
        setUpMap();
    }

    private void setUpMap() {
        for(TripTransport item : tripTransports){
            Double latitudeFrom = item.getLatitudeFrom();
            Double longitudeFrom = item.getLongtiudeFrom();
            if(latitudeFrom!=null && latitudeFrom!=0.0 && longitudeFrom!=null && longitudeFrom!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeFrom, longitudeFrom)).title(getString(R.string.transportDepartureFrom) + " " + item.getFrom()));
            } else {
                if(!"".equals(item.getFrom())){
                    String place = item.getFrom();
                    Address address = getAddressFromString(place);
                    if (address != null) {
                        LatLng placePosition = new LatLng(address.getLatitude(),
                                address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(placePosition).title(place));
                        try {
                            item.set(Constants.LATITUDEFROM, address.getLatitude());
                            item.set(Constants.LONGITUDEFROM, address.getLongitude());
                            item.save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject x, CloudException t) throws CloudException {
                                }
                            });
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new GetLocationTask(place, item, Constants.TAG_TRIPTRANSPORTMODEL, Constants.FROM).execute();
                    }
                }
            }
            Double latitudeTo = item.getLatitudeTo();
            Double longitudeTo = item.getLongitudeTo();
            if(latitudeFrom!=null && latitudeFrom!=0.0 && latitudeTo!=null && longitudeTo!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeTo, longitudeTo)).title(getString(R.string.transportArrivalTo) + " " + item.getTo()));
            } else {
                if(!"".equals(item.getTo())){
                    String place = item.getTo();
                    Address address = getAddressFromString(place);
                    if (address != null) {
                        LatLng placePosition = new LatLng(address.getLatitude(),
                                address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(placePosition).title(place));
                        try {
                            item.set(Constants.LATITUDETO, address.getLatitude());
                            item.set(Constants.LONGITUDETO, address.getLongitude());
                            item.save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject x, CloudException t) throws CloudException {
                                }
                            });
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new GetLocationTask(place, item, Constants.TAG_TRIPTRANSPORTMODEL, Constants.TO).execute();
                    }
                }
            }
        }

        for(TripAccommodation item : tripAccommodations){
            Double latitude = item.getLatitude();
            Double longitude = item.getLongitude();
            if(latitude!=null && latitude!=0.0 && longitude!=null && longitude!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(item.getPlace()+" "+item.getAddress()+" "+item.getCity()));
            } else {
                if(!"".equals(item.getPlace()) || !"".equals(item.getAddress()) || !"".equals(item.getCity())){
                    String place = item.getPlace() + " " + item.getAddress() + " " + item.getCity();
                    Address address = getAddressFromString(place);
                    if (address != null) {
                        LatLng placePosition = new LatLng(address.getLatitude(),
                                address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(placePosition).title(place));
                        try {
                            item.set(Constants.LATITUDE, address.getLatitude());
                            item.set(Constants.LONGITUDE, address.getLongitude());
                            item.save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject x, CloudException t) throws CloudException {
                                }
                            });
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new GetLocationTask(place, item, Constants.TAG_TRIPACCOMMODATIONMODEL, "").execute();
                    }
                }
            }
        }

        for(TripCalendar item : tripCalendars){
            Double latitude = item.getLatitude();
            Double longitude = item.getLongitude();
            if(latitude!=null && latitude!=0.0 && longitude!=null && longitude!=0.0){
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(item.getActivity()+" "+item.getPlace()+" "+item.getCity()));
            } else {
                if(!"".equals(item.getPlace()) || !"".equals(item.getCity())){
                    String place = item.getActivity()+" "+item.getPlace()+" "+item.getCity();
                    Address address = getAddressFromString(place);
                    if (address != null) {
                        LatLng placePosition = new LatLng(address.getLatitude(),
                                address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(placePosition).title(place));
                        try {
                            item.set(Constants.LATITUDE, address.getLatitude());
                            item.set(Constants.LONGITUDE, address.getLongitude());
                            item.save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject x, CloudException t) throws CloudException {
                                }
                            });
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new GetLocationTask(place, item, Constants.TAG_TRIPCALENDARMODEL, "").execute();
                    }
                }
            }
        }
    }

    private Address getAddressFromString(final String location) {
        Address result = null;
        List<Address> addresses = null;
        if (location != null) {
            try {
                Geocoder geocoder = new Geocoder(this);
                addresses = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {
                result = addresses.get(0);
            }
        }
        return result;
    }

    private class GetLocationTask extends AsyncTask<Void, Void, Address> {
        JSONObject jsonObject;
        String addressString;
        Object item;
        String table;
        String fromOrTo;

        public GetLocationTask(String locationName, Object item, String table, String fromOrTo) {
            this.addressString = locationName;
            this.item=item;
            this.table=table;
            this.fromOrTo=fromOrTo;
        }

        @Override
        protected Address doInBackground(Void... params) {
            return getLocationInfo(addressString);
        }

        private Address getLocationInfo(String address) {

            String query = "http://maps.google.com/maps/api/geocode/json?address="
                    + address.replaceAll("  ", " ").replaceAll(" ", "%20")
                    + "&sensor=false";
            Log.e("TAG", query);
            Address addr = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(query);

            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();

                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                    try {
                        jsonObject = new JSONObject(stringBuilder.toString());
                        Log.e("TAG", jsonObject.get("status").toString());
                        if ("OK".equals(jsonObject.get("status").toString())) {
                            addr = new Address(Locale.getDefault());

                            Double lon = null;
                            Double lat = null;

                            Log.e("TAG", jsonObject.get("results").toString());
                            JSONObject geometryJsonObject = ((JSONArray) jsonObject
                                    .get("results")).getJSONObject(0)
                                    .getJSONObject("geometry");
                            // Se comprueba si hay geometria y si hay geometria
                            // si hay coordenadas y se crea el objecto Address
                            // con los parametros recibidos del geocoder
                            if (geometryJsonObject != null) {
                                JSONObject locationJsonObject = geometryJsonObject
                                        .getJSONObject("location");
                                if (locationJsonObject != null) {
                                    lon = locationJsonObject.getDouble("lng");
                                    lat = locationJsonObject.getDouble("lat");

                                    if (lon != null && lat != null) {
                                        addr.setLatitude(lat);
                                        addr.setLongitude(lon);

                                        JSONArray addrComp = ((JSONArray) jsonObject
                                                .get("results")).getJSONObject(
                                                0).getJSONArray(
                                                "address_components");

                                        for (int i = 0; i < addrComp.length(); i++) {
                                            JSONObject addressComponentJsonObject = addrComp
                                                    .getJSONObject(i);
                                            JSONArray typeJsonArray = addressComponentJsonObject
                                                    .getJSONArray("types");
                                            for (int j = 0; j < typeJsonArray
                                                    .length(); j++) {
                                                if ("route"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es la calle (No seteable)
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("neighborhood"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Puede ser la calle. (No
                                                    // seteable)
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("locality"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es la poblacion
                                                    addr.setLocality(addressComponentJsonObject
                                                            .getString("long_name"));
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("administrative_area_level_2"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es la provincia
                                                    addr.setAdminArea(addressComponentJsonObject
                                                            .getString("long_name"));
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("administrative_area_level_1"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es la CCAA (No seteable
                                                    // si hay provincia)
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("country"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es el Pais
                                                    addr.setCountryName(addressComponentJsonObject
                                                            .getString("long_name"));
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("street_number"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es la numero de calle (No
                                                    // seteable)
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                } else if ("postal_code"
                                                        .equals(typeJsonArray
                                                                .getString(j))) {
                                                    // Es el Codigo Postal
                                                    addr.setPostalCode(addressComponentJsonObject
                                                            .getString("long_name"));
                                                    break; // Se rompe el bucle
                                                    // porque type puede
                                                    // tener
                                                    // varios valores y
                                                    // si hemos
                                                    // encontrado 1 el
                                                    // resto no importa
                                                }
                                            }
                                        }

                                    } else {
                                        addr = null;
                                    }
                                } else {
                                    addr = null;
                                }
                            } else {
                                addr = null;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return addr;

        }

        @Override
        protected void onPostExecute(Address address) {
            if (address != null) {

                LatLng placePosition = new LatLng(
                        address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(placePosition).title(addressString));
                switch (table){
                    case Constants.TAG_TRIPTRANSPORTMODEL:
                        TripTransport itemTransport = (TripTransport) item;
                        if(Constants.FROM.equals(fromOrTo)){
                            try {
                                itemTransport.set(Constants.LATITUDEFROM, address.getLatitude());
                                itemTransport.set(Constants.LONGITUDEFROM, address.getLongitude());
                                itemTransport.save(new CloudObjectCallback() {
                                    @Override
                                    public void done(CloudObject x, CloudException t) throws CloudException {
                                    }
                                });
                            } catch (CloudException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                itemTransport.set(Constants.LATITUDETO, address.getLatitude());
                                itemTransport.set(Constants.LONGITUDETO, address.getLongitude());
                                itemTransport.save(new CloudObjectCallback() {
                                    @Override
                                    public void done(CloudObject x, CloudException t) throws CloudException {
                                    }
                                });
                            } catch (CloudException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case Constants.TAG_TRIPACCOMMODATIONMODEL:
                        TripAccommodation itemAccommodation = (TripAccommodation) item;
                        try {
                            itemAccommodation.set(Constants.LATITUDE, address.getLatitude());
                            itemAccommodation.set(Constants.LONGITUDE, address.getLongitude());
                            itemAccommodation.save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject x, CloudException t) throws CloudException {
                                }
                            });
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constants.TAG_TRIPCALENDARMODEL:
                        TripCalendar itemCalendar = (TripCalendar) item;
                        try {
                            itemCalendar.set(Constants.LATITUDE, address.getLatitude());
                            itemCalendar.set(Constants.LONGITUDE, address.getLongitude());
                            itemCalendar.save(new CloudObjectCallback() {
                                @Override
                                public void done(CloudObject x, CloudException t) throws CloudException {
                                }
                            });
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            } else {
                Toast.makeText(TripMapActivity.this,
                        "La direccion no se encuentra en el mapa",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
