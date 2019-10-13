/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import net.dv8tion.jda.api.entities.Member;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class NoVoiceHijack extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().getMember()!=null) {
            Member selfMember = result.getEvent().getGuild().getMember(Karren.bot.client.getSelfUser());
            if (selfMember != null) {
                if (selfMember.getVoiceState() != null && result.getEvent().getMember().getVoiceState()!=null) {
                    if (selfMember.getVoiceState().getChannel() != null && result.getEvent().getMember().getVoiceState().getChannel()!=null) {
                        if (!selfMember.getVoiceState().getChannel().equals(result.getEvent().getMember().getVoiceState().getChannel())) {
                            msg = interaction.getRandomTemplate("nohijack").getTemplate();
                            interaction.stopProcessing();
                        }
                    }
                }
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "novoicehijack";
    }
}
