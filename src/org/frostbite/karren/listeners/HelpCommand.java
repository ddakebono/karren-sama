package org.frostbite.karren.listeners;

import org.frostbite.karren.Interactions;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IRole;

public class HelpCommand implements IListener<MessageReceivedEvent>{
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        String[] msgSplit = event.getMessage().getContent().split(" ");
        if(msgSplit[0].equalsIgnoreCase(Karren.conf.getCommandPrefix() + "help")){
            if(msgSplit.length>1) {
                switch (msgSplit[1]) {
                    case "interactions":
                        for (Interactions help : Karren.bot.getInteractions()) {
                            try {
                                bot.getOrCreatePMChannel(event.getMessage().getAuthor()).sendMessage("Interaction " + help.getIdentifier() + ", activators: " + help.getActivatorsToString() + ", tags: " + help.getTagsToString() + ", printout template: " + help.getResponseTemplate());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
            IPrivateChannel user = bot.getOrCreatePMChannel(event.getMessage().getAuthor());
            user.sendMessage(event.getClient().getOurUser().getName() + " bot commands. (All commands are proceded by a " + Karren.bot.getBotConf().getCommandPrefix() + " (Ex. " + Karren.bot.getBotConf().getCommandPrefix() + "help))");
            user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "help command - Prints out this message. Use " + Karren.bot.getBotConf().getCommandPrefix() + "help interactions to list all interactions.");
            user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "isgay command - Sends a message to the server calling whatever follows .isgay gay(Ex. .isgay Seth)");
            user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "echo command - Replies to you with an echo of whatever follows the command. (Ex. .echo Stuff)");
            if (isAdmin(event, bot)) {
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "topic command - Sets the MOTD section of the topic with whatever follows the command.");
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "kill command - Kills the bot.");
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "npswitch command - Enables or disables the automatic now playing announcements.");
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "reloadint command - Triggers a refresh of the interactions system, reloading all interactions from the Interactions.txt");
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "reloadserv command - Triggers a refresh of the service controller system, reloads all monitored services from Services.txt");
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "services [start, stop, restart] [Service name] command - Starts, stops, or restarts target service. (EX. " + Karren.bot.getBotConf().getCommandPrefix() + "services start SpaceEng)");
                user.sendMessage(Karren.bot.getBotConf().getCommandPrefix() + "recover-nick command - Attempts to change to the nick that the bot should have.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    boolean isAdmin(MessageReceivedEvent event, IDiscordClient bot){
        boolean result = false;
        for(IRole role : event.getMessage().getAuthor().getRolesForGuild(Karren.bot.getBotConf().getChannel())){
            if(role.getName().equals("Admins")){
                result = true;
            }
        }
        return result;
    }
}
