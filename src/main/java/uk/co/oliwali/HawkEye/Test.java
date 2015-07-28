package uk.co.oliwali.HawkEye;

import java.util.HashMap;
import java.util.UUID;

public class Test {
	
	public static void main(String[] args) {
		HashMap<User, Integer> users = new HashMap<User, Integer>();
		UUID uuid = UUID.randomUUID();
		User user1 = new User(uuid, "comniemeer");
		
		users.put(user1, 0);
		
		User user2 = new User(uuid, "comniemeer");
		
		System.out.println(users.containsKey(user1));
		System.out.println(users.containsKey(user2));
	}
}