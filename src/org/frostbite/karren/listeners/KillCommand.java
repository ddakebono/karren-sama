package org.frostbite.karren.listeners;

import org.frostbite.karren.Logging;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class KillCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String cmd = ".kill";
		String message = event.getMessage();
		PircBotX bot = event.getBot();
		String ops = event.getChannel().getOps().toString();
		boolean isAnOp = ops.contains(event.getUser().getNick());
		if(isAnOp && message.startsWith(cmd)){
				Logging.log("Bot has been killed by " + event.getUser().getNick(), true);
				message = message.replaceFirst(cmd, "").trim();
				bot.sendIRC().quitServer("Kill command fired, bot terminating.");
		} else {
			if(!isAnOp && message.startsWith(cmd))
				event.respond("I will not be killed by you!");
		}
	}

}
