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

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.sql.SQLException;

public class Fave implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(Karren.bot.getListenCast().getSong().getSongID()!=0) {
            try {
                Karren.bot.getSql().addFave(event.getMessage().getAuthor().getID(), Karren.bot.getListenCast().getSong());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            msg = interaction.getRandomTemplatesFail();
        }
        return msg;
    }
}