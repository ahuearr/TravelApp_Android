package com.gusycorp.travel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by agustin.huerta on 24/07/2015.
 */
public class ListTripTransportAdapter extends ArrayAdapter<HashMap<String,String>> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<HashMap<String, String>> mData = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> mDataHeader = new ArrayList<HashMap<String, String>>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    TextView columnDate;
    TextView columnOrigin;
    TextView columnDestination;

    private LayoutInflater mInflater;

    public ListTripTransportAdapter(Context context, int resource, List<HashMap<String,String>> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final HashMap<String, String> item) {
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
    public HashMap<String, String> getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListTripTransportAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ListTripTransportAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_list_transport_trip, null);
                    holder.columnDate = (TextView) convertView.findViewById(R.id.transport_date);
                    holder.columnOrigin = (TextView) convertView.findViewById(R.id.transport_from);
                    holder.columnDestination = (TextView) convertView.findViewById(R.id.transport_to);
                    holder.columnTransport = (ImageView) convertView.findViewById(R.id.transport_transport);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater
                            .inflate(R.layout.header_list_transport_trip, null);
                    holder.columnDate = (TextView) convertView.findViewById(R.id.header_transport_date);
                    holder.columnOrigin = (TextView) convertView.findViewById(R.id.header_transport_from);
                    holder.columnDestination = (TextView) convertView.findViewById(R.id.header_transport_to);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ListTripTransportAdapter.ViewHolder) convertView.getTag();
        }
        if (mData.get(position) != null) {
            holder.columnDate.setText(mData.get(position).get(Constants.TRIPTRANSPORTLIST_COLUMN_ONE));
            holder.columnOrigin.setText(mData.get(position).get(Constants.TRIPTRANSPORTLIST_COLUMN_TWO));
            holder.columnDestination.setText(mData.get(position).get(Constants.TRIPTRANSPORTLIST_COLUMN_THREE));
        } else {
            holder.columnDate.setText(mDataHeader.get(position).get(Constants.TRIPTRANSPORTLIST_COLUMN_ONE));
            holder.columnOrigin.setText(mDataHeader.get(position).get(Constants.TRIPTRANSPORTLIST_COLUMN_TWO));
            holder.columnDestination.setText(mDataHeader.get(position).get(Constants.TRIPTRANSPORTLIST_COLUMN_THREE));
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView columnDate;
        TextView columnOrigin;
        TextView columnDestination;
        ImageView columnTransport;
    }
}
