package fr.iss.soa.centralmicroservice;

import com.netflix.discovery.EurekaClient;
import fr.iss.soa.centralmicroservice.constants.MicroserviceType;
import fr.iss.soa.centralmicroservice.errors.RoomNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	private EurekaClient eurekaClient;

	ServicesController servicesController;

	@PostConstruct
	public void init() {
		servicesController = new ServicesController(eurekaClient);
	}


	@GetMapping("/rooms")
	public ArrayList<Room> rooms() {
		return servicesController.getRoomList();
	}

	@GetMapping("/rooms/{id}")
	public Room roomId(@PathVariable("id") long id) {
		Optional<Room> room = servicesController.getRoomOfId(id);
		return room.orElseThrow(() -> new RoomNotFoundException(id));
	}

	@PostMapping("/presence-event/{id}")
	public void presenceId(@PathVariable("id") long id) {
		Optional<Room> room = servicesController.getRoomOfId(id);
		if (room.isPresent()) {
			servicesController.addPresenceDetectionEvent(id);
		} else {
			throw new RoomNotFoundException(id);
		}
	}

	@PostMapping(value="/alarms/{id}")
	public void setAlarm(@PathVariable int id, @RequestBody String requestBody) {
		JSONObject jsonBody = new JSONObject(requestBody);
		try {
			boolean enabled = jsonBody.getBoolean("enabled");
			Optional<Room> room = servicesController.getRoomOfId(id);
			room.ifPresent(value -> servicesController.toggleMicroservice(value, MicroserviceType.ALARM, enabled));
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}

}
