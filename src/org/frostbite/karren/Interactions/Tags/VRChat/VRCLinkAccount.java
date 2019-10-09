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

import discord4j.core.object.entity.User;
import io.github.vrchatapi.VRCFriends;
import io.github.vrchatapi.VRCUser;
import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.List;
import java.util.Optional;

public class VRCLinkAccount extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Optional<User> userOpt = result.getEvent().getMessage().getAuthor();
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if (interaction.hasParameter()) {
                if (Karren.bot.sql.getUserData(user).getVrcUserID() == null) {
                    List<VRCUser> users = VRCUser.list(0, 1, false, interaction.getParameter());
                    if (users.size() > 0) {
                        VRCUser vrcUser = VRCUser.fetch(users.get(0).getId());
                        VRCFriends.sendFriendRequest(vrcUser);
                        DbUser dbUser = Karren.bot.sql.getUserData(user);
                        dbUser.setVrcUserID(vrcUser.getId());
                        dbUser.update();
                        msg = interaction.replaceMsg(msg, "%username", vrcUser.getDisplayName());
                    } else {
                        msg = interaction.getRandomTemplate("fail").getTemplate();
                    }
                } else {
                    msg = interaction.getRandomTemplate("linked").getTemplate();
                }
            } else {
                msg = interaction.getRandomTemplate("noparam").getTemplate();
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "VRCAddFriend";
    }

}
