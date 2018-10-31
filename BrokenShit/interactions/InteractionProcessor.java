/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.data.stored.MessageBean;
import discord4j.core.object.entity.Guild;
import org.frostbite.karren.Karren;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.PermissionUtils;

import java.util.ArrayList;

public class InteractionProcessor {

    private ArrayList<Interaction> interactions;
    private ArrayList<Interaction> defaultInteractions = null;
    private Guild guild; //CAN BE NULL

    public InteractionProcessor(Guild guild){
        this(guild, null);
    }

    public InteractionProcessor(Guild guild, ArrayList<Interaction> defaultInteractions){
        this.guild = guild;
        this.defaultInteractions = defaultInteractions;
        loadAndUpdateDatabase();
    }

    public void loadAndUpdateDatabase(){
        interactions = new ArrayList<>();
        interactions.addAll(defaultInteractions);
        if(guild!=null) {
            /*if(Karren.bot.getGuildMusicManager(guild) == null || !(guild.getAudioManager().getAudioProvider() instanceof AudioProvider)){
                Karren.log.info("Looks like the GuildMusicManager failed to start, let's try again.");
                try {
                    GuildMusicManager gm = Karren.bot.createGuildMusicManager(guild);
                    guild.getAudioManager().setAudioProvider(gm.getAudioProvider());
                    Karren.log.info("GuildMusicManager initialized on second try for guild " + guild.getName());
                } catch (NullPointerException e){
                    Karren.log.error("Uh oh, looks like we couldn't start the music manager on the second try! Guild " + guild.getName() + " doesn't have a music manager!");
                }
            }*/
            Karren.log.info("Interaction Processor for " + guild.getName() + " ready!");

        } else {
            Karren.log.info("Default interaction processor initialized!");
        }
    }

    public MessageBean handle(MessageCreateEvent event) {
        String returned;
        MessageBean result = null;
        for(Interaction check : interactions){
            returned = check.handleMessage(event);
            if(returned!=null){
                check.setLock(true);
                Karren.log.info("Interaction match for " + check.getIdentifier() + ", handling templates! (Confidence: " + check.getConfidenceChecked() + ")");
                if(event.getGuild()!=null && Karren.bot.getSql().getGuild(event.getGuild()).getOverrideChannel()!=0)
                    result = new MessageBuilder(Karren.bot.getClient()).withChannel(event.getGuild().getChannelByID(Karren.bot.getSql().getGuild(event.getGuild()).getOverrideChannel()));
                else
                    result = new MessageBuilder(Karren.bot.getClient()).withChannel(event.getMessage().getChannel());
                if(!check.isPermBad()) {
                    for (String tag : check.getTags()) {
                        if (!tag.equalsIgnoreCase("pm") && !check.isStopProcessing()) {
                            Tag handler = Karren.bot.getGuildManager().getTag(tag.toLowerCase());
                            if (handler != null && returned != null) {
                                if (!handler.getVoiceUsed()) {
                                    if (PermissionUtils.hasPermissions(event.getChannel(), event.getClient().getOurUser(), handler.getRequiredPermissions()) || (check.isSpecialInteraction() && Karren.bot.sql.getGuild(guild).getOverrideChannel()!=0)) {
                                        returned = handler.handleTemplate(returned, check, result, event);
                                    } else {
                                        returned = "Uh oh, looks like I'm missing some text channel permissions! " + handler.getRequiredPermissions().toString() + ". Ask your admin to fix this.";
                                    }
                                } else {
                                    if (PermissionUtils.hasPermissions(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel(), event.getClient().getOurUser(), handler.getRequiredPermissions())) {
                                        returned = handler.handleTemplate(returned, check, result, event);
                                    } else {
                                        returned = "Uh oh, looks like I'm missing some voice channel permissions! " + handler.getRequiredPermissions().toString() + ". Ask your admin to fix this.";
                                    }
                                }
                            } else if (!tag.equalsIgnoreCase("bot") && !tag.equalsIgnoreCase("prefixed") && !tag.equalsIgnoreCase("special") && !tag.equalsIgnoreCase("feelinglucky") && !tag.equalsIgnoreCase("nodisplay") && returned != null) {
                                Karren.log.error("Please check interaction " + check.getIdentifier() + " as the file contains invalid tags!");
                            }
                        }
                    }
                    if(check.interactionUsed())
                        Karren.bot.getGuildManager().getInteractionProcessor(event.getGuild()).getInteractions().remove(check);
                }
                if(check.isEmbedUsed())
                    result.withEmbed(check.getEmbed().build());
                check.setLock(false);
                if(returned!=null && returned.length()>0){
                    returned = returned.replaceAll("%prefix", Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()));
                    result.withContent(returned);
                } else {
                    if(!check.isEmbedUsed())
                        result = null;
                }

                if(!check.isSpecialInteraction())
                    break;

            }
        }
        return result;
    }//https://discordapp.com/oauth2/authorize?&client_id=397506837861105696&scope=bot&permissions=506690640

    public ArrayList<Interaction> getInteractions() {
        return interactions;
    }
}
