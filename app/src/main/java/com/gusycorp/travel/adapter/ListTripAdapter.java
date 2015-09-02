package com.gusycorp.travel.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gusycorp.travel.R;
import com.gusycorp.travel.model.Trip;
import com.gusycorp.travel.util.Constants;
import com.parse.ParseUser;

public class ListTripAdapter extends ArrayAdapter<Trip> {

	private static final String TAG = Constants.TAG_LISTRIPADAPTER;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;

	private ArrayList<Trip> mData = new ArrayList<Trip>();
	private ArrayList<String> mDataHeader = new ArrayList<String>();
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

	private LayoutInflater mInflater;

	public ListTripAdapter(Context context, int resource, List<Trip> objects) {
		super(context, resource, objects);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addItem(final Trip item) {
		mData.add(item);
		mDataHeader.add(null);
		notifyDataSetChanged();
	}

	public void addSectionHeaderItem(final String item) {
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
	public Trip getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int rowType = getItemViewType(position);

		if (convertView == null) {
			holder = new ViewHolder();
			switch (rowType) {
			case TYPE_ITEM:
				convertView = mInflater.inflate(R.layout.row_list_trip, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.text);
				holder.roleView = (ImageView) convertView.findViewById(R.id.trip_role);
				break;
			case TYPE_SEPARATOR:
				convertView = mInflater
						.inflate(R.layout.header_list_trip, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textSeparator);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mData.get(position) != null) {
			holder.textView.setText(mData.get(position).getTripName());
			if(ParseUser.getCurrentUser().getObjectId().equals(mData.get(position).getOrganizerId())){
				holder.roleView.setImageResource(R.drawable.organizer);
			} else {
				holder.roleView.setImageResource(R.drawable.mates);
			}
		} else {
			holder.textView.setText(mDataHeader.get(position));
		}

		return convertView;
	}

	public static class ViewHolder {
		public TextView textView;
		public ImageView roleView;
	}

}
