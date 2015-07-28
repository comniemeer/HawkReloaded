package uk.co.oliwali.HawkEye;

import java.util.UUID;

public class User {
	
	private UUID uuid;
	private String name;
	
	public User(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String getName() {
		return this.name;
	}
}