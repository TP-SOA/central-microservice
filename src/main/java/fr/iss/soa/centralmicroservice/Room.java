package fr.iss.soa.centralmicroservice;

public class Room {

	private final long id;
	private final boolean lampEnabled;
	private final boolean presenceDetected;

	Room(long id) {
		this.id = id;
		lampEnabled = false;
		presenceDetected = false;
	}

	Room(long id, boolean lampEnabled, boolean presenceDetected) {
		this.id = id;
		this.lampEnabled = lampEnabled;
		this.presenceDetected = presenceDetected;
	}

	public long getId() {
		return id;
	}

	public boolean isLampEnabled() {
		return lampEnabled;
	}

	public boolean isPresenceDetected() {
		return presenceDetected;
	}
}
