package fr.iss.soa.centralmicroservice;

import com.netflix.discovery.EurekaClient;
import fr.iss.soa.centralmicroservice.constants.MicroserviceType;
import fr.iss.soa.centralmicroservice.constants.MicroservicesUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class ServicesController {
	private final ArrayList<Room> roomList = new ArrayList<>();

	private final MicroservicesUrl microservicesUrl;

	ServicesController(EurekaClient eurekaClient) {
		initRoomList();
		this.microservicesUrl = new MicroservicesUrl(eurekaClient);
	}

	private void initRoomList() {
		Timer t = new Timer();
		// Update the microservice data every 5 seconds
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				fetchMicroservice(MicroserviceType.LIGHT);
				fetchMicroservice(MicroserviceType.ALARM);
				fetchMicroservice(MicroserviceType.DOOR);
			}
		}, 0, 5 * 1000);
	}

	public void toggleMicroservice(Room room, MicroserviceType type, boolean enabled) {
		RestTemplate restTemplate = new RestTemplate();
		String url = microservicesUrl.getServiceIdUrl(type, room.getId());
		String requestJson = "{\"enabled\": " + enabled + "}";
		if (type == MicroserviceType.DOOR) {
			requestJson = "{\"locked\": " + enabled + "}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity(requestJson, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			room.setStatus(type, enabled);
		}
	}

	private void fetchMicroservice(MicroserviceType type) {
		RestTemplate restTemplate = new RestTemplate();
		String url = microservicesUrl.getBaseServiceUrl(type);
		try {
			String results = restTemplate.getForObject(url, String.class);
			System.out.println(results);
			try {
				// Build the room list from JSON response
				JSONArray jsonResponse = new JSONArray(results);
				jsonResponse.forEach(data -> {
					JSONObject json = (JSONObject) data;
					long id = json.getLong("id");
					boolean enabled;
					if (type == MicroserviceType.DOOR) {
						enabled = json.getBoolean("locked");
					} else {
						enabled = json.getBoolean("enabled");
					}
					Optional<Room> room = getRoomOfId(id);
					if (room.isPresent()) {
						room.get().setStatus(type, enabled);
					} else {
						switch (type) {
							case LIGHT:
								roomList.add(new Room(id, enabled, false, false));
								break;
							case ALARM:
								roomList.add(new Room(id, false, enabled, false));
								break;
							case DOOR:
								roomList.add(new Room(id, false, false, enabled));
								break;
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (ResourceAccessException e) {
			e.printStackTrace();
		}
	}

	public Optional<Room> getRoomOfId(long id) {
		return roomList.stream().filter(i -> i.getId() == id).findFirst();
	}

	public ArrayList<Room> getRoomList() {
		return roomList;
	}

	public void addPresenceDetectionEvent(long roomId) {
		Optional<Room> room = getRoomOfId(roomId);
		room.ifPresent(value -> {
			if (value.isDoorLocked()) {
				toggleMicroservice(value, MicroserviceType.ALARM, true);
			} else {
				toggleMicroservice(value, MicroserviceType.LIGHT, true);
			}
			value.addPresenceDetectionEvent(new PresenceDetectionEvent(), () ->
					toggleMicroservice(value, MicroserviceType.LIGHT, false));
		});
	}
}
