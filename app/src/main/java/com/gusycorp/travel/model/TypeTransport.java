package com.gusycorp.travel.model;

import com.gusycorp.travel.util.Constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class TypeTransport extends ITObject {

	private static String TABLENAME = Constants.TAG_TYPETRANSPORTMODEL;
	private CloudObject typeTransport;

	public TypeTransport(){
		typeTransport = new CloudObject(TABLENAME);
	}

	public TypeTransport(CloudObject typeTransport){
		this.typeTransport = typeTransport;
	}

	public CloudObject getTypeTransport(){
		return typeTransport;
	}

	public void setTypeTransport(CloudObject typeTransport){
		this.typeTransport = typeTransport;
	}

	public String getId() {
		return typeTransport.getId();
	}

	public String getTransportName() {
		return typeTransport.getString(Constants.TRANSPORTNAME);
	}

	public void setTransportName(String transportName) throws CloudException {
		typeTransport.set(Constants.TRANSPORTNAME, transportName);
	}

	public String getTransportImageName() {
		return typeTransport.getString(Constants.TRANSPORTIMAGENAME);
	}

	public void setTransportImageName(String transportImageName) throws CloudException {
		typeTransport.set(Constants.TRANSPORTIMAGENAME, transportImageName);
	}

	public static void findTypeTransportInBackground(String objectId, final CloudObjectCallback callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		query.findById(objectId, new CloudObjectCallback(){
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
			Map<String, Object> filter, final CloudObjectArrayCallback callback) throws CloudException {
		CloudQuery query = new CloudQuery(TABLENAME);
		Iterator it = filter.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			query.equalTo((String) e.getKey(), e.getValue());
		}
		query.find(new CloudObjectArrayCallback() {
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
