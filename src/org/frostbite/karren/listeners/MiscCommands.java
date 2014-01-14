/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class MiscCommands extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event){
		String[] cmds = {"brad", "echo", "isgay", "origin", "version", "help", "debug"};
		String message = event.getMessage();
		String cmd = "";
		if(message.startsWith(".")){
			message.replaceFirst(".", "");
			for(String check : cmds){
				if(message.toLowerCase().startsWith(check)){
					cmd = check;
					message.replaceFirst("(?i)" + check, "");
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
					event.getChannel().send().message(GlobalVars.botname + " is running version " + GlobalVars.versionMarker);
					break;
				case "help":
					event.getUser().send().message("Karren-sama bot commands. (All commands are proceded by a . (Ex. .help))");
					event.getUser().send().message(".help command - Prints out this message.");
					event.getUser().send().message(".isgay command - Sends a message to the server calling whatever follows .isgay gay(Ex. .isgay Seth)");
					event.getUser().send().message(".kill command - Only operators on the channel can use this. Kills the bot.");
					event.getUser().send().message(".news command - user must atleast have voice (+v) in the channel. Posts a news update to the CRaZyPANTS website.");
					event.getUser().send().message(".np command - displays the currently playing song. (on/off change auto update mode)");
					event.getUser().send().message(".origin command - Replies to you stating that Origin is bad.");
					event.getUser().send().message("Random, command - This command allows you to get the bot to randomize a list of things. (Ex. Random, 1,2,3)");
					event.getUser().send().message(".echo command - Replies to you with an echo of whatever follows the command. (Ex. .echo Stuff)");
					event.getUser().send().message(".topic command - Sets the MOTD section of the topic with whatever follows the command.");
					event.getUser().send().message(".version command - replies with current version of the bot.");
					event.getUser().send().message("Hello(or alternative), goodbye(or alternative), clayton commands - replies with responses accordingly");
					event.getUser().send().message(".brad command - poop");
					break;
				case "debug":
					for(String nick : GlobalVars.userList)
						event.getUser().send().message("DEBUG USERLIST: " + nick);
					break;
			}
		}
	}

}
