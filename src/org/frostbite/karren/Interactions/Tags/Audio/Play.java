/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Audio;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.AudioPlayer.AudioResultHandler;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.Arrays;
import java.util.Objects;


public class Play extends Tag {

    public final String[] usableExtensions = {"mp3", "ogg", "webm", "flac", "wav", "mp4", "m4a", "aac", "m3u", "pls"};

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        GuildMusicManager gm = Karren.bot.getGuildMusicManager(result.getEvent().getGuild());
        String voiceFile = interaction.getRandomVoiceFile();
        gm.scheduler.setAnnounceChannel((TextChannel) result.getEvent().getChannel());
        AudioResultHandler arh = new AudioResultHandler(result.getEvent(), interaction, gm, msg);
        if (!gm.scheduler.isPlaying())
            gm.player.setVolume(Math.round(interaction.getVoiceVolume()));
        if (interaction.hasParameter() || voiceFile != null) {
            if (voiceFile != null) {
                Karren.bot.getPm().loadItemOrdered(gm, voiceFile, arh);
            } else {
                String[] params = interaction.getParameter().split(",");
                if (params.length >= 2) {
                    try {
                        arh.setStartAt(Long.parseLong(params[1].trim()) * 1000);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (params.length == 3) {
                    try {
                        int volume = Integer.parseInt(params[2].trim());
                        if (volume > Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getMaxVolume())
                            volume = Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getMaxVolume();
                        gm.player.setVolume(volume);
                    } catch (NumberFormatException ignored) {
                    }
                }
                Karren.bot.getPm().loadItemOrdered(gm, params[0].trim(), arh);
            }
        } else if (!result.getEvent().getMessage().getAttachments().isEmpty()) {
            for (Message.Attachment media : result.getEvent().getMessage().getAttachments()) {
                if (Arrays.stream(usableExtensions).anyMatch(x -> Objects.equals(x, FilenameUtils.getExtension(media.getFileName())))) {
                    Karren.bot.getPm().loadItemOrdered(gm, media.getUrl(), arh);
                }
            }
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }

        if (arh.isFailed())
            msg = arh.getMsg();

        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jplay";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
    }

    @Override
    public Boolean getVoiceUsed() {
        return true;
    }

}