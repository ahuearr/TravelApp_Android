package com.gusycorp.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.model.TripTransport;
import com.gusycorp.travel.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by agustin.huerta on 24/07/2015.
 */
public class ListTripMateAdapter extends ArrayAdapter<TripMate> {

    private static final String TAG = Constants.TAG_LISTRIPMATEADAPTER;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<TripMate> mData = new ArrayList<TripMate>();
    private ArrayList<HashMap<String, String>> mDataHeader = new ArrayList<HashMap<String, String>>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    private Context context;

    public ListTripMateAdapter(Context context, int resource, List<TripMate> objects) {
        super(context, resource, objects);
        this.context=context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final TripMate item) {
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
    public TripMate getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListTripMateAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ListTripMateAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_list_mate_trip, null);
                    holder.mateUser = (TextView) convertView.findViewById(R.id.mate_user);
                    holder.mateRol = (TextView) convertView.findViewById(R.id.mate_rol);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater
                            .inflate(R.layout.header_list_mate_trip, null);
                    holder.mateUser = (TextView) convertView.findViewById(R.id.header_mate_user);
                    holder.mateRol = (TextView) convertView.findViewById(R.id.header_mate_rol);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ListTripMateAdapter.ViewHolder) convertView.getTag();
        }
        if (mData.get(position) != null) {
            holder.mateUser.setText(mData.get(position).getUsername());
            holder.mateRol.setText(mData.get(position).getOrganizer() ? context.getString(R.string.organizer) : context.getString(R.string.mate));
        } else {
            holder.mateUser.setText(mDataHeader.get(position).get(context.getString(R.string.username)));
            holder.mateRol.setText(mDataHeader.get(position).get(context.getString(R.string.rol)));
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView mateUser;
        TextView mateRol;
    }
}
