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
		String localNp;
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
					bot.sendMessage(event.getChannel(), "Automagic now playing is now active, periodic updates will occur.");
					Logging.log("Now playing setting changed by " + user.getNick());
					while(GlobalVars.loop){
						GlobalVars.npSongNew = getNowPlaying();
						if(GlobalVars.npSongNew == "")
							GlobalVars.npSongNew = "Song name error!";
						int npSongInt1 = GlobalVars.npSong.hashCode();
						int npSongInt2 = GlobalVars.npSongNew.hashCode();
						if(npSongInt1 == npSongInt2){
							System.out.println("No song change detected!");
						} else {
							GlobalVars.npSong = GlobalVars.npSongNew;
							bot.sendMessage("#minecraft", "Now playing: \"" + GlobalVars.npSong + "\" On CRaZyRADIO.");
						}
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					if(hasVoice || isAnOp){
						//Killing loop
						GlobalVars.loop = false;
						bot.sendMessage(event.getChannel(), "Now playing is now off, use .np to get current song.");
					} else {
						event.respond("Sorry but you do not have permission to do this");
					}
				}
			} else {
				localNp = getNowPlaying();
				bot.sendMessage("#minecraft", "Now playing: \"" + localNp + "\" On CRaZyRADIO.");
			}
		}
	}
	public String getNowPlaying(){
		String nowPlaying = "offair";
		try {
			URL np = new URL(GlobalVars.icelink);
			HttpURLConnection npData = (HttpURLConnection) np.openConnection();
			BufferedReader getData = new BufferedReader(new InputStreamReader(npData.getInputStream()));
			nowPlaying = getData.readLine();
		} catch (MalformedURLException e) {
			System.out.println("Error, page could not be reached!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Connection not established...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nowPlaying;
	}
}
