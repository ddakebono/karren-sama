package org.frostbite.karren.listeners;

import java.util.ArrayList;

import org.frostbite.karren.Logging;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/*
 * This file is 100% specific to the CRaZyPANTS website and will not work with other sites
 * Unless for some reason they have the same setup.
 */
public class SiteCommand extends ListenerAdapter {
	public void onMessage(MessageEvent event) throws Exception{
		String cmd = ".site";
		String message = event.getMessage();
		String[] data = new String[2];
		if(message.startsWith(cmd)){
			message.replaceFirst(cmd, "").trim();
			String[] messageSplit = message.split(" ");
			if(messageSplit.length > 0){
				if(messageSplit[0].equalsIgnoreCase("set") && messageSplit.length > 1){
					if(messageSplit[1].equalsIgnoreCase("alert")){
						if(messageSplit.length > 2 && messageSplit[2].equalsIgnoreCase("on")){
							data[0] = "on";
							MySQLConnector.sqlPush("site", "alert", data);
							Logging.log("Site wide alert enabled!", false);
						} else if(messageSplit.length > 2 && messageSplit[2].equalsIgnoreCase("off")){
							data[0] = "off";
							MySQLConnector.sqlPush("site", "alert", data);
							Logging.log("Site wide alert disabled!", false);
						} else if(messageSplit.length > 2 && messageSplit[2].equalsIgnoreCase("msg")){
							data[0] = "msg";
							//Rebuild message into string
							for(int i=3; i<messageSplit.length-1; i++){
								if(i==3){
									data[1] = messageSplit[i];
								} else {
									data[1] = data[1] + " " + messageSplit[i];
								}
							}
							MySQLConnector.sqlPush("site", "alert", data);
							Logging.log("Alert message set to " + data[1], false);
						}
					}
					if(messageSplit[1].equalsIgnoreCase("maint")){
						if(messageSplit.length > 2 && messageSplit[2].equalsIgnoreCase("on")){
							data[0] = "on";
							MySQLConnector.sqlPush("site", "maint", data);
							Logging.log("Site wide lockdown enabled!", false);
						} else if(messageSplit.length > 2 && messageSplit[2].equalsIgnoreCase("off")){
							data[0] = "off";
							MySQLConnector.sqlPush("site", "maint", data);
							Logging.log("Site wide lockdown disabled!", false);
						}
					}
					if(messageSplit[1].equalsIgnoreCase("test")){
						event.respond("Test");
					}
				}
			} else {
				event.respond("Testing");
			}
		}
	}
}
