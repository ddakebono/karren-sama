/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Voice;

import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.AudioPlayer.AudioResultHandler;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.Arrays;
import java.util.Objects;


public class D4JPlay extends Tag {

    public final String[] usableExtensions = {"mp3", "ogg", "webm", "flac", "wav", "mp4", "m4a", "aac", "m3u", "pls"};

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        GuildMusicManager gm = Karren.bot.getGuildMusicManager(guild);
        if(gm.getAudioProvider() != null) {
            String voiceFile = interaction.getRandomVoiceFile();
            if(Karren.bot.sql.getGuild(guild).getOverrideChannel()>0)
                gm.scheduler.setAnnounceChannel((MessageChannel) Karren.bot.client.getChannelById(Snowflake.of(Karren.bot.sql.getGuild(guild).getOverrideChannel())).block());
            else
                gm.scheduler.setAnnounceChannel(result.getEvent().getMessage().getChannel().block());
            AudioResultHandler arh = new AudioResultHandler(result.getEvent(), interaction, gm, msg);
            if (!gm.scheduler.isPlaying())
                gm.player.setVolume(Math.round(interaction.getVoiceVolume()));
            if (interaction.hasParameter() || voiceFile != null) {
                if (voiceFile != null) {
                    Karren.bot.pm.loadItemOrdered(gm, voiceFile, arh);
                } else {
                    String[] params = interaction.getParameter().split(",");
                    if(params.length>=2) {
                        try {
                            arh.setStartAt(Long.parseLong(params[1].trim()) * 1000);
                        } catch (NumberFormatException ignored){ }
                    }
                    if(params.length==3){
                        try{
                            int volume = Integer.parseInt(params[2].trim());
                            if(volume>40)
                                volume=40;
                            gm.player.setVolume(volume);
                        } catch (NumberFormatException ignored){}
                    }
                    Karren.bot.pm.loadItemOrdered(gm, params[0].trim(), arh);
                }
            } else if (result.getEvent().getMessage().getAttachments().size()>0) {
                for (Attachment media : result.getEvent().getMessage().getAttachments()) {
                    if (Arrays.stream(usableExtensions).anyMatch(x -> Objects.equals(x, FilenameUtils.getExtension(media.getFilename())))) {
                        Karren.bot.pm.loadItemOrdered(gm, media.getUrl(), arh);
                    }
                }
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
            if (arh.isFailed())
                msg = arh.getMsg();
        } else {
            msg = "Provider disabled";
        }

        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jplay";
    }

    @Override
    public PermissionSet getRequiredPermissions() {
        return PermissionSet.of(Permission.SEND_MESSAGES, Permission.CONNECT, Permission.SPEAK);
    }

    @Override
    public Boolean getVoiceUsed() {
        return true;
    }

}