/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TalkToCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event) throws IOException, SQLException{
		String[] hiCheck = {"Hi", "Hello", "Hey", "Yo", "Greeting", "Salutations" };
		String[] byeCheck = {"Bye", "Cya", "Good bye", "Goodbye"};
		String[] randCheck = {"Pick", "Choose", "Select", "Draw"};
		boolean goodbye = false;
		boolean hello = false;
		boolean isAway = false;
		boolean random = false;
		String result;
		String[] data = new String[1];
		ArrayList<String> resultData = new ArrayList<String>();
		for(String checkAway : GlobalVars.awayUser){
			if(event.getUser().getNick().equalsIgnoreCase(checkAway)){
				isAway = true;
				hello=true;
			}
		}
		if(event.getMessage().toString().toLowerCase().contains(GlobalVars.botname.toLowerCase()) || isAway){
			for(String check : byeCheck){
				if(event.getMessage().toLowerCase().startsWith(check.toLowerCase())){
					goodbye = true;
				}
			}
			for(String check : hiCheck){
				if(event.getMessage().toLowerCase().startsWith(check.toLowerCase())){
					hello = true;
				}
			}
			if(event.getMessage().toLowerCase().startsWith("clayton")){
				event.getChannel().send().message("Clayton");
			}
			for(String check : randCheck){
				if(event.getMessage().toLowerCase().contains(check.toLowerCase()) && event.getMessage().toLowerCase().contains("one")){
					random = true;
				}
			}
			if(event.getMessage().toLowerCase().contains("wow wow")){
				event.getChannel().send().message("JUST LIVING IN THE DATABASE WOW WOW WOW WOW!");
			}
		}
		if(goodbye){
			event.getChannel().send().message("Good bye " + event.getUser().getNick() + ".");
			data[0] = event.getUser().getNick();
			MySQLConnector.sqlPush("part", "", data);
			GlobalVars.awayUser.add(event.getUser().getNick());
		}
		if(hello&&!goodbye){
			data[0] = event.getUser().getNick();
			resultData.addAll(MySQLConnector.sqlPush("part", "back", data));
			if(resultData.get(0) != null){
				event.getChannel().send().message("Hello " + event.getUser().getNick() + ", you have been away for " + calcAway(resultData.get(0)));
			} else {
				event.getChannel().send().message("Hello " + event.getUser().getNick() + ", remember to say good bye when you leave!");
			}
			GlobalVars.awayUser.remove(event.getUser().getNick());
		}
		if(random){
			String[] tempArray = event.getMessage().split(":");
			String msgOut = "";
			if(tempArray.length > 2){
				random = false;
				event.respond("You can only have one colon in the random list, it must seperate your question and your list.");
			} else if(tempArray.length < 2){
				random = false;
				event.respond("You must have a list of objects for me to select from!");
			}
			if(random){
				msgOut = tempArray[1];
				result = randomList(msgOut);
				event.respond(result);
			}
		}
		
	}
    public static String randomList(String message){
        String[] choiceSet = message.split("[\\s*],[\\s*]");
        String choice = "";
        int random = new Random().nextInt(choiceSet.length);
        choice = choiceSet[random];
        return choice;
    }
	public static String calcAway(String leaveDate){
		String backTime = "0";
		long diffTime = 0;
		long seconds = 0;
		long minutes = 0;
		long hours = 0;
		long days = 0;
		Date date = new Date();
		diffTime = date.getTime()-Long.parseLong(leaveDate);
		seconds = diffTime/1000;
		if(seconds>=60){
			minutes = seconds/60;
			seconds = seconds-(minutes*60);
		}
		if(minutes>=60){
			hours = minutes/60;
			minutes = minutes-(hours*60);
		}
		if(hours>=24){
			days = hours/24;
			hours = hours-(days*24);
		}
		backTime = days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds.";
		return backTime;
	}

}