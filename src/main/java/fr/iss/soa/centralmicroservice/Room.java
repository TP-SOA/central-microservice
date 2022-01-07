package fr.iss.soa.centralmicroservice;

import java.util.*;

public class Room {

	private final long id;
	private boolean lightEnabled;
	private boolean presenceAvailable;
	private boolean presenceDetected;
	Timer presenceTimer = new Timer();

	private final ArrayList<PresenceDetectionEvent> presenceDetectionEvents = new ArrayList<>();

	Room(long id) {
		this.id = id;
		lightEnabled = false;
		presenceAvailable = false;
	}

	Room(long id, boolean lampEnabled) {
		this.id = id;
		this.lightEnabled = lampEnabled;
		presenceAvailable = false;
	}

	public long getId() {
		return id;
	}

	public boolean isLightEnabled() {
		return lightEnabled;
	}

	public void setLightEnabled(boolean lightEnabled) {
		this.lightEnabled = lightEnabled;
	}

	public boolean isPresenceAvailable() {
		return presenceAvailable;
	}

	public void setPresenceAvailable(boolean presenceAvailable) {
		this.presenceAvailable = presenceAvailable;
	}

	public void addPresenceDetectionEvent(PresenceDetectionEvent event) {
		if (presenceAvailable) {
			presenceDetectionEvents.add(event);
			System.out.println("Adding detection event for " + id);
			presenceDetected = true;
			presenceTimer.cancel();
			presenceTimer = new Timer();
			presenceTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("Disabling detection state for " + id);
					presenceDetected = false;
				}
			}, 20 * 1000);
		}
	}

	public boolean isPresenceDetected() {
		return presenceDetected;
	}
}
