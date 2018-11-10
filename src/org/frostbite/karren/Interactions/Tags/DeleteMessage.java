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

import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

public class DeleteMessage extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        result.getEvent().getMessage().delete().block();
        return msg;
    }

    @Override
    public String getTagName() {
        return "deletemsg";
    }

    @Override
    public PermissionSet getRequiredPermissions() {
        return PermissionSet.of(Permission.SEND_MESSAGES, Permission.MANAGE_MESSAGES);
    }
}
