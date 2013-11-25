package org.frostbite.karren.listeners;

import org.frostbite.karren.Logging;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TopicCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".topic";
		PircBotX bot = event.getBot();
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			String voices = event.getChannel().getVoices().toString();
			String ops = event.getChannel().getOps().toString();
			boolean hasVoice = voices.contains(event.getUser().getNick());
			boolean isAnOp = ops.contains(event.getUser().getNick());
			if(hasVoice || isAnOp){
				if(message != ""){
					bot.setTopic(event.getChannel(), "CRaZyPANTS Minecraft channel, use .help to get current commands avalable for use. MOTD: " + message);
					Logging.log("The MOTD has been changed to " + message + " By " + event.getUser().getNick());
				} else {
					event.respond("Your message does not contain a new MOTD");
				}
			} else {
				event.respond("You do not have permission to change the topic!");
			}
		}
	}

}
