/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.List;

public class CreateTempChannel extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        List<Category> categoryList = result.getEvent().getGuild().getCategoriesByName("temp voice channels", true);
        int maxChannels = 0;
        if(categoryList.size()>0){
            Category category = categoryList.get(0);
            String[] nameSplit = category.getName().split("-");
            if(nameSplit.length>1)
                maxChannels = Integer.parseInt(nameSplit[1].trim());
            if(interaction.hasParameter() && (category.getVoiceChannels().size()<maxChannels || maxChannels==0)) {
                VoiceChannel newVC = category.createVoiceChannel(interaction.getParameter()).complete();
                msg = interaction.replaceMsg(msg, "%channel", newVC.getName());
                //Create invite to monitor when channel expires
                if(Karren.conf.isTestMode()) {
                    newVC.createInvite().setMaxAge(60).setMaxUses(1).setTemporary(false).setUnique(true).complete();
                } else {
                    newVC.createInvite().setMaxAge(86400).setMaxUses(1).setTemporary(false).setUnique(true).complete();
                }
            } else if(interaction.hasParameter()) {
                return interaction.getRandomTemplate("limited").getTemplate();
            } else {
                return interaction.getRandomTemplate("noparam").getTemplate();
            }
        } else {
            return interaction.getRandomTemplate("nocategory").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "createtempchannel";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_CHANNEL, Permission.MESSAGE_WRITE};
    }
}
