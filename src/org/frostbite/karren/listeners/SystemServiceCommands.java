package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.OsSpecific.SystemService;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class SystemServiceCommands extends ListenerAdapter<PircBotX> {
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        String cmd = bot.getBotConf().getCommandPrefix() + "services";
        String msg = event.getMessage();
        SystemService service;
        boolean returnState;
        boolean listServices = false;
        if(bot.getOsType() != 0 && bot.isEnableServiceControl()){
            if(event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())){
                if(msg.toLowerCase().startsWith(cmd)){
                    msg = msg.substring(cmd.length(), msg.length()).trim();
                    bot.getLog().debug("Services command triggered.");
                    if(msg.toLowerCase().startsWith("stop")){
                        msg = msg.substring(4, msg.length()).trim();
                        bot.getLog().debug("Trying to stop " + msg);
                        service = getServiceObject(msg, bot);
                        if(service != null) {
                            try {
                                event.respond("Stopping service.");
                                if(bot.getOsType() == 1){
                                    returnState = service.winStop();
                                } else {
                                    returnState = service.linStop();
                                }
                                if (returnState)
                                    event.respond("Service " + msg + " has been stopped!");
                                else
                                    event.respond("Service " + msg + "could not be stopped!");

                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            event.respond("Service could not be stopped. (" + msg + " not a monitored service!)");
                            listServices = true;
                        }
                    } else if(msg.toLowerCase().startsWith("start")){
                        msg = msg.substring(5, msg.length()).trim();
                        bot.getLog().debug("Trying to start " + msg);
                        service = getServiceObject(msg, bot);
                        if(service != null) {
                            try {
                                event.respond("Started service.");
                                if(bot.getOsType() == 1){
                                    returnState = service.winStart();
                                } else {
                                    returnState = service.linStart();
                                }
                                if (returnState)
                                    event.respond("Service " + msg + " has been started!");
                                else
                                    event.respond("Service " + msg + "could not be started!");
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            event.respond("Service could not be started. (" + msg + " not a monitored service!)");
                            listServices = true;
                        }
                    } else if(msg.toLowerCase().startsWith("restart")){
                        msg = msg.substring(7, msg.length()).trim();
                        bot.getLog().debug("Trying to restart " + msg);
                        service = getServiceObject(msg, bot);
                        if(service != null) {
                            try {
                                event.respond("Restarting service.");
                                if(bot.getOsType() == 1){
                                    returnState = service.winRestart();
                                } else {
                                    returnState = service.linRestart();
                                }
                                if (returnState)
                                    event.respond("Service " + msg + " has been restarted!");
                                else
                                    event.respond("Service " + msg + "could not be restarted!");
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            event.respond("Service could not be restarted. (" + msg + " not a monitored service!)");
                            listServices = true;
                        }
                    } else {
                        event.respond("The entered command is not one of the usable commands for the " + bot.getBotConf().getCommandPrefix() + "services command.");
                    }
                    if(listServices){
                        event.getUser().send().message("List of monitored services:");
                        for(SystemService print : bot.getServices()){
                            event.getUser().send().message("Service name: " + print.getIdent() + ", Windows Services Name: " + print.getName());
                        }
                    }
                }
            }
        }
    }
    public SystemService getServiceObject(String ident, KarrenBot bot){
        SystemService result = null;
        for(SystemService check : bot.getServices()){
            if(check.getIdent().equalsIgnoreCase(ident))
                result = check;
        }
        return result;
    }
}
