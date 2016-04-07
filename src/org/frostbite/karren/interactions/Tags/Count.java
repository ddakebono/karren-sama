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
import java.sql.Timestamp;
import java.util.ArrayList;

public class Count implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        ArrayList<Object> returned = null;
        try {
            returned = Karren.bot.getSql().modifyWordCount(interaction.getIdentifier().toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(returned!=null) {
            Timestamp time = (Timestamp) returned.get(3);
            return msg.replace("%count", String.valueOf((long) returned.get(2))).replace("%since", time.toString());
        } else {
            return msg;
        }
    }
}
