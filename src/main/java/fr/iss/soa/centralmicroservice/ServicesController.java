package fr.iss.soa.centralmicroservice;

import fr.iss.soa.centralmicroservice.constants.MicroserviceType;
import fr.iss.soa.centralmicroservice.constants.MicroservicesUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

public class ServicesController {
	private final ArrayList<Room> roomList = new ArrayList<>();

	ServicesController() {
		initRoomList();
	}

	private void initRoomList() {
		Thread t = new Thread(() -> {
			fetchMicroservice(MicroserviceType.LIGHT);
			fetchMicroservice(MicroserviceType.ALARM);
			fetchMicroservice(MicroserviceType.DOOR);
		});
		t.start();
	}

	private void toggleMicroservice(Room room, MicroserviceType type, boolean enabled) {
		RestTemplate restTemplate = new RestTemplate();
		String url = MicroservicesUrl.getServiceIdUrl(type, room.getId());
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
		String url = MicroservicesUrl.getServiceUrl(type);

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
				value.addPresenceDetectionEvent(new PresenceDetectionEvent(), () ->
						toggleMicroservice(value, MicroserviceType.LIGHT, false));
			}
		});
	}
}
