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

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import java.awt.*;
import java.util.EnumSet;

public class EmbedMessage extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.withColor(Color.RED);
        embed.withTitle((interaction.getFriendlyName()!=null&&interaction.getFriendlyName().length()>0)?interaction.getFriendlyName():interaction.getIdentifier());
        embed.withDescription(msg);
        if(interaction.getEmbedImage()!=null)
            embed.withImage(interaction.getEmbedImage());
        if(interaction.getEmbedURL()!=null)
            embed.withUrl(interaction.getEmbedURL());
        if(interaction.getEmbedFooter()!=null)
            embed.withFooterText("Requested By: " + event.getAuthor().getName() + " | " + interaction.getEmbedFooter());
        else
            embed.withFooterText("Requested By: " + event.getAuthor().getName());
        if((interaction.getEmbedFields()!=null && interaction.getEmbedFields().size()>0 && interaction.getReplacementTextCount()>0) || interaction.isTagAddedEmbeds()){
            for(InteractionEmbedFields field : interaction.getEmbedFields()){
                if(interaction.isTagAddedEmbeds())
                    embed.appendField(field.getFieldTitle(), field.getFieldValue(), field.isInline());
                else
                    embed.appendField(field.getFieldTitle(), interaction.getReplacementText(field.getFieldValue()), field.isInline());
            }
        }
        interaction.setEmbed(embed);
        return null;
    }

    @Override
    public String getTagName() {
        return "embedded";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.EMBED_LINKS);
    }
}
