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

import io.github.vrchatapi.VRCWorld;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class VRCWorldSearch extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter()){
            String search = interaction.getParameter();
            List<VRCWorld> worlds = new LinkedList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm");
            VRCWorld world = null;
            if(!search.startsWith("wrld_")) {
                worlds = VRCWorld.list(0, 15, false, search);
            } else {
                world = VRCWorld.fetch(search);
            }
            if(worlds.size()==1 || world!=null){
                if(world==null) {
                    world = worlds.get(0);
                    world.getFull();
                }
                interaction.setEmbedImage(world.getThumbnailImageUrl());
                msg = interaction.replaceMsg(msg, "%name", world.getName());
                msg = interaction.replaceMsg(msg, "%author", world.getAuthorName());
                msg = interaction.replaceMsg(msg, "%relstatus", world.getReleaseStatus().name());
                msg = interaction.replaceMsg(msg, "%occupants", Integer.toString(world.getOccupants()));
                msg = interaction.replaceMsg(msg, "%capacity", Integer.toString(world.getCapacity()));
                msg = interaction.replaceMsg(msg, "%visits", Integer.toString(world.getTotalVisits()));
                if(!world.getUpdatedAt().equalsIgnoreCase("none")) {
                    msg = interaction.replaceMsg(msg, "%updated", formatter.format(Date.from(Instant.parse(world.getUpdatedAt()))));
                } else {
                    msg = interaction.replaceMsg(msg, "%updated", "Never");
                }

                if(!world.getCreatedAt().equalsIgnoreCase("none")) {
                    msg = interaction.replaceMsg(msg, "%created", formatter.format(Date.from(Instant.parse(world.getCreatedAt()))));
                } else {
                    msg = interaction.replaceMsg(msg, "%created", "Never");
                }

                if(!world.getPublicationDate().equalsIgnoreCase("none")) {
                    msg = interaction.replaceMsg(msg, "%published", formatter.format(Date.from(Instant.parse(world.getPublicationDate()))));
                } else {
                    msg = interaction.replaceMsg(msg, "%published", "Never");
                }

                if(!world.getLabsPublicationDate().equalsIgnoreCase("none")) {
                    msg = interaction.replaceMsg(msg, "%labsPublished", formatter.format(Date.from(Instant.parse(world.getLabsPublicationDate()))));
                } else {
                    msg = interaction.replaceMsg(msg, "%labsPublished", "Never");
                }
                msg = interaction.replaceMsg(msg, "%occuPrivate", Integer.toString(world.getPrivateOccupants()));
                msg = interaction.replaceMsg(msg, "%occuPublic", Integer.toString(world.getPublicOccupants()));
                msg = interaction.replaceMsg(msg, "%worldid", world.getId());
                msg = interaction.replaceMsg(msg, "%favorites", Integer.toString(world.getTotalLikes()));
                msg = interaction.replaceMsg(msg, "%tags", world.getTags().toString());
                msg = interaction.replaceMsg(msg, "%description", world.getDescription());
            } else {
                if(worlds.size()>1){
                    //Search
                    msg = interaction.getRandomTemplate("search").getTemplate();
                    msg = interaction.replaceMsg(msg, "%search", search);
                    result.setEmbedTemplateType("search");
                    for(VRCWorld worldSearch : worlds){
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
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "VRCWorldSearch";
    }

}
