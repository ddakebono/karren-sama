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

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

public class Echo extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        String parameter = interaction.getParameter();
        if(parameter!=null) {
            return interaction.replaceMsg(msg,"%echo", parameter);
        }
        else
            return interaction.replaceMsg(msg,"%echo", "");
    }

    @Override
    public String getTagName() {
        return "echo";
    }

}
