package fr.iss.soa.centralmicroservice.errors;

public class RoomNotFoundException extends RuntimeException {

	public RoomNotFoundException(long id) {
		super("Could not find room " + id);
	}
}
