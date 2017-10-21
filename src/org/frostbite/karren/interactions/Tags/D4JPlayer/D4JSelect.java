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

import com.google.api.services.youtube.model.Video;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.List;

public class D4JSelect extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String param = interaction.getParameter();
        int selection = 0;
        try {
            selection = Integer.parseInt(param);
        } catch (NumberFormatException ignored){}
        if(selection>0 && selection<=3) {
            D4JSearch search = (D4JSearch) interaction.getTagCache().get(0);
            List<Video> resultArray = search.getResultArray(event.getAuthor().getStringID());
            interaction.setParameter(resultArray.get(selection-1).getId());
            msg = interaction.replaceMsg(msg,"%title", resultArray.get(selection - 1).getSnippet().getTitle());
        } else if(param.trim().equalsIgnoreCase("c")){
            msg = "Alright, I've deleted the results.";
            interaction.stopProcessing();
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
            interaction.addUsageCount();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jselect";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
