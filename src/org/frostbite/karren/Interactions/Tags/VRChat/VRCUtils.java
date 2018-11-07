/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.VRChat;

import io.github.vrchatapi.VRCAnalytics;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

public class VRCUtils extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        int count = VRCAnalytics.getUserCount();
        msg = interaction.replaceMsg(msg, "%vrccount", Integer.toString(count));
        interaction.setEmbedFooter("Using VRChat API - Users Online: " + count);
        return msg;
    }

    @Override
    public String getTagName() {
        return "vrcanalytics";
    }

}
