/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.VRChat;

import io.github.vrchatapi.VRCUser;
import io.github.vrchatapi.VRCWorld;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.KarrenUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class VRCUserSearch extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()){
            String search = interaction.getParameter();
            List<VRCUser> users = VRCUser.list(0, 15, false, search);
            if(users.size()>0) {
                //VRCUser user = VRCUser.fetch(users.get(0).getId());
                Optional<VRCUser> userOpt = users.stream().filter(userFind -> userFind.getDisplayName().toLowerCase().equals(search.toLowerCase())).findFirst();
                VRCUser user;
                if(userOpt.isPresent()) {
                    user = VRCUser.fetch(userOpt.get().getId());
                } else {
                    user = VRCUser.fetch(users.get(0).getId());
                }
                if (user.isFriend()) {
                    if (!user.getWorldID().equalsIgnoreCase("offline")) {
                        if (!user.getWorldID().equalsIgnoreCase("private")) {
                            VRCWorld world = VRCWorld.fetch(user.getWorldID());
                            msg = interaction.replaceMsg(msg, "%world", world.getName());
                            if (user.getInstanceID().length() < 10)
                                msg = interaction.replaceMsg(msg, "%link", "[Click here to join](https://www.vrchat.net/launch?worldId=" + world.getId() + "&instanceId=" + user.getInstanceID() + ")");
                            else
                                msg = interaction.replaceMsg(msg, "%link", "Not Public");
                        } else {
                            msg = interaction.replaceMsg(msg, "%world", "Private World");
                            msg = interaction.replaceMsg(msg, "%link", "Private World");
                        }
                    } else {
                        msg = interaction.replaceMsg(msg, "%world", "Offline");
                        msg = interaction.replaceMsg(msg, "%link", "Offline");
                    }
                } else {
                    msg = interaction.replaceMsg(msg, "%world", "Not a friend");
                    msg = interaction.replaceMsg(msg, "%link", "Not a friend");
                }
                msg = interaction.replaceMsg(msg, "%status", user.getStatus());
                if(user.getStatusDesc().length()>0)
                    msg = interaction.replaceMsg(msg, "%statdesc", user.getStatusDesc());
                else
                    msg = interaction.replaceMsg(msg, "%statdesc", "None");
                msg = interaction.replaceMsg(msg, "%username", user.getDisplayName());
                msg = interaction.replaceMsg(msg, "%userid", user.getId());
                interaction.setEmbedImage(user.getCurrentAvatarImageUrl());
                msg = interaction.replaceMsg(msg, "%tags", user.getTags().toString());
                msg = interaction.replaceMsg(msg, "%newlevel", KarrenUtil.getVRCSafetySystemRank(user.getTags()));
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            msg = interaction.getRandomTemplate("noparam").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "VRCUserSearch";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.EMBED_LINKS);
    }
}
