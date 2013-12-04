package org.frostbite.karren.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class EchoCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".echo";
		if(message.startsWith(".echo")){
			message = message.replaceFirst(cmd, "").trim();
			event.respond(message);
		}
	}

}
