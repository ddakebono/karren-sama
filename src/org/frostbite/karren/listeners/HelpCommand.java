package org.frostbite.karren.listeners;

import org.frostbite.karren.Interactions;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;

public class HelpCommand implements IListener<MessageReceivedEvent>{
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        if(event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix() + "help")){
            MessageBuilder helpMsg = new MessageBuilder(bot);
            try {
                helpMsg.withChannel(event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()));
                helpMsg.withContent("```\n");
                for (Interactions help : Karren.bot.getInteractions()) {
                    if(hasRole(event.getMessage().getAuthor(), event.getClient(), help.getPermissionLevel()))
                        helpMsg.appendContent( help.getIdentifier() + " : " + help.getHelptext() + "\n");
                }
                helpMsg.appendContent("```").send();
            } catch (DiscordException | HTTP429Exception | MissingPermissionsException e) {
                e.printStackTrace();
            }
        }
    }

    boolean hasRole(IUser user, IDiscordClient bot, String roleName){
        boolean result = false;
        if(roleName!=null) {
            for (IRole role : user.getRolesForGuild(bot.getGuildByID(Karren.conf.getGuildId()))) {
                if (role.getName().equals(roleName)) {
                    result = true;
                }
            }
            return result;
        }
        return true;
    }
}
