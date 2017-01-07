package org.frostbite.karren.interactions.Tags.D4JPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;


public class D4JPlay implements Tag{

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        GuildMusicManager gm = Karren.bot.getGuildMusicManager(event.getGuild());
        final String[] message = {msg};
        if(interaction.hasParameter()){
            Karren.bot.getPm().loadItemOrdered(gm, interaction.getParameter(), new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    connectToVoiceChannel(event);
                    gm.scheduler.queue(audioTrack);
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    connectToVoiceChannel(event);
                    for(AudioTrack track : audioPlaylist.getTracks()){
                        gm.scheduler.queue(track);
                    }
                }

                @Override
                public void noMatches() {
                    message[0] = interaction.getRandomTemplatesFail();
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    message[0] = interaction.getRandomTemplatesPermError();
                }
            });
        }

        return message[0];
    }

    public void connectToVoiceChannel(MessageReceivedEvent event){
        if(!event.getMessage().getAuthor().getConnectedVoiceChannels().get(0).isConnected()){
            if(event.getMessage().getAuthor().getConnectedVoiceChannels().size()>0){
                try {
                    event.getMessage().getAuthor().getConnectedVoiceChannels().get(0).join();
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}