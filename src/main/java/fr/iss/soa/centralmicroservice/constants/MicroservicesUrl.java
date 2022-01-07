package fr.iss.soa.centralmicroservice.constants;

public class MicroservicesUrl {
	private final static String LIGHT_BASE_URL = "http://192.168.0.1";
	private final static String PRESENCE_BASE_URL = "http://192.168.43.67:8084";

	public static String getLightsUrl() {
		return LIGHT_BASE_URL + "/lights";
	}

	public static String getLightIdUrl(long id) {
		return getLightsUrl() + "/" + id;
	}

	public static String getPresenceDetectorsListUrl() {
		return PRESENCE_BASE_URL + "/detectors";
	}
}
