package fr.iss.soa.centralmicroservice.constants;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

public class MicroservicesUrl {
	private final static String LIGHT_SERVICE = "light-microservice";
	private final static String ALARM_SERVICE = "alarm-microservice";
	private final static String DOOR_SERVICE = "door-microservice";
	private final static String PRESENCE_SERVICE = "presence-microservice";

	private final EurekaClient eurekaClient;

	public MicroservicesUrl(EurekaClient eurekaClient) {
		this.eurekaClient = eurekaClient;
	}

	private String getBaseServiceUrl(String serviceName) {
		InstanceInfo service = eurekaClient
				.getApplication(serviceName)
				.getInstances()
				.get(0);

		String hostName = service.getHostName();
		int port = service.getPort();
		return "http://" + hostName + ":" + port;
	}

	public String getLightsUrl() {
		return getBaseServiceUrl(LIGHT_SERVICE) + "/lights";
	}

	public String getAlarmsUrl() {
		return getBaseServiceUrl(ALARM_SERVICE) + "/alarms";
	}

	public String getDoorsUrl() {
		return getBaseServiceUrl(DOOR_SERVICE) + "/doors";
	}

	public String getBaseServiceUrl(MicroserviceType type) {
		String url = null;
		switch (type) {
			case LIGHT:
				url = getLightsUrl();
				break;
			case ALARM:
				url = getAlarmsUrl();
				break;
			case DOOR:
				url = getDoorsUrl();
				break;
		}
		return url;
	}

	public String getLightIdUrl(long id) {
		return getLightsUrl() + "/" + id;
	}

	public String getAlarmIdUrl(long id) {
		return getAlarmsUrl() + "/" + id;
	}

	public String getDoorIdUrl(long id) {
		return getDoorsUrl() + "/" + id;
	}

	public String getServiceIdUrl(MicroserviceType type, long  id) {
		String url = null;
		switch (type) {
			case LIGHT:
				url = getLightIdUrl(id);
				break;
			case ALARM:
				url = getAlarmIdUrl(id);
				break;
			case DOOR:
				url = getDoorIdUrl(id);
				break;
		}
		return url;
	}
}
