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

import org.frostbite.karren.Database.Models.tables.records.WordcountsRecord;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;

public class Count5 implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        WordcountsRecord count = Karren.bot.getSql().getWordCount(interaction.getIdentifier());
        if(count!=null) {
            count.setCount(count.getCount()+1);
            count.update();
            if((count.getCount()%5)==0) {
                Timestamp time = count.getCountstarted();
                return msg.replace("%count", String.valueOf(count.getCount())).replace("%since", time.toString());
            } else {
                return null;
            }
        } else {
            return msg;
        }
    }
}
