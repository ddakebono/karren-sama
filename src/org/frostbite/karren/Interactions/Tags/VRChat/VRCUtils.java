/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.VRChat;

import io.github.vrchatapi.ApiException;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class VRCUtils extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        int count = 0;
        if(Karren.bot.getSystemApi()!=null) {
            try {
                count = Karren.bot.getSystemApi().getCurrentOnlineUsers();
            } catch (ApiException e) {
                Karren.log.error("Exception during getOnlineUsers! " + e.getCode());
                Karren.log.error("Reason: " + e.getResponseBody());
                Karren.log.error("Response Headers: " + e.getResponseHeaders());
                e.printStackTrace();
            }
        }
        msg = interaction.replaceMsg(msg, "%vrccount", Integer.toString(count));
        interaction.setEmbedFooter("Using VRChat API - Users Online: " + count);
        return msg;
    }

    @Override
    public String getTagName() {
        return "vrcanalytics";
    }

}
