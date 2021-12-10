package fr.iss.soa.centralmicroservice;

import fr.iss.soa.centralmicroservice.errors.RoomNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	private final ArrayList<Room> roomList = new ArrayList<>();
	private final ArrayList<PresenceDetectionEvent> presenceDetectionEvents = new ArrayList<>();

	RestController() {
		roomList.add(new Room(0));
		roomList.add(new Room(1, true, false));
	}

	@GetMapping("/rooms")
	public ArrayList<Room> rooms() {
		return roomList;
	}

	@GetMapping("/rooms/{id}")
	public Room roomId(@PathVariable("id") long id) {
		System.out.println(id);
		Optional<Room> room = roomList.stream().filter(i -> i.getId() == id).findFirst();
		return room.orElseThrow(() -> new RoomNotFoundException(id));
	}

	@PostMapping("/presence-event/{id}")
	public void presenceId(@PathVariable("id") long id) {
		Optional<Room> room = roomList.stream().filter(i -> i.getId() == id).findFirst();
		if (room.isPresent()) {
			presenceDetectionEvents.add(new PresenceDetectionEvent(id));
		} else {
			throw new RoomNotFoundException(id);
		}
	}

}
