/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import java.util.ArrayList;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.Logging;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class NPCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		String message = event.getMessage();
		String cmd = ".np";
		String voices = event.getChannel().getVoices().toString();
		boolean hasVoice = voices.contains(event.getUser().getNick());
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			if(message.startsWith("switch") && (event.getUser().isIrcop() || hasVoice)){
                if(!GlobalVars.loop){
                    GlobalVars.loop = true;
                    GlobalVars.npChannel = event.getChannel();
                    Logging.log("Now Playing enabled by " + event.getUser().getNick(), false);
                    event.getChannel().send().message("Automagic now playing is now active, periodic updates of the now playing will occur.");
                } else {
                    GlobalVars.loop = false;
                    GlobalVars.npChannel = null;
                    Logging.log("Now Playing disabled by " + event.getUser().getNick(), false);
                }
			} else {
				if(!GlobalVars.iceDJ.equalsIgnoreCase("noone")){
					event.getChannel().send().message("Now playing: \"" + GlobalVars.npSong + "\" On CRaZyRADIO ("+ GlobalVars.iceStreamTitle +"). Listeners: " + GlobalVars.iceListeners + "/" + GlobalVars.iceMaxListeners + ". Faves: " + GlobalVars.songFavCount + ". Plays: " + GlobalVars.songPlayedAmount);
				} else {
					event.getChannel().send().message("The stream is currently offline...");
				}
			}
		} else {
			if(message.startsWith(".dj")){
				if(!GlobalVars.iceDJ.equalsIgnoreCase("noone")){
					event.getChannel().send().message("Current DJ is " + GlobalVars.iceDJ);
				} else {
					event.getChannel().send().message("The stream is currently offline...");
				}
			} else { 
				if(message.startsWith(".fave") && !GlobalVars.iceDJ.equalsIgnoreCase("noone")){
					ArrayList<String> returned = new ArrayList<String>();
					String[] data = new String[2];
					data[0] = String.valueOf(GlobalVars.songID);
					data[1] = event.getUser().getNick();
					returned.addAll(MySQLConnector.sqlPush("song", "fave", data));
					if(Integer.parseInt(returned.get(0))==1){
						event.getUser().send().message("Favorited: " + GlobalVars.npSong);
					} else {
						event.getUser().send().message("I know you like this song but you've already favorited it!");
					}
				} else if(message.startsWith(".fave")){
					event.getUser().send().message("Sorry but the stream is down, you cannot favorite thing while nothing is playing!");
				}
			}
		}
	}
}
