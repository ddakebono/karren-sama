package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.OsSpecific.WindowsService;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class WindowsServiceCommands extends ListenerAdapter<PircBotX> {
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        String cmd = bot.getBotConf().getCommandPrefix() + "services";
        String msg = event.getMessage();
        WindowsService service;
        boolean returnState;
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
                                event.respond("Stopping service.");
                                returnState = service.stop();
                                if(returnState)
                                    event.respond("Service " + msg + " has been stopped!");
                                else
                                    event.respond("Service " + msg + "could not be stopped!");

                            } catch (IOException | InterruptedException e) {
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
                                event.respond("Started service.");
                                returnState = service.start();
                                if(returnState)
                                    event.respond("Service " + msg + " has been started!");
                                else
                                    event.respond("Service " + msg + "could not be started!");
                            } catch (IOException | InterruptedException e) {
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
                                event.respond("Restarting service.");
                                returnState = service.restart();
                                if(returnState)
                                    event.respond("Service " + msg + " has been restarted!");
                                else
                                    event.respond("Service " + msg + "could not be restarted!");
                            } catch (InterruptedException | IOException e) {
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
