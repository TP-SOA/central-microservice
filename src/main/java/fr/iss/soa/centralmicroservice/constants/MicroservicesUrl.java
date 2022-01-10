package fr.iss.soa.centralmicroservice.constants;

public class MicroservicesUrl {
	private final static String LIGHT_BASE_URL = "http://127.0.0.1:8081";
	private final static String ALARM_BASE_URL = "http://127.0.0.1:8082";
	private final static String DOOR_BASE_URL = "http://127.0.0.1:8083";
	private final static String PRESENCE_BASE_URL = "http://127.0.0.1:8084";

	public static String getLightsUrl() {
		return LIGHT_BASE_URL + "/lights";
	}

	public static String getAlarmsUrl() {
		return ALARM_BASE_URL + "/alarms";
	}

	public static String getDoorsUrl() {
		return DOOR_BASE_URL + "/doors";
	}

	public static String getServiceUrl(MicroserviceType type) {
		String url = null;
		switch (type) {
			case LIGHT:
				url = MicroservicesUrl.getLightsUrl();
				break;
			case ALARM:
				url = MicroservicesUrl.getAlarmsUrl();
				break;
			case DOOR:
				url = MicroservicesUrl.getDoorsUrl();
				break;
		}
		return url;
	}

	public static String getLightIdUrl(long id) {
		return getLightsUrl() + "/" + id;
	}

	public static String getAlarmIdUrl(long id) {
		return getAlarmsUrl() + "/" + id;
	}

	public static String getDoorIdUrl(long id) {
		return getDoorsUrl() + "/" + id;
	}

	public static String getServiceIdUrl(MicroserviceType type, long  id) {
		String url = null;
		switch (type) {
			case LIGHT:
				url = MicroservicesUrl.getLightIdUrl(id);
				break;
			case ALARM:
				url = MicroservicesUrl.getAlarmIdUrl(id);
				break;
			case DOOR:
				url = MicroservicesUrl.getDoorIdUrl(id);
				break;
		}
		return url;
	}

	public static String getPresenceDetectorsListUrl() {
		return PRESENCE_BASE_URL + "/detectors";
	}
}
