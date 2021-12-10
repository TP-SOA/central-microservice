package fr.iss.soa.centralmicroservice;

import java.util.Date;

public class PresenceDetectionEvent {
	private final long id;
	private final Date date;

	PresenceDetectionEvent(long id) {
		this.id = id;
		date = new Date();
	}

	public Date getDate() {
		return date;
	}
}
