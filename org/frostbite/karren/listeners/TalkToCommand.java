package org.frostbite.karren.listeners;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TalkToCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws IOException, SQLException{
		String[] hiCheck = {"Hi", "Hello", "Hey", "Yo", "Greeting", "Salutations" };
		String[] byeCheck = {"Bye", "Cya", "Good bye", "Goodbye"};
		boolean goodbye = false;
		boolean hello = false;
		boolean isAway = false;
		String[] data = new String[1];
		ArrayList<String> resultData = new ArrayList<String>();
		PircBotX bot = event.getBot();
		int awayTime = 0;
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
				bot.sendMessage(event.getChannel(),"Clayton");
			}
		}
		if(goodbye){
			bot.sendMessage(event.getChannel(),"Good bye " + event.getUser().getNick() + ".");
			data[0] = event.getUser().getNick();
			MySQLConnector.sqlPush("part", "", data);
			GlobalVars.awayUser.add(event.getUser().getNick());
		}
		if(hello){
			data[0] = event.getUser().getNick();
			resultData.addAll(MySQLConnector.sqlPush("part", "back", data));
			if(resultData.get(0) != null){
				bot.sendMessage(event.getChannel(),"Hello " + event.getUser().getNick() + ", you have been away for " + calcAway(resultData.get(0)));
			} else {
				bot.sendMessage(event.getChannel(), "Hello " + event.getUser().getNick() + ", remember to say good bye when you leave!");
			}
			GlobalVars.awayUser.remove(event.getUser().getNick());
		}
		
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