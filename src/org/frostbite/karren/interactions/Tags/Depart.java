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
import java.util.ArrayList;

public class Depart implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String[] data = {event.getMessage().getAuthor().getID()};
        ArrayList<Object> resultData = new ArrayList<>();
        try {
            resultData.addAll(Karren.bot.getSql().getUserData(event.getMessage().getAuthor().getID()));
            if(Boolean.parseBoolean(String.valueOf(resultData.get(2)))){
                 msg = interaction.getRandomTemplatesFail();
            }
            Karren.bot.getSql().userOperation("part", data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
