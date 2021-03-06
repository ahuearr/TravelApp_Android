package com.gusycorp.travel.util;

public class Constants {

	public final static String DATE_MASK_DATABASE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public final static String DATE_MASK = "dd-MM-yyyy HH:mm";
	public final static String ONLY_DATE_MASK = "dd-MM-yyyy";

	// Adapters
	public final static String TAG_LISTRIPADAPTER = "ListTripAdapter";
	public final static String TAG_LISTRIPMATEADAPTER = "ListTripMateAdapter";
	public final static String TAG_LISTRIPACTIVITIESADAPTER = "ListTripActivitiesAdapter";
	public final static String TAG_LISTRIPACCOMODATIONADAPTER = "ListTripAccommodationAdapter";
	public final static String TAG_LISTRIPCALENDARADAPTER = "ListTripCalendarAdapter";

	public final static String TAG_TRAVELAPPLICACION = "TravelApplication";

	// Model
	public final static String ID = "id";
	public final static String OBJECTID = "objectId";

	// Trip
	public final static String TAG_TRIPMODEL = "Trip";
	public final static String TRIPNAME = "tripName";
	public final static String DATEINI = "dateIni";
	public final static String DATEFIN = "dateFin";
	public final static String DESTINYNAME = "destinyName";
	public final static String STATUS = "status";
	public final static String ORGANIZERID = "organizerId";
	public final static String VALUE_STATUS_FUTURE = "Future";
	public final static String VALUE_STATUS_PAST = "Past";
	public final static String TRIPTRANSPORT = "tripTransport";
	public final static String TRIPACCOMMODATION = "tripAccommodation";
	public final static String TRIPCALENDAR = "tripCalendar";
	public final static String TRIPMATE = "tripMate";
	public final static String USER = "user";
	public final static String TRIPID = "tripId";

	// TripTransport
	public final static String TAG_TRIPTRANSPORTMODEL = "TripTransport";
	public final static String DATEFROM = "dateFrom";
	public final static String DATETO = "dateTo";
	public final static String FROM = "from";
	public final static String TO = "to";
	public final static String PRIZE = "prize";
	public final static String LOCATOR = "locator";
	public final static String TYPETRANSPORT = "typeTransport";
	public final static String LATITUDEFROM = "latitudeFrom";
	public final static String LATITUDETO = "latitudeTo";
	public final static String LONGITUDEFROM = "longitudeFrom";
	public final static String LONGITUDETO = "longitudeTo";
	public final static String TRIPMATEPRIZE = "tripMatePrize";

	// TypeTransport
	public final static String TAG_TYPETRANSPORTMODEL = "TypeTransport";
	public final static String TRANSPORTNAME = "transportName";
	public final static String TRANSPORTIMAGENAME = "transportImageName";

	//TripTransportList
	public static final String TRIPTRANSPORTLIST_COLUMN_ONE="Fecha";
	public static final String TRIPTRANSPORTLIST_COLUMN_TWO="De";
	public static final String TRIPTRANSPORTLIST_COLUMN_THREE="A";
	public static final String TRIPTRANSPORTLIST_COLUMN_FOUR="Medio Transporte";

	// TripAccommodation
	public final static String TAG_TRIPACCOMMODATIONMODEL = "TripAccommodation";
	public final static String PLACE = "place";
	public final static String CITY = "city";
	public final static String ADDRESS = "address";
	public final static String NUMROOMS = "numRooms";
	public final static String LATITUDE = "latitude";
	public final static String LONGITUDE = "longitude";

	//TripAccommodationList
	public static final String TRIPACCOMMODATIONLIST_COLUMN_ONE="Hotel";
	public static final String TRIPACCOMMODATIONLIST_COLUMN_TWO="Ciudad";
	public static final String TRIPACCOMMODATIONLIST_COLUMN_THREE="Entrada";
	public static final String TRIPACCOMMODATIONLIST_COLUMN_FOUR="Salida";

	// TripCalendar
	public final static String TAG_TRIPCALENDARMODEL = "TripCalendar";
	public final static String DATE = "date";
	public final static String ACTIVITY = "activity";
	public final static String ISACTIVITY = "isActivity";

	//TripCalendarList
	public static final String TRIPCALENDARLIST_COLUMN_ONE="Fecha";
	public static final String TRIPCALENDARLIST_COLUMN_TWO="Actividad";

	// TripMate
	public final static String TAG_TRIPMATEMODEL = "TripMate";
	public final static String USERID = "userId";
	public final static String ORGANIZER = "isOrganizer";

	// TripMatePrize
	public final static String TAG_TRIPMATEPRIZEMODEL = "TripMatePrize";
	public final static String TRIPMATEID = "tripMateId";
	public final static String PARENTID = "parentId";
	public final static String PARENTTYPE = "parentType";

	//User
	public final static String USERNAME = "username";

	// Others
	public final static String TAG_UTILS = "Utils";
	public final static int SAVE=0;
	public final static int UPDATE=1;

	public final static String PARENTTYPE_TRANSPORT = "T";
	public final static String PARENTTYPE_ACCOMODATION = "A";
	public final static String PARENTTYPE_CALENDAR = "C";

}
