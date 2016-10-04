package org.frostbite.karren.interactions.Tags.D4JPlayer;

import net.dv8tion.d4j.player.MusicPlayer;
import net.dv8tion.jda.player.Playlist;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.frostbite.karren.listeners.D4JPlayerVoiceExit;
import sx.blah.discord.handle.audio.impl.DefaultProvider;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class D4JPlay implements Tag{

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        MusicPlayer player;
        if(event.getMessage().getGuild().getAudioManager().getAudioProvider() instanceof DefaultProvider){
            player = new MusicPlayer();
            player.setVolume(interaction.getVoiceVolume());
            player.addEventListener(new D4JPlayerVoiceExit());
            event.getMessage().getGuild().getAudioManager().setAudioProvider(player);
        } else {
            player = (MusicPlayer) event.getMessage().getGuild().getAudioManager().getAudioProvider();
        }
        Karren.log.debug("Loaded provider");
        if(interaction.hasParameter()){
            Playlist pls = Playlist.getPlaylist(interaction.getParameter());
            List<AudioSource> sources = new LinkedList<>(pls.getSources());
            if(sources.size()>1){
                Karren.log.debug("Found playlist with size " + sources.size());
                final MusicPlayer fPlayer = player;
                Thread plsRunner = new Thread(){
                    @Override
                    public void run(){
                        for(Iterator<AudioSource> it = sources.iterator(); it.hasNext();){
                            AudioSource source = it.next();
                            AudioInfo info = source.getInfo();
                            List<AudioSource> queue = fPlayer.getAudioQueue();
                            if(info.getError()==null){
                                queue.add(source);
                                if(fPlayer.isStopped())
                                    fPlayer.play();
                            } else {
                                Karren.log.error("Bad source " + info.getError());
                                it.remove();
                            }
                        }
                    }
                };
                plsRunner.start();
            } else {
                Karren.log.debug("Found single source");
                AudioSource source = sources.get(0);
                AudioInfo info = source.getInfo();
                if(info.getError()==null) {
                    player.getAudioQueue().add(source);
                    if (player.isStopped()) {
                        Karren.log.info("D4JPlayer started playback!");
                        player.play();
                    }
                } else {
                    msg = interaction.getRandomTemplatesFail().replace("%error", info.getError());
                    Karren.log.error("Bad source " +info.getError());
                }
            }
        } else if(!player.isPaused()) {
            player.pause();
        } else {
            msg = interaction.getRandomTemplatesPermError();
            player.play();
        }
        if(!Karren.bot.getClient().getConnectedVoiceChannels().contains(event.getMessage().getAuthor().getConnectedVoiceChannels().get(0))){
            if(event.getMessage().getAuthor().getConnectedVoiceChannels().size()>0){
                try {
                    event.getMessage().getAuthor().getConnectedVoiceChannels().get(0).join();
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;
    }
}
