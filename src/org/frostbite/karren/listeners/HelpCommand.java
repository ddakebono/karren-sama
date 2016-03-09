package org.frostbite.karren.listeners;

import org.frostbite.karren.Interactions;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;

public class HelpCommand implements IListener<MessageReceivedEvent>{
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        String[] msgSplit = event.getMessage().getContent().split(" ");
        if(msgSplit[0].equalsIgnoreCase(Karren.conf.getCommandPrefix() + "help")){
            if(msgSplit.length>1) {
                switch (msgSplit[1]) {
                    case "interactions":
                        MessageBuilder helpMsg = new MessageBuilder(bot);
                        try {
                            helpMsg.withChannel(event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()));
                            helpMsg.withContent("```\n");
                            for (Interactions help : Karren.bot.getInteractions()) {
                                helpMsg.appendContent( help.getIdentifier() + " : " + help.getHelptext() + "\n");
                            }
                            helpMsg.appendContent("```").send();
                        } catch (DiscordException | HTTP429Exception | MissingPermissionsException e) {
                            e.printStackTrace();
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
    public void printBasic(MessageReceivedEvent event, IDiscordClient bot){
        try {
            MessageBuilder helpMsg = new MessageBuilder(bot);
            helpMsg.withChannel(bot.getOrCreatePMChannel(event.getMessage().getAuthor()));
            helpMsg.withContent("```\n");
            helpMsg.appendContent(event.getClient().getOurUser().getName() + " bot commands. (All commands are proceded by a " + Karren.conf.getCommandPrefix() + " (Ex. " + Karren.conf.getCommandPrefix() + "help))\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "help command - Prints out this message. Use " + Karren.conf.getCommandPrefix() + "help interactions to list all interactions.\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "isgay command - Sends a message to the server calling whatever follows .isgay gay(Ex. .isgay Seth)\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "echo command - Replies to you with an echo of whatever follows the command. (Ex. .echo Stuff)\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "topic command - Sets the MOTD section of the topic with whatever follows the command.\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "kill command - Kills the bot.\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "npswitch command - Enables or disables the automatic now playing announcements.\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "reloadint command - Triggers a refresh of the interactions system, reloading all interactions from the Interactions.txt\n");
            helpMsg.appendContent(Karren.conf.getCommandPrefix() + "reloadserv command - Triggers a refresh of the service controller system, reloads all monitored services from Services.txt\n");
            helpMsg.appendContent("```").send();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
