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

import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class Topic implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.getParameter()!=null) {
            msg = msg.replace("%motd", interaction.getParameter());
            try {
                event.getMessage().getChannel().changeTopic(msg);
            } catch (HTTP429Exception | DiscordException e) {
                e.printStackTrace();
            } catch (MissingPermissionsException e) {
                return "You'll need to give me more permissions so I can change the topic!";
            }
        } else {
            return interaction.getRandomTemplatesFail();
        }
        return msg;
    }
}
