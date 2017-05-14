/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.D4JPlayer;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.json.JSONArray;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class D4JSelect implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String param = interaction.getParameter();
        int selection = 0;
        try {
            selection = Integer.parseInt(param);
        } catch (NumberFormatException ignored){}
        if(selection>0 && selection<=3) {
            D4JSearch search = (D4JSearch) Karren.bot.getGuildManager().getHandlers().get("d4jsearch");
            JSONArray resultArray = search.getResultArray(event.getAuthor().getStringID());
            interaction.setParameter(resultArray.getJSONObject(selection - 1).getJSONObject("id").getString("videoId"));
            msg = msg.replace("%title", resultArray.getJSONObject(selection - 1).getJSONObject("snippet").getString("title"));
        } else if(param.trim().equalsIgnoreCase("c")){
            msg = "Alright, I've deleted the results.";
            D4JSearch search = (D4JSearch) Karren.bot.getGuildManager().getHandlers().get("d4jsearch");
            search.getResultArray(event.getAuthor().getStringID());
            interaction.stopProcessing();
        } else {
            msg = interaction.getRandomTemplatesFail();
            interaction.addUsageCount();
        }
        return msg;
    }
}
