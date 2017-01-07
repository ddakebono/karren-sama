package org.frostbite.karren.interactions.Tags.D4JPlayer;

import org.frostbite.karren.AudioPlayer.AudioResultHandler;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;


public class D4JPlay implements Tag{

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        GuildMusicManager gm = Karren.bot.getGuildMusicManager(event.getGuild());
        String voiceFile = interaction.getRandomVoiceFile();
        if(interaction.hasParameter() || voiceFile != null){
            AudioResultHandler arh = new AudioResultHandler(event, interaction, gm, msg);
            gm.player.setVolume(Math.round(interaction.getVoiceVolume()));
            if(voiceFile != null){
                Karren.bot.getPm().loadItemOrdered(gm, voiceFile, arh);
            } else {
                Karren.bot.getPm().loadItemOrdered(gm, interaction.getParameter(), arh);
            }

            if(arh.isFailed()){
                msg = arh.getMsg();
            }
        } else {
            msg = interaction.getRandomTemplatesFail();
        }

        return msg;
    }


}