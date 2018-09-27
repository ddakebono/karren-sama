package org.frostbite.karren.interactions.Tags.VRChat;

import io.github.vrchatapi.VRCAnalytics;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class VRCUtils extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        int count = VRCAnalytics.getUserCount();
        msg = interaction.replaceMsg(msg, "%vrccount", Integer.toString(count));
        interaction.setEmbedFooter("Using VRChat API - Users Online: " + count);
        return msg;
    }

    @Override
    public String getTagName() {
        return "vrcanalytics";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
