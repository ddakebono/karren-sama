/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Database.Objects.DbWordcount;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.Timestamp;

public class Count5 extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageEvent event) {
        DbWordcount count = Karren.bot.getSql().getWordCount(interaction.getIdentifier());
        if(count!=null) {
            count.incrementCount();
            count.update();
            if((count.getCount()%5)==0) {
                Timestamp time = count.getCountStarted();
                return interaction.replaceMsg(msg,"%count", String.valueOf(count.getCount())).replace("%since", time.toString());
            } else {
                return null;
            }
        } else {
            return msg;
        }
    }

    @Override
    public String getTagName() {
        return "count5";
    }

}
