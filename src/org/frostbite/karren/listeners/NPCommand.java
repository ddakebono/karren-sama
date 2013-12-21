package org.frostbite.karren.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.Logging;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class NPCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".np";
		User user = event.getUser();
		PircBotX bot = event.getBot();
		String ops = event.getChannel().getOps().toString();
		String voices = event.getChannel().getVoices().toString();
		boolean hasVoice = voices.contains(user.getNick());
		boolean isAnOp = ops.contains(user.getNick());
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			if(message.startsWith("on")||message.startsWith("off")){
				if(message.startsWith("on") && (hasVoice || isAnOp)){
					GlobalVars.loop = true;
					GlobalVars.npChannel = event.getChannel();
					event.getChannel().send().message("Automagic now playing is now active, periodic updates will occur.");
					Logging.log("Now playing setting changed by " + user.getNick(), false);
				} else {
					if(hasVoice || isAnOp){
						//Stopping now playing functions of ListenCast
						GlobalVars.loop = false;
						event.getChannel().send().message("Now playing is now off, use .np to get current song.");
					} else {
						event.respond("Sorry but you do not have permission to do this");
					}
				}
			} else {
				event.getChannel().send().message("Now playing: \"" + GlobalVars.npSong + "\" On CRaZyRADIO ("+ GlobalVars.iceStreamTitle +"). Listeners: " + GlobalVars.iceListeners + "/" + GlobalVars.iceMaxListeners);
			}
		} else {
			if(message.startsWith(".dj")){
				event.getChannel().send().message("Current DJ is " + GlobalVars.iceDJ);
			}
		}
	}
}
