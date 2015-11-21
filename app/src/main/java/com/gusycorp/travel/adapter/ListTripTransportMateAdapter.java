package com.gusycorp.travel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.TripMate;
import com.gusycorp.travel.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by agustin.huerta on 24/07/2015.
 */
public class ListTripTransportMateAdapter extends ArrayAdapter<TripMate> {

    private static final String TAG = Constants.TAG_LISTRIPTRANSPORTADAPTER;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<TripMate> mData = new ArrayList<TripMate>();
    private ArrayList<HashMap<String, String>> mDataHeader = new ArrayList<HashMap<String, String>>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    private Context context;

    public ListTripTransportMateAdapter(Context context, int resource, List<TripMate> objects) {
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
        ListTripTransportMateAdapter.ViewHolder holder = null;
        final int currentPosition = position;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ListTripTransportMateAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_list_transport_mate_trip, null);
                    holder.mateUser = (TextView) convertView.findViewById(R.id.mate_user);
                    holder.mateSelected = (CheckBox) convertView.findViewById(R.id.mate_selected);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater
                            .inflate(R.layout.header_list_mate_trip, null);
                    holder.mateUser = (TextView) convertView.findViewById(R.id.header_mate_user);
                    break;
            }
            convertView.setTag(holder);
            if(rowType==TYPE_ITEM){
                holder.mateSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        if(mData.get(currentPosition).isSelected()){
                            mData.get(currentPosition).setIsSelected(false);
                        }else{
                            mData.get(currentPosition).setIsSelected(true);
                        }
                    }
                });
            }
        } else {
            holder = (ListTripTransportMateAdapter.ViewHolder) convertView.getTag();
        }
        if (mData.get(position) != null) {
            holder.mateUser.setText(mData.get(position).getUsername());
            holder.mateSelected.setChecked(mData.get(position).isSelected());
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView mateUser;
        CheckBox mateSelected;
    }

    public ArrayList<TripMate> getTripMateList() {
        return mData;
    }

}
