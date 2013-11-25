package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class VersionCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".version";
		if(message.startsWith(cmd)){
			event.respond(GlobalVars.botname + " is running version " + GlobalVars.versionMarker);
		}
	}

}
