package fr.iss.soa.centralmicroservice;

import java.util.Date;

public class PresenceDetectionEvent {
	private final Date date;

	PresenceDetectionEvent() {
		date = new Date();
	}

	public Date getDate() {
		return date;
	}
}
