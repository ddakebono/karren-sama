/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Audio;

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class Repeat extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().getGuild().getAudioManager().isConnected()){
            boolean toggle = Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).scheduler.getQueue().toggleRepeat();
            if(toggle)
                msg = interaction.replaceMsg(msg, "%toggle", "true");
            else
                msg = interaction.replaceMsg(msg, "%toggle", "false");
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jrepeat";
    }
}
