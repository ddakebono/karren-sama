package org.frostbite.karren.listeners;

import org.frostbite.karren.Interactions;
import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class HelpCommand extends ListenerAdapter<PircBotX>{
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        String[] msgSplit = event.getMessage().split(" ");
        if(msgSplit[0].equalsIgnoreCase(bot.getBotConf().getCommandPrefix() + "help")){
            if(msgSplit.length>1) {
                switch (msgSplit[1]) {
                    case "interactions":
                        for (Interactions help : bot.getInteractions()) {
                            event.getUser().send().message("Interaction " + help.getIdentifier() + ", activators: " + help.getActivatorsToString() + ", tags: " + help.getTagsToString() + ", printout template: " + help.getResponseTemplate());
                        }
                        break;
                    default:
                        printBasic(event, bot);
                }
            } else {
                printBasic(event, bot);
            }
        }
    }
    public void printBasic(MessageEvent event, KarrenBot bot){
        event.getUser().send().message(event.getBot().getNick() + " bot commands. (All commands are proceded by a " + bot.getBotConf().getCommandPrefix() + " (Ex. " + bot.getBotConf().getCommandPrefix() + "help))");
        event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "help command - Prints out this message. Use " + bot.getBotConf().getCommandPrefix() + "help interactions to list all interactions.");
        event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "isgay command - Sends a message to the server calling whatever follows .isgay gay(Ex. .isgay Seth)");
        event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "echo command - Replies to you with an echo of whatever follows the command. (Ex. .echo Stuff)");
        if (event.getChannel().isOp(event.getUser()) || event.getChannel().hasVoice(event.getUser())) {
            event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "topic command - Sets the MOTD section of the topic with whatever follows the command.");
            event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "kill command - Kills the bot.");
            event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "news command - Posts a news update to the CRaZyPANTS website.");
            event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "npswitch command - Enables or disables the automatic now playing announcements.");
            event.getUser().send().message(bot.getBotConf().getCommandPrefix() + "reloadint command - Triggers a refresh of the interactions system, reloading all interactions from the Interactions.txt");
        }
    }
}
