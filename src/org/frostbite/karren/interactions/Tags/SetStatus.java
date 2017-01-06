/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.MessageBuilder;

public class SetStatus implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String param = interaction.getParameter();
        if(param!=null){
            msg = msg.replace("%param", param);
            event.getClient().changeStatus(Status.game(param));
        } else {
            msg = interaction.getRandomTemplatesFail();
            event.getClient().changeStatus(Status.empty());
        }
        return msg;
    }
}
