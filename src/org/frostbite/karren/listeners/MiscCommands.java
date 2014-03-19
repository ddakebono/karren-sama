/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.Logging;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class MiscCommands extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event){
		String[] cmds = {"brad", "echo", "isgay", "origin", "version", "help", "faves", "fave", "dj", "np", "test"};
		String message = event.getMessage();
		String cmd = "";
		if(message.startsWith(".")){
			message = message.replaceFirst(".", "").trim();
			for(String check : cmds){
				if(message.toLowerCase().startsWith(check)){
					cmd = check;
					message = message.replaceFirst("(?i)" + check, "").trim();
				}
			}
			switch(cmd){
				case "brad":
					event.respond("poop");
					break;
				case "echo":
					event.respond(message.trim());
					break;
				case "isgay":
					event.getChannel().send().message("Wow, " + message.trim() + " is so fucking gaaaaaaaaay!");
					break;
				case "origin":
					event.respond("Wow Origin is fucking terrible, you should probably just kill yourself.");
					break;
				case "version":
					event.getChannel().send().message(event.getBot().getNick() + " is running version " + ((KarrenBot)event.getBot()).getBotConf().getConfigPayload("version"));
					break;
				case "help":
					event.getUser().send().message(event.getBot().getNick() + " bot commands. (All commands are proceded by a . (Ex. .help))");
					event.getUser().send().message(".help command - Prints out this message.");
					event.getUser().send().message(".isgay command - Sends a message to the server calling whatever follows .isgay gay(Ex. .isgay Seth)");
					event.getUser().send().message(".kill command - Only operators on the channel can use this. Kills the bot.");
					event.getUser().send().message(".news command - user must atleast have voice (+v) in the channel. Posts a news update to the CRaZyPANTS website.");
					event.getUser().send().message(".np command - displays the currently playing song. (on/off change auto update mode)");
					event.getUser().send().message(".origin command - Replies to you stating that Origin is bad.");
					event.getUser().send().message(event.getBot().getNick() + ", [pick, choose, select, draw] one: 1,2,3,4,etc - The list of entries can contain anything and can include spaces as long as the entries are seperated by a comma.");
					event.getUser().send().message(".echo command - Replies to you with an echo of whatever follows the command. (Ex. .echo Stuff)");
					event.getUser().send().message(".topic command - Sets the MOTD section of the topic with whatever follows the command.");
					event.getUser().send().message(".version command - replies with current version of the bot.");
					event.getUser().send().message("(Hello, goodbye, clayton) " + event.getBot().getNick() + " - Replies with different responses, hello and goodbye support many alternative words.");
                    event.getUser().send().message(".faves (start/stop), Enables or Disables the fave alerting system, defaults to enabled.");
					event.getUser().send().message(".brad command - poop");
					break;
                case "dj":
                    if(!GlobalVars.iceDJ.equalsIgnoreCase("noone")){
                        event.getChannel().send().message("Current DJ is " + GlobalVars.iceDJ);
                    } else {
                        event.getChannel().send().message("The stream is currently offline...");
                    }
                    break;
                case "np":
                    if(message.startsWith("switch") && event.getUser().isIrcop()){
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
			}
		}
	}
}
