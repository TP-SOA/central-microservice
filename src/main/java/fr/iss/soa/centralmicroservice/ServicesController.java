package fr.iss.soa.centralmicroservice;

import fr.iss.soa.centralmicroservice.constants.MicroservicesUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

public class ServicesController {
	private final ArrayList<Room> roomList = new ArrayList<>();

	ServicesController() {
		initRoomList();
	}

	private void initRoomList() {
		RestTemplate restTemplate = new RestTemplate();
		Thread t1 = new Thread(() -> {
//			String lightsResult = restTemplate.getForObject(MicroservicesUrl.getLightsUrl(), String.class);
//			System.out.println(lightsResult);
			roomList.add(new Room(11));
			roomList.add(new Room(114));
			roomList.add(new Room(116));
			roomList.add(new Room(7));
			roomList.add(new Room(213));
			roomList.add(new Room(1));
			try {
				// TODO debug
				// Build the room list from JSON response
//				JSONArray jsonLightsResponse = new JSONArray(lightsResult);
//				jsonLightsResponse.forEach(light -> {
//					JSONObject jsonLight = (JSONObject) light;
//					long id = jsonLight.getLong("id");
//					boolean enabled = jsonLight.getBoolean("enabled");
//					roomList.add(new Room(id, enabled));
//				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

			String detectorsResult = restTemplate.getForObject(MicroservicesUrl.getPresenceDetectorsListUrl(), String.class);
			System.out.println(detectorsResult);
			try {
				// Set if rooms have a presence detector
				JSONArray jsonDetectorsResponse = new JSONArray(detectorsResult);
				jsonDetectorsResponse.forEach(detector -> {
					JSONObject jsonDetector = (JSONObject) detector;
					long id = jsonDetector.getLong("id");
					Optional<Room> room = getRoomOfId(id);
					room.ifPresent(value -> value.setPresenceAvailable(true));
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		});
		t1.start();
	}

	public Optional<Room> getRoomOfId(long id) {
		return roomList.stream().filter(i -> i.getId() == id).findFirst();
	}

	public ArrayList<Room> getRoomList() {
		return roomList;
	}

	public void addPresenceDetectionEvent(long roomId) {
		Optional<Room> room = getRoomOfId(roomId);
		room.ifPresent(value -> value.addPresenceDetectionEvent(new PresenceDetectionEvent()));
	}

}
