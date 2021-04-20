package edu.wit.cs.rperkett.finalbot;

import java.util.ArrayList;
import java.util.HashMap;
import com.github.twitch4j.common.events.domain.EventUser;
import java.util.Map;

public class User {
	
	private String username;
	private int user_id;
	private int total_commands_invoked;
	
	HashMap<String, Integer> commands_ran = new HashMap<String, Integer>();
	
	public User() {
		this.username = "unknown";
		this.user_id = 0;
	}
	
	public User(String name) {
		this.username = name;
		this.user_id = 0;
	}
	
	public User(String name, int user_id) {
		this.username = name;
		this.user_id = user_id;
	}
	
	public User(EventUser user) {
		this.username = user.getName();
		this.user_id = Integer.parseInt(user.getId());
	}
	
	/////////////////////////////////////////////// END CONSTRUCTORS //////////////////////////////////////////////
	
	public void addCommand(String command_name) {
		command_name = command_name.toUpperCase(); // Just making sure
		
		int times_ran;
		
		try {
			times_ran = this.commands_ran.get(command_name) + 1;
		} catch(NullPointerException e) {
			times_ran = 1;
		}
		
		this.commands_ran.put(command_name, times_ran);
		this.total_commands_invoked += 1;
	}
	
	
	public String getName() {
		return this.username;
	}
	
	public int getId() {
		return this.user_id;
	}
	
	public int getTotalCommands() {
		return this.total_commands_invoked;
	}
	
	public HashMap<String, Integer> getSpecificCommands() {
		return this.commands_ran;
	}
	
	public String[] getTopThreeCommands() {
		String[] topThree = {"x", "x", "x"};
		try {
			for(int i = 0; i < 3; i++) {
				int times_ran = -1;
				String current_command = "NONE";
				for(Map.Entry<String, Integer> x : this.commands_ran.entrySet()) {
					if((Integer) x.getValue() > times_ran && !(hasValue(String.format("%s: %d", x.getKey().toString(), x.getValue()), topThree))) {
						times_ran = (Integer) x.getValue();
						current_command = x.getKey().toString();
					}
					  
				}
				topThree[i] = String.format("%s: %d", current_command, times_ran);
			}
		} catch (ArrayIndexOutOfBoundsException  e) {
			assert(true); // Do nothing.
		}
		
		return topThree;
	}
	
	public static boolean hasValue(String looking, String[] in) { // Checks if a value is in an array.
		for(int i = 0; i < in.length; i++) {
			if(looking.equals(in[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasSpecificUser(ArrayList<User> users, int id) {
		for(User x: users) {
			if(x.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public static User getSpecificUser(ArrayList<User> users, int id) {
		for(User x: users) {
			if(x.getId() == id) {
				return x;
			}
		}
		return null;
	}
	
	
}