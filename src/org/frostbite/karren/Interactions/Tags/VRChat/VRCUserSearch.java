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
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.KarrenUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class VRCUserSearch extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter()){
            String search = interaction.getParameter();
            List<VRCUser> users = new LinkedList<>();
            VRCUser user = null;
            if(!search.startsWith("usr_")) {
                users = VRCUser.list(0, 15, false, search);
            }
            else {
                user = VRCUser.fetch(search);
            }
            if(users.size()==1 || user!=null) {
                //VRCUser user = VRCUser.fetch(users.get(0).getId());
                if(user==null) {
                    Optional<VRCUser> userOpt = users.stream().filter(userFind -> userFind.getDisplayName().toLowerCase().equals(search.toLowerCase())).findFirst();
                    if (userOpt.isPresent()) {
                        user = VRCUser.fetch(userOpt.get().getId());
                    } else {
                        user = VRCUser.fetch(users.get(0).getId());
                    }
                }
                if (user.isFriend()) {
                    if (!user.getWorldID().equalsIgnoreCase("offline")) {
                        if (!user.getWorldID().equalsIgnoreCase("private")) {
                            VRCWorld world = VRCWorld.fetch(user.getWorldID());
                            msg = interaction.replaceMsg(msg, "%world", world.getName());
                            if (user.getInstanceID().length() < 10)
                                msg = interaction.replaceMsg(msg, "%link", "[Join Me](https://www.vrchat.net/launch?worldId=" + world.getId() + "&instanceId=" + user.getInstanceID() + ")");
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
                msg = interaction.replaceMsg(msg, "%status", user.getState().length()>0?user.getState():"Not Friend");
                if(user.getStatusDesc().length()>0)
                    msg = interaction.replaceMsg(msg, "%statdesc", user.getStatusDesc());
                else
                    msg = interaction.replaceMsg(msg, "%statdesc", "None");
                msg = interaction.replaceMsg(msg, "%username", user.getUsername());
                msg = interaction.replaceMsg(msg, "%displayname", user.getDisplayName());
                msg = interaction.replaceMsg(msg, "%userid", user.getId());
                msg = interaction.replaceMsg(msg, "%last_login", user.getLastLogin());
                msg = interaction.replaceMsg(msg, "%last_platform", user.getLastPlatform());
                msg = interaction.replaceMsg(msg, "%bio", user.getBio());
                interaction.setEmbedImage(user.getCurrentAvatarImageUrl());
                msg = interaction.replaceMsg(msg, "%tags", user.getTags().toString());
                msg = interaction.replaceMsg(msg, "%newlevel", KarrenUtil.getVRCSafetySystemRank(user.getTags()));
            } else {
                if(users.size()>1){
                    //Display search results
                    msg = interaction.getRandomTemplate("search").getTemplate();
                    msg = interaction.replaceMsg(msg, "%search", search);
                    result.setEmbedTemplateType("search");
                    for(VRCUser userSearch : users){
                        interaction.addEmbedField(new InteractionEmbedFields(
                                userSearch.getDisplayName(),
                                userSearch.getId(),
                                false
                        ));
                    }
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                    result.setEmbedTemplateType("fail");
                }
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

}
