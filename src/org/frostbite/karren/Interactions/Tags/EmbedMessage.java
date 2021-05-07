/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.awt.*;

public class EmbedMessage extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.red);
        embed.setTitle((interaction.getFriendlyName() != null && interaction.getFriendlyName().length() > 0) ? interaction.getFriendlyName() : interaction.getIdentifier());
        if(interaction.getEmbedURL()!=null)
            embed.setTitle((interaction.getFriendlyName() != null && interaction.getFriendlyName().length() > 0) ? interaction.getFriendlyName() : interaction.getIdentifier(), interaction.getEmbedURL());
        embed.setDescription(msg);
        if(interaction.getEmbedThumbnail()!=null)
            embed.setThumbnail(interaction.getEmbedThumbnail());
        if (interaction.getEmbedImage() != null)
            embed.setImage(interaction.getEmbedImage());
        if (interaction.getEmbedFooter() != null)
            embed.setFooter("Requested By: " + result.getEvent().getAuthor().getName() + " | " + interaction.getEmbedFooter(), Karren.bot.client.getSelfUser().getAvatarUrl());
        else
            embed.setFooter("Requested By: " + result.getEvent().getAuthor().getName(), Karren.bot.client.getSelfUser().getAvatarUrl());
        InteractionEmbedFields[] embedFields = interaction.getEmbedFields(result.getEmbedTemplateType());
        if ((embedFields!=null && embedFields.length > 0 && interaction.getReplacementTextCount() > 0) || interaction.getTempAddedEmbedFields().size()>0) {
            if(embedFields!=null) {
                for (InteractionEmbedFields field : embedFields) {
                    embed.addField(field.getFieldTitle(), interaction.getReplacementText(field.getFieldValue()), field.isInline());
                }
            }
            for(InteractionEmbedFields field : interaction.getTempAddedEmbedFields()){
                embed.addField(field.getFieldTitle(), field.getFieldValue(), field.isInline());
            }
        }
        result.setEmbed(embed.build());
        return null;
    }

    @Override
    public String getTagName() {
        return "embedded";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_WRITE};
    }
}
