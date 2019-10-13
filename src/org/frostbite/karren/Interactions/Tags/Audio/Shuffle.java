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

import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class Shuffle extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().getGuild().getAudioManager().isConnected()){
            GuildMusicManager gm = Karren.bot.getGuildMusicManager(result.getEvent().getGuild());
            if(gm.scheduler.isShuffle()) {
                gm.scheduler.setShuffle(false);
                msg = interaction.replaceMsg(msg,"%bool", "false");
            } else {
                gm.scheduler.setShuffle(true);
                msg = interaction.replaceMsg(msg,"%bool", "true");
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jshuffle";
    }
}
