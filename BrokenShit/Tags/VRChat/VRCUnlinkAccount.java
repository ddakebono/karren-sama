/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.VRChat;

import io.github.vrchatapi.VRCFriends;
import io.github.vrchatapi.VRCUser;
import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class VRCUnlinkAccount extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(event.getMessage().getMentions().size()>0){
            DbUser user = Karren.bot.sql.getUserData(event.getMessage().getMentions().get(0));
            if(user.getVrcUserID()!=null){
                VRCUser vrcUser = VRCUser.fetch(user.getVrcUserID());
                msg = interaction.replaceMsg(msg, "%vrcuser", vrcUser.getDisplayName());
                VRCFriends.unfriend(vrcUser);
                user.setVrcUserID(null);
                user.update();
            } else {
                msg = interaction.getRandomTemplate("nolink").getTemplate();
            }
        } else {
            msg = interaction.getRandomTemplate("noparam").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "VRCUnlinkAccount";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return super.getRequiredPermissions();
    }
}