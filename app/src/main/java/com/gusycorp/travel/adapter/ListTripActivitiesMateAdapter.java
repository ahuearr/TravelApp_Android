package com.gusycorp.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.TripMatePrize;
import com.gusycorp.travel.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import io.cloudboost.CloudException;

/**
 * Created by agustin.huerta on 24/07/2015.
 */
public class ListTripActivitiesMateAdapter extends ArrayAdapter<TripMatePrize> {

    private static final String TAG = Constants.TAG_LISTRIPACTIVITIESADAPTER;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<TripMatePrize> mData = new ArrayList<TripMatePrize>();
    private ArrayList<HashMap<String, String>> mDataHeader = new ArrayList<HashMap<String, String>>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    private Context context;
    private boolean isOrganizer;

    public ListTripActivitiesMateAdapter(Context context, int resource, List<TripMatePrize> objects, boolean isOrganizer) {
        super(context, resource, objects);
        this.context=context;
        this.isOrganizer=isOrganizer;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final TripMatePrize item) {
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
    public TripMatePrize getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListTripActivitiesMateAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ListTripActivitiesMateAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_list_activities_mate_trip, null);
                    holder.mateUser = (TextView) convertView.findViewById(R.id.mate_user);
                    holder.matePrize = (EditText) convertView.findViewById(R.id.mate_prize);
                    if(!isOrganizer){
                        holder.mateUser.setEnabled(false);
                        holder.matePrize.setEnabled(false);
                    }
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater
                            .inflate(R.layout.header_list_activities_mate_trip, null);
                    holder.mateUser = (TextView) convertView.findViewById(R.id.header_mate_user);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ListTripActivitiesMateAdapter.ViewHolder) convertView.getTag();
        }
        if (mData.get(position) != null) {
            holder.mateUser.setText(mData.get(position).getTripMate().getUsername());
            holder.matePrize.setText(Double.toString(mData.get(position).getPrize()));
            holder.matePrize.setId(position);

            holder.matePrize.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText matePrize = (EditText) v;
                        double prize = StringUtils.isNotBlank(matePrize.getText().toString()) ? Double.parseDouble(matePrize.getText().toString()) : 0.0;
                        try {
                            mData.get(position).set(Constants.PRIZE, prize);
                        } catch (CloudException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView mateUser;
        EditText matePrize;
    }

    public ArrayList<TripMatePrize> getTripMateList() {
        return mData;
    }

}
