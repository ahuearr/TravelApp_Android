package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudQuery;

public class TypeTransport extends ITObject {

	private static String TABLENAME = Constants.TAG_TRIPMODEL;

	public TypeTransport(){
		super(TABLENAME);
	}
	public String getTransportName() {
		return getString(Constants.TRANSPORTNAME);
	}
	public String getTransportImageName() {
		return getString(Constants.TRANSPORTIMAGENAME);
	}

	public static void findTypeTransportInBackground(String objectId, final ITObjectCallback<TypeTransport> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new ITObjectCallback<TypeTransport>(){
			@Override
			public void done(TypeTransport typeTransport, CloudException e) throws CloudException {
				if(typeTransport != null){
					callback.done(typeTransport, null);
				} else {
					callback.done(null,e);
				}
			}
			@Override
			public void done(CloudObject obj, CloudException e) throws CloudException {
				if(obj != null){
					callback.done(obj, null);
				} else {
					callback.done(null,e);
				}
			}
		});

	}

	public static void findTypeTransportListByFieldsInBackground(
			Map<String, Object> filter, final ITObjectArrayCallback<TypeTransport> callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new ITObjectArrayCallback<TypeTransport>() {
			@Override
			public void done(TypeTransport[] typeTransportList, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(typeTransportList, null);
				} else {
					callback.done(null, e);
				}
			}

			@Override
			public void done(CloudObject[] obj, CloudException e) throws CloudException {
				if (e == null) {
					callback.done(obj, null);
				} else {
					callback.done(null, e);
				}
			}
		});
	}


	@Override
	public String toString() {
		return getTransportName();
	}
}
