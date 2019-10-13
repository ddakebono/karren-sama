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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

public class DeleteMessage extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        AuditableRestAction deleteAction = result.getEvent().getMessage().delete();
        deleteAction.queue();
        return msg;
    }

    @Override
    public String getTagName() {
        return "deletemsg";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE};
    }
}
