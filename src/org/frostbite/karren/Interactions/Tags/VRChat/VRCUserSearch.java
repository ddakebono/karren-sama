/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.VRChat;

import io.github.vrchatapi.VRCUser;
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

                assert user != null;
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
                msg = interaction.replaceMsg(msg, "%devtype", user.getDeveloperType().toString());
                msg = interaction.replaceMsg(msg, "%joindate", user.getDateJoined());
                msg = interaction.replaceMsg(msg, "%avatarcopy", Boolean.toString(user.isAllowAvatarCopying()));
                msg = interaction.replaceMsg(msg, "%vrcplus", user.getTags().contains("system_supporter")?"(VRC+)":"");
                interaction.setEmbedImage(user.getCurrentAvatarImageUrl());
                msg = interaction.replaceMsg(msg, "%tags", formatTags(user.getTags()));
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

    private String formatTags(List<String> tags){
        StringBuilder builder = new StringBuilder();

        for(String tag : tags) {
            builder.append(tag).append(System.lineSeparator());
        }

        return builder.toString().trim();
    }

    @Override
    public String getTagName() {
        return "VRCUserSearch";
    }

}
