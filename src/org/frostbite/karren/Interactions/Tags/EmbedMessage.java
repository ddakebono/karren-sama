/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import discord4j.core.spec.EmbedCreateSpec;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.awt.*;


public class EmbedMessage extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        User author = result.getEvent().getMessage().getAuthor().block();
        User bot = Karren.bot.getClient().getSelf().block();
        if(author != null && bot !=null) {
            EmbedCreateSpec embed = new EmbedCreateSpec();
            embed.setColor(Color.red);
            embed.setTitle((interaction.getFriendlyName() != null && interaction.getFriendlyName().length() > 0) ? interaction.getFriendlyName() : interaction.getIdentifier());
            embed.setDescription(msg);
            if (interaction.getEmbedImage() != null)
                embed.setImage(interaction.getEmbedImage());
            if (interaction.getEmbedURL() != null)
                embed.setUrl(interaction.getEmbedURL());
            if (interaction.getEmbedFooter() != null)
                embed.setFooter("Requested By: " + author.getUsername() + " | " + interaction.getEmbedFooter(), bot.getAvatarUrl());
            else
                embed.setFooter("Requested By: " + author.getUsername(), bot.getAvatarUrl());
            if ((interaction.getEmbedFields() != null && interaction.getEmbedFields().size() > 0 && interaction.getReplacementTextCount() > 0) || interaction.isTagAddedEmbeds()) {
                for (InteractionEmbedFields field : interaction.getEmbedFields()) {
                    if (interaction.isTagAddedEmbeds())
                        embed.addField(field.getFieldTitle(), field.getFieldValue(), field.isInline());
                    else
                        embed.addField(field.getFieldTitle(), interaction.getReplacementText(field.getFieldValue()), field.isInline());
                }
            }
            result.getMessage().setEmbed(embed);
            return null;
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "embedded";
    }

    @Override
    public PermissionSet getRequiredPermissions() {
        return PermissionSet.of(Permission.SEND_MESSAGES, Permission.EMBED_LINKS);
    }
}
