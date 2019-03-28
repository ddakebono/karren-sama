/*
 * Copyright (c) 2019 Owen Bennett.
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
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.VoiceChannel;
import discord4j.voice.VoiceConnection;

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


  public VoiceChannel voiceChannel;

  public VoiceConnection voiceConn;

  /**
   * Creates a player and a track scheduler.
   * @param manager Audio player manager to use for creating the player.
   */
  public GuildMusicManager(AudioPlayerManager manager, Guild guild) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player, guild);
    player.addListener(scheduler);
  }

  public VoiceChannel getVoiceChannel() {
    return voiceChannel;
  }

  public void setVoiceChannel(VoiceChannel voiceChannel) {
    this.voiceChannel = voiceChannel;
  }

  public VoiceConnection getVoiceConn() {
    return voiceConn;
  }

  public void setVoiceConn(VoiceConnection voiceConn) {
    this.voiceConn = voiceConn;
  }

  /**
   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
   */
  public KarrenAudioProvider getAudioProvider() {
    return new KarrenAudioProvider(player);
  }

}
