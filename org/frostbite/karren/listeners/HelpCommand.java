package org.frostbite.karren.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class HelpCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		PircBotX bot = event.getBot();
		String message = event.getMessage();
		String cmd = ".help";
		if(message.startsWith(cmd)){
			bot.sendMessage(event.getUser(), "Karren-sama bot commands. (All commands are proceded by a . (Ex. .help))");
			bot.sendMessage(event.getUser(), ".help command - Prints out this message.");
			bot.sendMessage(event.getUser(), ".isgay command - Sends a message to the server calling whatever follows .isgay gay(Ex. .isgay Seth)");
			bot.sendMessage(event.getUser(), ".kill command - Only operators on the channel can use this. Kills the bot.");
			bot.sendMessage(event.getUser(), ".news command - user must atleast have voice (+v) in the channel. Posts a news update to the CRaZyPANTS website.");
			bot.sendMessage(event.getUser(), ".np command - displays the currently playing song. (on/off change auto update mode)");
			bot.sendMessage(event.getUser(), ".origin command - Replies to you stating that Origin is bad.");
			bot.sendMessage(event.getUser(), "Random, command - This command allows you to get the bot to randomize a list of things. (Ex. Random, 1,2,3)");
			bot.sendMessage(event.getUser(), ".echo command - Replies to you with an echo of whatever follows the command. (Ex. .echo Stuff)");
			bot.sendMessage(event.getUser(), ".topic command - Sets the MOTD section of the topic with whatever follows the command.");
			bot.sendMessage(event.getUser(), ".version command - replies with current version of the bot.");
			bot.sendMessage(event.getUser(), "Hello(or alternative), goodbye(or alternative), clayton commands - replies with responses accordingly");
			bot.sendMessage(event.getUser(), ".brad command - poop");
		}
	}

}
