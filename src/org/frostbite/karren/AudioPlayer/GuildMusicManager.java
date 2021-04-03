/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.AudioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
  /**
   * Audio player for the guild.
   */
  public final AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  /**
   * Creates a player and a track scheduler.
   * @param manager Audio player manager to use for creating the player.
   */
  public GuildMusicManager(AudioPlayerManager manager, Guild guild) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player, guild);
    player.addListener(scheduler);
  }

  /**
   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
   */
  public AudioProvider getAudioProvider() {
    return new AudioProvider(player);
  }

  public boolean connectToVoiceChannel(MessageReceivedEvent event) {
    if (event.getMember() != null) {
      if (event.getMember().getVoiceState() != null) {
        if (event.getMember().getVoiceState().inVoiceChannel()) {
          AudioManager am = event.getGuild().getAudioManager();
          if (!am.isConnected()) {
            //Connect to chat
            am.openAudioConnection(event.getMember().getVoiceState().getChannel());
          }
        } else {
          return true;
        }
      } else {
        return true;
      }
    }
    return false;
  }
}
