package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.OsSpecific.WindowsService;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

/**
 * Created by frostbite on 09/06/14.
 */
public class WindowsServiceCommands extends ListenerAdapter<PircBotX> {
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        String cmd = bot.getBotConf().getCommandPrefix() + "services";
        String msg = event.getMessage();
        WindowsService service;
        if(bot.isWindows()){
            if(event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())){
                if(msg.toLowerCase().startsWith(cmd)){
                    msg = msg.substring(cmd.length(), msg.length()).trim();
                    bot.getLog().debug("Services command triggered.");
                    if(msg.toLowerCase().startsWith("stop")){
                        msg = msg.substring(4, msg.length()).trim();
                        bot.getLog().debug("Trying to stop " + msg);
                        service = getServiceObject(msg, bot);
                        if(service != null)
                            try {
                                service.stop();
                                event.respond("Stopping service.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        else
                            event.respond("Service could not be stopped. (" + msg + " not a monitored service!)");
                    }
                    if(msg.toLowerCase().startsWith("start")){
                        msg = msg.substring(5, msg.length()).trim();
                        bot.getLog().debug("Trying to start " + msg);
                        service = getServiceObject(msg, bot);
                        if(service != null)
                            try {
                                service.start();
                                event.respond("Started service.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        else
                            event.respond("Service could not be started. (" + msg + " not a monitored service!)");
                    }
                    if(msg.toLowerCase().startsWith("restart")){
                        msg = msg.substring(7, msg.length()).trim();
                        bot.getLog().debug("Trying to restart " + msg);
                        service = getServiceObject(msg, bot);
                        if(service != null)
                            try {
                                service.restart();
                                event.respond("Restarting service.");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        else
                            event.respond("Service could not be restarted. (" + msg + " not a monitored service!)");
                    }
                }
            }
        }
    }
    public WindowsService getServiceObject(String ident, KarrenBot bot){
        WindowsService result = null;
        for(WindowsService check : bot.getServices()){
            if(check.getIdent().equalsIgnoreCase(ident))
                result = check;
        }
        return result;
    }
}
