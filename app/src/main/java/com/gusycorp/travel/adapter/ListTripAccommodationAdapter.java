package com.gusycorp.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.TripAccommodation;
import com.gusycorp.travel.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by agustin.huerta on 24/07/2015.
 */
public class ListTripAccommodationAdapter extends ArrayAdapter<TripAccommodation> {

    private static final String TAG = Constants.TAG_LISTRIPACCOMODATIONADAPTER;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<TripAccommodation> mData = new ArrayList<TripAccommodation>();
    private ArrayList<HashMap<String, String>> mDataHeader = new ArrayList<HashMap<String, String>>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public ListTripAccommodationAdapter(Context context, int resource, List<TripAccommodation> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final TripAccommodation item) {
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
    public TripAccommodation getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListTripAccommodationAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ListTripAccommodationAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_list_accommodation_trip, null);
                    holder.columnPlace = (TextView) convertView.findViewById(R.id.accommodation_place);
                    holder.columnCity = (TextView) convertView.findViewById(R.id.accommodation_city);
                    holder.columnDateFrom = (TextView) convertView.findViewById(R.id.accommodation_date_from);
                    holder.columnDateTo = (TextView) convertView.findViewById(R.id.accommodation_date_to);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater
                            .inflate(R.layout.header_list_accommodation_trip, null);
                    holder.columnPlace = (TextView) convertView.findViewById(R.id.header_accommodation_place);
                    holder.columnCity = (TextView) convertView.findViewById(R.id.header_accommodation_city);
                    holder.columnDateFrom = (TextView) convertView.findViewById(R.id.header_accommodation_date_from);
                    holder.columnDateTo = (TextView) convertView.findViewById(R.id.header_accommodation_date_to);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ListTripAccommodationAdapter.ViewHolder) convertView.getTag();
        }
        if (mData.get(position) != null) {
            holder.columnPlace.setText(mData.get(position).getPlace());
            holder.columnCity.setText(mData.get(position).getCity());
            holder.columnDateFrom.setText(mData.get(position).getDateFrom());
            holder.columnDateTo.setText(mData.get(position).getDateTo());
        } else {
            holder.columnPlace.setText(mDataHeader.get(position).get(Constants.TRIPACCOMMODATIONLIST_COLUMN_ONE));
            holder.columnCity.setText(mDataHeader.get(position).get(Constants.TRIPACCOMMODATIONLIST_COLUMN_TWO));
            holder.columnDateFrom.setText(mDataHeader.get(position).get(Constants.TRIPACCOMMODATIONLIST_COLUMN_THREE));
            holder.columnDateTo.setText(mDataHeader.get(position).get(Constants.TRIPACCOMMODATIONLIST_COLUMN_FOUR));
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView columnPlace;
        TextView columnCity;
        TextView columnDateFrom;
        TextView columnDateTo;
    }
}
