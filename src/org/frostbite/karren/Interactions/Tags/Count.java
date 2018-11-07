/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Database.Objects.DbWordcount;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.sql.Timestamp;

public class Count extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        DbWordcount count = Karren.bot.getSql().getWordCount(interaction.getIdentifier());
        if(count!=null) {
            count.incrementCount();
            count.update();
            if(interaction.hasParameter() && (count.getCount()%Integer.parseInt(interaction.getParameter()))!=0) {
                result.setDoNotSend(true);
                return null;
            }
            Timestamp time = count.getCountStarted();
            return interaction.replaceMsg(msg,"%count", String.valueOf(count.getCount())).replace("%since", time.toString());
        } else {
            return msg;
        }
    }

    @Override
    public String getTagName() {
        return "count";
    }

}
