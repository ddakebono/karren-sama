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

import io.github.vrchatapi.VRCFriends;
import io.github.vrchatapi.VRCUser;
import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class VRCUnlinkAccount extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.getMentionedUsers().size()>0){
            DbUser user = Karren.bot.sql.getUserData(interaction.getMentionedUsers().get(0));
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

}
