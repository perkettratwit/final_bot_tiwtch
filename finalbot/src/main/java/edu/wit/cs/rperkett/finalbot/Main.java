package edu.wit.cs.rperkett.finalbot;


import java.util.ArrayList;
import java.util.HashMap;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;



public class Main {
	
	static ArrayList<User> participants = new ArrayList<>();
	
	final static String[] VALID_COMMANDS = {
			"ENTER", "E",
			"UP", "U",
			"DOWN", "D",
			"LEFT", "L",
			"RIGHT", "R",
			"CANCEL",
			"BACK", "B",
			"MENU",
			"BOOST UP", "O",
			"BOOST DOWN", "Q"
	};
	
    public static void main(String[] args) {
    	OAuth2Credential credential = new OAuth2Credential("twitch", "oauth:8jgwxhaze1f5js3z7g25khfj0z0snk"); // Class definition!
    
    	TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withChatAccount(credential)
            .build();
    	
    	twitchClient.getChat().joinChannel("caldraeus");
    	
    	twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> { // System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage())
    		  handle_message(event);
    		});
    }	
    
    public static int[] get_key_code(String s) {
    	int[] key_codes = {9999,9999}; // Up to two keystrokes.
    	if(s.equals("ENTER") || s.equals("E")) {
    		key_codes[0] = KeyEvent.VK_ENTER;
    	}else if(s.equals("UP") || s.equals("U")) {
    		key_codes[0] = KeyEvent.VK_W;
    	}else if(s.equals("DOWN") || s.equals("D")) {
    		key_codes[0] = KeyEvent.VK_S;
    	}else if(s.equals("LEFT") || s.equals("L")) {
    		key_codes[0] = KeyEvent.VK_A;
    	}else if(s.equals("RIGHT") || s.equals("R")) {
    		key_codes[0] = KeyEvent.VK_D;
    	}else if(s.equals("CANCEL") || s.equals("BACK") || s.equals("B")){
    		key_codes[0] = KeyEvent.VK_BACK_SPACE;
    	}else if(s.equals("MENU")) {
    		key_codes[0] = KeyEvent.VK_ESCAPE;
    	}else if(s.equals("BOOST UP") || s.equals("O")) {
    		key_codes[0] = KeyEvent.VK_O;
    	}else if(s.equals("BOOST DOWN") || s.equals("Q")) {
    		key_codes[0] = KeyEvent.VK_Q;
    	}else if(s.equals("J") || s.equals("JAY")) {
    		key_codes[0] = KeyEvent.VK_J;
    		//
    	}
    	
    	return key_codes;
    }
    
    public static void press(String key_code) {
    	try {
            Robot r = new Robot();
            int[] keys = get_key_code(key_code);
            
            if(keys[0] != 9999 && keys[1] == 9999) {
            	r.keyPress(keys[0]);
            	r.delay(300);
            	r.keyRelease(keys[0]);
                
            	
            }else if(keys[0] != 9999 && keys[1] != 9999) {
            	r.keyPress(keys[0]);
            	r.keyPress(keys[1]);
                r.delay(300);
                r.keyRelease(keys[0]);
                r.keyRelease(keys[1]);
            }
            else {
            	System.out.println("ERROR: Invalid Keycode");
            }
            
        } catch (AWTException e) {
            e.printStackTrace();
        }
    	
    }
    
    public static boolean is_valid(String s) {
    	for(String x: VALID_COMMANDS) {
    		if(x.equals(s)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static void handle_message(ChannelMessageEvent e) {
    	String message_content = e.getMessage();
    	
    	if(is_valid(message_content)) {
    		press(e.getMessage());
    		
    		if(User.getSpecificUser(participants, Integer.parseInt(e.getUser().getId())) != null) {
        		User existing_user = User.getSpecificUser(participants, Integer.parseInt(e.getUser().getId()));
        		existing_user.addCommand(e.getMessage());
        	}else {
        		participants.add(new User(e.getUser()));
        	}
    	}else {
    		if(User.getSpecificUser(participants, Integer.parseInt(e.getUser().getId())) == null) {
    			participants.add(new User(e.getUser()));
    		}
    		
    		User checking_user = User.getSpecificUser(participants, Integer.parseInt(e.getUser().getId()));
    		
    		if(checking_user.getName().toLowerCase().equals("caldraeus")) {
    			if(e.getMessage().toLowerCase().equals("!topuser")) {
    				User topUser = participants.get(0);
    				for(int i = 0; i < participants.size(); i++) {
    					if(participants.get(0).getTotalCommands() > topUser.getTotalCommands()) {
    						topUser = participants.get(0);
    					}
    				}
    				
    				System.out.printf("Most Engaged User: %s - %d Commands Ran%n", topUser.getName(), topUser.getTotalCommands());
    			}
    		}
    	}
    }
}