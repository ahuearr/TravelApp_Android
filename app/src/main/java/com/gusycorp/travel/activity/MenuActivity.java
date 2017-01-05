package com.gusycorp.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.activity.Accommodation.TripAccommodationListActivity;
import com.gusycorp.travel.activity.Calendar.TripCalendarListActivity;
import com.gusycorp.travel.activity.Map.TripMapActivity;
import com.gusycorp.travel.activity.Mates.TripMatesActivity;
import com.gusycorp.travel.activity.Transport.TripTransportListActivity;
import com.gusycorp.travel.application.TravelApplication;

import java.util.Arrays;
import java.util.List;

public class MenuActivity extends Activity implements View.OnClickListener {

    public static final String URL = "URL";
    public String tripName = "";
    public List<Integer> menus;

    private final List<Integer> options = Arrays.asList(new Integer[]{
            R.id.transporte, R.id.alojamiento, R.id.calendario,
            R.id.mapa, R.id.companeros,});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_trip);
        final TravelApplication app = (TravelApplication) getApplication();
        menus = app.getMenus();
    }

    @Override
    public void onResume() {
        super.onResume();

        for (final Integer option : options) {
            final TextView textView = (TextView) findViewById(option);
            textView.setOnClickListener(this);
            textView.setBackgroundColor(getResources().getColor(
                    R.color.backgroundMenu));
        }

        if (menus.size() - 1 >= 0) {
            final TextView textView = (TextView) findViewById(
                    menus.get(menus.size() - 1));
            textView.setBackgroundColor(getResources().getColor(
                    R.color.backgroundMenuSelected));
        }

    }

    private Class findClass(final Integer identifier) {
        switch (identifier) {
            case R.id.alojamiento:
                return TripAccommodationListActivity.class;
            case R.id.transporte:
                return TripTransportListActivity.class;
            case R.id.calendario:
                return TripCalendarListActivity.class;
            case R.id.mapa:
                return TripMapActivity.class;
            case R.id.companeros:
                return TripMatesActivity.class;
            default:
                return null;
        }
    }

    @Override
    public void onClick(final View v) {
        seleccionarMenu(this, v.getId(), findClass(v.getId()));
    }

    public void seleccionarMenu(final Activity context, final int id,
                                       final Class destination) throws Resources.NotFoundException {
        final TravelApplication app = (TravelApplication) context
                .getApplication();
        final List<Integer> menus = app.getMenus();
        final int position = menus.size() - 1;
        if (position >= 0) {
            final TextView textView = (TextView) context.findViewById(menus
                    .get(position));
            textView.setBackgroundColor(context.getResources().getColor(
                    R.color.backgroundMenu));
        }

        if(!TripMapActivity.class.equals(destination)){
            menus.add(id);
            app.setMenus(menus);
        }

        final Intent intent = new Intent(context, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("tripName", tripName);
        context.startActivity(intent);
    }

}