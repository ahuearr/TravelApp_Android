package com.gusycorp.travel.util;

public class Constants {

	public final static String DATE_MASK = "dd/MM/yyyy HH:mm";
	public final static String ONLY_DATE_MASK = "dd/MM/yyyy";

	// Adapters
	public final static String TAG_LISTRIPADAPTER = "ListTripAdapter";
	public final static String TAG_LISTRIPTRANSPORTADAPTER = "ListTripTransportAdapter";
	public final static String TAG_LISTRIPACCOMODATIONADAPTER = "ListTripAccommodationAdapter";

	public final static String TAG_TRAVELAPPLICACION = "TravelApplication";

	// Model
	public final static String OBJECTID = "objectId";

	public final static String TYPETRANSPORT_PLANE = "Avion";
	public final static String TYPETRANSPORT_TRAIN = "Tren";
	public final static String TYPETRANSPORT_BUS = "Bus";
	public final static String TYPETRANSPORT_SHIP = "Barco";
	public final static String TYPETRANSPORT_CAR = "Coche";

	// Trip
	public final static String TAG_TRIPMODEL = "Trip";
	public final static String TRIP_TRIPNAME = "tripName";
	public final static String TRIP_DATEINI = "dateIni";
	public final static String TRIP_DATEFIN = "dateFin";
	public final static String TRIP_DESTINYNAME = "destinyName";
	public final static String TRIP_STATUS = "status";
	public final static String TRIP_ORGANIZERID = "organizerId";
	public final static String TRIP_VALUE_STATUS_FUTURE = "Future";
	public final static String TRIP_VALUE_STATUS_PAST = "Past";
	public final static String TRIP_TRIPTRANSPORT = "tripTransport";
	public final static String TRIP_TRIPACCOMMODATION = "tripAccommodation";

	// TripTransport
	public final static String TAG_TRIPTRANSPORTMODEL = "TripTransport";
	public final static String TRIPTRANSPORT_DATEFROM = "dateFrom";
	public final static String TRIPTRANSPORT_DATETO = "dateTo";
	public final static String TRIPTRANSPORT_FROM = "from";
	public final static String TRIPTRANSPORT_TO = "to";
	public final static String TRIPTRANSPORT_PRIZE = "prize";
	public final static String TRIPTRANSPORT_LOCATOR = "locator";
	public final static String TRIPTRANSPORT_TYPETRANSPORT = "typeTransport";

	// TypeTransport
	public final static String TAG_TYPETRANSPORTMODEL = "TypeTransport";
	public final static String TYPETRANSPORT_TRANSPORTNAME = "transportName";
	public final static String TYPETRANSPORT_TRANSPORTIMAGENAME = "transportImageName";

	//TripTransportList
	public static final String TRIPTRANSPORTLIST_COLUMN_ONE="Fecha";
	public static final String TRIPTRANSPORTLIST_COLUMN_TWO="De";
	public static final String TRIPTRANSPORTLIST_COLUMN_THREE="A";
	public static final String TRIPTRANSPORTLIST_COLUMN_FOUR="Medio Transporte";

	// TripAccommodation
	public final static String TAG_TRIPACCOMMODATIONMODEL = "TripAccommodation";
	public final static String TRIPACCOMMODATION_PLACE = "place";
	public final static String TRIPACCOMMODATION_CITY = "city";
	public final static String TRIPACCOMMODATION_ADDRESS = "address";
	public final static String TRIPACCOMMODATION_NUMROOMS = "numRooms";

	//TripaccommodationList
	public static final String TRIPACCOMMODATIONLIST_COLUMN_ONE="Hotel";
	public static final String TRIPACCOMMODATIONLIST_COLUMN_TWO="Ciudad";
	public static final String TRIPACCOMMODATIONLIST_COLUMN_THREE="Entrada";
	public static final String TRIPACCOMMODATIONLIST_COLUMN_FOUR="Salida";

	// Others
	public final static String TAG_UTILS = "Utils";
}
