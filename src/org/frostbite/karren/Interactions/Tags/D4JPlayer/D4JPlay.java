/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.D4JPlayer;

import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.AudioPlayer.AudioProvider;
import org.frostbite.karren.AudioPlayer.AudioResultHandler;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;


public class D4JPlay extends Tag{

    public final String[] usableExtensions = {"mp3", "ogg", "webm", "flac", "wav", "mp4", "m4a", "aac", "m3u", "pls"};

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(event.getGuild().getAudioManager().getAudioProvider() instanceof AudioProvider) {
            GuildMusicManager gm = Karren.bot.getGuildMusicManager(event.getGuild());
            String voiceFile = interaction.getRandomVoiceFile();
            gm.scheduler.setAnnounceChannel(event.getChannel());
            AudioResultHandler arh = new AudioResultHandler(event, interaction, gm, msg);
            if (!gm.scheduler.isPlaying())
                gm.player.setVolume(Math.round(interaction.getVoiceVolume()));
            if (interaction.hasParameter() || voiceFile != null) {
                if (voiceFile != null) {
                    Karren.bot.getPm().loadItemOrdered(gm, voiceFile, arh);
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
                            if(volume>Karren.bot.getSql().getGuild(event.getGuild()).getMaxVolume())
                                volume=Karren.bot.getSql().getGuild(event.getGuild()).getMaxVolume();
                            gm.player.setVolume(volume);
                        } catch (NumberFormatException ignored){}
                    }
                    Karren.bot.getPm().loadItemOrdered(gm, params[0].trim(), arh);
                }
            } else if (!event.getMessage().getAttachments().isEmpty()) {
                for (IMessage.Attachment media : event.getMessage().getAttachments()) {
                    if (Arrays.stream(usableExtensions).anyMatch(x -> Objects.equals(x, FilenameUtils.getExtension(media.getFilename())))) {
                        Karren.bot.getPm().loadItemOrdered(gm, media.getUrl(), arh);
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
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.VOICE_CONNECT, Permissions.VOICE_SPEAK);
    }

    @Override
    public Boolean getVoiceUsed() {
        return true;
    }

}