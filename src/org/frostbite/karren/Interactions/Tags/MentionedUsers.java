/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import net.dv8tion.jda.api.entities.User;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

import java.util.List;

public class MentionedUsers extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        List<User> users = result.getEvent().getMessage().getMentionedUsers();
        if(users.size()>0)
            interaction.getMentionedUsers().addAll(users);
        return msg;
    }

    @Override
    public String getTagName() {
        return "mentioned";
    }

}
