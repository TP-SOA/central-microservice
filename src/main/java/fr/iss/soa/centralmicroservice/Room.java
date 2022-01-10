package fr.iss.soa.centralmicroservice;

import fr.iss.soa.centralmicroservice.constants.MicroserviceType;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Room {

	private final long id;
	private final ArrayList<PresenceDetectionEvent> presenceDetectionEvents = new ArrayList<>();
	Timer presenceTimer = new Timer();
	private boolean lightEnabled;
	private boolean alarmEnabled;
	private boolean doorLocked;
	private boolean presenceDetected;

	Room(long id) {
		this.id = id;
		lightEnabled = false;
		alarmEnabled = false;
		doorLocked = false;
	}

	Room(long id, boolean lampEnabled, boolean alarmEnabled, boolean doorLocked) {
		this.id = id;
		this.lightEnabled = lampEnabled;
		this.alarmEnabled = alarmEnabled;
		this.doorLocked = doorLocked;
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

	public void addPresenceDetectionEvent(PresenceDetectionEvent event, PresenceDetectionTimeoutCallback callback) {
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
				callback.onTimeout();
			}
		}, 20 * 1000);
	}

	public boolean isPresenceDetected() {
		return presenceDetected;
	}

	public boolean isAlarmEnabled() {
		return alarmEnabled;
	}

	public void setAlarmEnabled(boolean alarmEnabled) {
		this.alarmEnabled = alarmEnabled;
	}

	public boolean isDoorLocked() {
		return doorLocked;
	}

	public void setDoorLocked(boolean doorLocked) {
		this.doorLocked = doorLocked;
	}

	public void setStatus(MicroserviceType type, boolean status) {
		switch (type) {
			case LIGHT:
				setLightEnabled(status);
				break;
			case ALARM:
				setAlarmEnabled(status);
				break;
			case DOOR:
				setDoorLocked(status);
				break;
		}
	}

	public interface PresenceDetectionTimeoutCallback {
		void onTimeout();
	}
}
