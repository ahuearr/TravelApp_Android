package com.gusycorp.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.model.TripCalendar;
import com.gusycorp.travel.util.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by agustin.huerta on 24/07/2015.
 */
public class ListTripCalendarAdapter extends ArrayAdapter<TripCalendar> {

    private static final String TAG = Constants.TAG_LISTRIPCALENDARADAPTER;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<TripCalendar> mData = new ArrayList<TripCalendar>();
    private ArrayList<HashMap<String, String>> mDataHeader = new ArrayList<HashMap<String, String>>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public ListTripCalendarAdapter(Context context, int resource, List<TripCalendar> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final TripCalendar item) {
        mData.add(item);
        mDataHeader.add(null);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final HashMap<String, String> item) {
        mData.add(null);
        mDataHeader.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TripCalendar getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try{
            ListTripCalendarAdapter.ViewHolder holder = null;
            int rowType = getItemViewType(position);

            if (convertView == null) {
                holder = new ListTripCalendarAdapter.ViewHolder();
                switch (rowType) {
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.row_list_calendar_trip, null);
                        holder.columnDate = (TextView) convertView.findViewById(R.id.calendar_date);
                        holder.columnActivity = (TextView) convertView.findViewById(R.id.calendar_activity);
                        break;
                    case TYPE_SEPARATOR:
                        convertView = mInflater
                                .inflate(R.layout.header_list_calendar_trip, null);
                        holder.columnDate = (TextView) convertView.findViewById(R.id.header_calendar_date);
                        holder.columnActivity = (TextView) convertView.findViewById(R.id.header_calendar_activity);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ListTripCalendarAdapter.ViewHolder) convertView.getTag();
            }
            if (mData.get(position) != null) {
                holder.columnDate.setText(mData.get(position).getDate());
                holder.columnActivity.setText(mData.get(position).getActivity());
            } else {
                holder.columnDate.setText(mDataHeader.get(position).get(Constants.TRIPCALENDARLIST_COLUMN_ONE));
                holder.columnActivity.setText(mDataHeader.get(position).get(Constants.TRIPCALENDARLIST_COLUMN_TWO));
            }
        }catch(ParseException e){
            e.printStackTrace();
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView columnDate;
        TextView columnActivity;
    }
}
