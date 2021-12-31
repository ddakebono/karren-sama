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
import io.github.vrchatapi.model.LimitedWorld;
import io.github.vrchatapi.model.World;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class VRCWorldSearch extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter() && Karren.bot.getWorldsApi()!=null){
            String search = interaction.getParameter();
            List<LimitedWorld> worlds = new LinkedList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy hh:mm");
            World world = null;
            Karren.bot.refreshCurrentUser();
            if(!search.startsWith("wrld_")) {
                try {
                    worlds = Karren.bot.getWorldsApi().searchWorlds(null, null, null, null, 15, "descending", 0, search, null, null, null, null, null, null);
                } catch (ApiException e) {
                    Karren.log.error("Exception during searchWorlds! " + e.getCode());
                    Karren.log.error("Reason: " + e.getResponseBody());
                    Karren.log.error("Response Headers: " + e.getResponseHeaders());
                    e.printStackTrace();
                }
            } else {
                try {
                    world = Karren.bot.getWorldsApi().getWorld(search);
                } catch (ApiException e) {
                    Karren.log.error("Exception during getWorld! " + e.getCode());
                    Karren.log.error("Reason: " + e.getResponseBody());
                    Karren.log.error("Response Headers: " + e.getResponseHeaders());
                    e.printStackTrace();
                }
            }
            if(worlds.size()==1 || world!=null){
                if(world==null) {
                    LimitedWorld limWorld = worlds.get(0);
                    try {
                        world = Karren.bot.getWorldsApi().getWorld(limWorld.getId());
                    } catch (ApiException e) {
                        Karren.log.error("Exception during getWorldFullFromSearch! " + e.getCode());
                        Karren.log.error("Reason: " + e.getResponseBody());
                        Karren.log.error("Response Headers: " + e.getResponseHeaders());
                        e.printStackTrace();
                    }
                }
                if(world!=null) {
                    interaction.setEmbedImage(world.getThumbnailImageUrl());
                    msg = interaction.replaceMsg(msg, "%name", world.getName());
                    msg = interaction.replaceMsg(msg, "%author", world.getAuthorName());
                    msg = interaction.replaceMsg(msg, "%relstatus", world.getReleaseStatus().name());
                    msg = interaction.replaceMsg(msg, "%occupants", Objects.requireNonNull(world.getOccupants()).toString());
                    msg = interaction.replaceMsg(msg, "%capacity", world.getCapacity().toString());
                    msg = interaction.replaceMsg(msg, "%visits", world.getVisits().toString());
                    msg = interaction.replaceMsg(msg, "%updated", formatter.format(world.getUpdatedAt()));
                    msg = interaction.replaceMsg(msg, "%created", formatter.format(world.getCreatedAt()));
                    msg = interaction.replaceMsg(msg, "%published", world.getPublicationDate());
                    msg = interaction.replaceMsg(msg, "%labsPublished", world.getLabsPublicationDate());
                    msg = interaction.replaceMsg(msg, "%occuPrivate", Objects.requireNonNull(world.getPrivateOccupants()).toString());
                    msg = interaction.replaceMsg(msg, "%occuPublic", Objects.requireNonNull(world.getPublicOccupants()).toString());
                    msg = interaction.replaceMsg(msg, "%worldid", world.getId());
                    msg = interaction.replaceMsg(msg, "%favorites", Objects.requireNonNull(world.getFavorites()).toString());
                    msg = interaction.replaceMsg(msg, "%tags", world.getTags().toString());
                    msg = interaction.replaceMsg(msg, "%description", world.getDescription());
                }
            } else {
                if(worlds.size()>1){
                    //Search
                    msg = interaction.getRandomTemplate("search").getTemplate();
                    msg = interaction.replaceMsg(msg, "%search", search);
                    result.setEmbedTemplateType("search");
                    for(LimitedWorld worldSearch : worlds){
                        interaction.addEmbedField(new InteractionEmbedFields(
                                worldSearch.getName(),
                                worldSearch.getId(),
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
            if(Karren.bot.getWorldsApi()==null)
                msg = interaction.getRandomTemplate("apidead").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "VRCWorldSearch";
    }

}
