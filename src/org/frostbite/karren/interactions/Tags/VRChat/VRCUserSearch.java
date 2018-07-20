package org.frostbite.karren.interactions.Tags.VRChat;

import io.github.vrchatapi.VRCAvatar;
import io.github.vrchatapi.VRCUser;
import io.github.vrchatapi.VRCWorld;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.List;

public class VRCUserSearch extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()){
            List<VRCUser> users = VRCUser.list(0, 1, false, interaction.getParameter());
            if(users.size()>0){
                VRCUser user = VRCUser.fetch(users.get(0).getId());
                if(user.isFriend()) {
                    if(!user.getWorldID().equalsIgnoreCase("offline")) {
                        VRCWorld world = VRCWorld.fetch(user.getLocation());
                        msg = interaction.replaceMsg(msg, "%world", world.getName());
                    } else {
                        msg = interaction.replaceMsg(msg, "%world", "Offline");
                    }
                }
                msg = interaction.replaceMsg(msg, "%username", user.getUsername());
                if(user.getAvatarId()!=null) {
                    VRCAvatar avatar = VRCAvatar.fetch(user.getAvatarId());
                    msg = interaction.replaceMsg(msg, "%avatar", avatar.getName());
                } else {
                    msg = interaction.replaceMsg(msg, "%avatar", "Offline");
                }
                interaction.setEmbedImage(user.getCurrentAvatarImageUrl());

            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
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

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
