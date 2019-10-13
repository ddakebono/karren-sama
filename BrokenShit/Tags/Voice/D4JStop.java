/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Voice;

import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class D4JStop extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        GuildMusicManager gms = Karren.bot.getGuildMusicManager(result.getEvent().getGuild().block());
        if(gms.scheduler.isPlaying())
            gms.scheduler.stopQueue();
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jstop";
    }

    @Override
    public PermissionSet getRequiredPermissions() {
        return PermissionSet.of(Permission.SEND_MESSAGES);
    }

}
