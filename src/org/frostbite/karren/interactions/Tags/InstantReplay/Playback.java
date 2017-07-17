/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.InstantReplay;

import org.frostbite.karren.InstantReplay.IRAudioProvider;
import org.frostbite.karren.InstantReplay.InstantReplay;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class Playback extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(Karren.bot.getIrm().isGuildIRActive(event.getGuild()) && !Karren.bot.getGuildMusicManager(event.getGuild()).scheduler.isSchedulerActive()) {
            InstantReplay ir = Karren.bot.getIrm().getInstantReplay(event.getGuild(), event.getAuthor());
            if (interaction.getMentionedUsers().size() > 0) {
                if(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().equals(ir.getChannel())) {
                    ir.getLockedUsers().add(interaction.getMentionedUsers().get(0).getStringID());
                    msg = msg.replace("%username", interaction.getMentionedUsers().get(0).getName());
                    event.getGuild().getAudioManager().setAudioProvider(new IRAudioProvider(ir, interaction.getMentionedUsers().get(0)));
                } else {
                    msg = "You must be in the voice channel that I'm listening in.";
                }
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            msg = "Hold on a sec, looks like I'm either not listening or I'm playing music already.";
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "irplay";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.VOICE_SPEAK, Permissions.SEND_MESSAGES);
    }

}
