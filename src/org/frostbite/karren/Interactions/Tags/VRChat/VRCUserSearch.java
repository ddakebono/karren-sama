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

import io.github.vrchatapi.ApiException;
import io.github.vrchatapi.model.LimitedUser;
import io.github.vrchatapi.model.User;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class VRCUserSearch extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter() && Karren.bot.getVrcUser()!=null){
            String search = interaction.getParameter();
            List<LimitedUser> users = new LinkedList<>();
            User user = null;
            Karren.bot.refreshCurrentUser();
            if(!search.startsWith("usr_")) {
                try {
                    users = Karren.bot.getUsersApi().searchUsers(search, null, 15, 0);
                } catch (ApiException e) {
                    Karren.log.error("Exception during searchUsers! " + e.getCode());
                    Karren.log.error("Reason: " + e.getResponseBody());
                    Karren.log.error("Response Headers: " + e.getResponseHeaders());
                    e.printStackTrace();
                }
            }
            else {
                try {
                    user = Karren.bot.getUsersApi().getUser(search);
                } catch (ApiException e) {
                    Karren.log.error("Exception during getUser! " + e.getCode());
                    Karren.log.error("Reason: " + e.getResponseBody());
                    Karren.log.error("Response Headers: " + e.getResponseHeaders());
                    e.printStackTrace();
                }
            }
            if(users.size()==1 || user!=null) {
                //VRCUser user = VRCUser.fetch(users.get(0).getId());
                if(user==null) {
                    try {
                        Optional<LimitedUser> userOpt = users.stream().filter(userFind -> userFind.getDisplayName().equalsIgnoreCase(search)).findFirst();
                        if (userOpt.isPresent()) {
                            user = Karren.bot.getUsersApi().getUser(userOpt.get().getId());
                        } else {
                            user = Karren.bot.getUsersApi().getUser(users.get(0).getId());
                        }
                    } catch (ApiException e){
                        Karren.log.error("Exception during fetchFromSearch! " + e.getCode());
                        Karren.log.error("Reason: " + e.getResponseBody());
                        Karren.log.error("Response Headers: " + e.getResponseHeaders());
                        e.printStackTrace();
                    }
                }

                assert user != null;
                if(user.getStatusDescription().length()>0)
                    msg = interaction.replaceMsg(msg, "%statdesc", user.getStatusDescription());
                else
                    msg = interaction.replaceMsg(msg, "%statdesc", "None");
                msg = interaction.replaceMsg(msg, "%username", user.getUsername());
                msg = interaction.replaceMsg(msg, "%displayname", user.getDisplayName());
                msg = interaction.replaceMsg(msg, "%userid", user.getId());
                msg = interaction.replaceMsg(msg, "%last_login", user.getLastLogin());
                msg = interaction.replaceMsg(msg, "%last_platform", user.getLastPlatform());
                msg = interaction.replaceMsg(msg, "%bio", user.getBio());
                msg = interaction.replaceMsg(msg, "%devtype", user.getDeveloperType().toString());
                msg = interaction.replaceMsg(msg, "%joindate", user.getDateJoined().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                msg = interaction.replaceMsg(msg, "%avatarcopy", Boolean.toString(user.getAllowAvatarCopying()));
                msg = interaction.replaceMsg(msg, "%vrcplus", user.getTags().contains("system_supporter")?"(VRC+)":"");
                if(user.getProfilePicOverride().trim().isEmpty())
                    interaction.setEmbedImage(user.getCurrentAvatarImageUrl());
                else
                    interaction.setEmbedImage(user.getProfilePicOverride());
                msg = interaction.replaceMsg(msg, "%tags", formatTags(user.getTags()));
                msg = interaction.replaceMsg(msg, "%newlevel", KarrenUtil.getVRCSafetySystemRank(user.getTags()));
            } else {
                if(users.size()>1){
                    //Display search results
                    msg = interaction.getRandomTemplate("search").getTemplate();
                    msg = interaction.replaceMsg(msg, "%search", search);
                    result.setEmbedTemplateType("search");
                    for(LimitedUser userSearch : users){
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
            if(Karren.bot.getUsersApi()==null)
                msg = interaction.getRandomTemplate("apidead").getTemplate();
        }
        return msg;
    }

    private String formatTags(List<String> tags){
        StringBuilder builder = new StringBuilder();

        if(tags.size()>0) {
            for (String tag : tags) {
                builder.append(tag).append(System.lineSeparator());
            }
        } else {
          builder.append("User has no tags!");
        }

        return builder.toString().trim();
    }

    @Override
    public String getTagName() {
        return "VRCUserSearch";
    }

}
