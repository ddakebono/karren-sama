package org.frostbite.karren.listeners;

import org.frostbite.karren.Logging;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class NewsCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		String message = event.getMessage();
		String[] data = new String[2];
		String cmd = ".news";
		User user = event.getUser();
		String ops = event.getChannel().getOps().toString();
		boolean hasVoice = ops.contains(user.getNick());
		boolean isAnOp = ops.contains(user.getNick());
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			if(hasVoice || isAnOp){
				if(message != null){
					data[0] = user.getNick();
					data[1] = message;
					MySQLConnector.sqlPush("news", "add", data);
					Logging.log("A news update was posted by " + user.getNick(), false);
				} else {
					event.respond("You seem to be missing the news post, please supply a post after the command. (ex. .news This is my post!)");
				}
			} else {
				event.respond("You do not have the required permission to do this.");
			}
			
		}
	}
}
