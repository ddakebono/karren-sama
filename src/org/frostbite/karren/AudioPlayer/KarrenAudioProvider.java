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

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

/**
 * This is a wrapper around AudioPlayer which makes it behave as an IAudioProvider for D4J. As D4J calls canProvide
 * before every call to provide(), we pull the frame in canProvide() and use the frame we already pulled in
 * provide().
 */
public class KarrenAudioProvider extends AudioProvider {
  private final AudioPlayer audioPlayer;
  private MutableAudioFrame frame = new MutableAudioFrame();

  /**
   * @param audioPlayer Audio player to wrap.
   */
  public KarrenAudioProvider(final AudioPlayer audioPlayer) {
    // Allocate a ByteBuffer for Discord4J's AudioProvider to hold audio data for Discord
    super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
    // Set LavaPlayer's MutableAudioFrame to use the same buffer as the one we just allocated
    frame.setBuffer(getBuffer());
    this.audioPlayer = audioPlayer;
  }

  @Override
  public boolean provide() {
    // AudioPlayer writes audio data to its AudioFrame
    final boolean didProvide = audioPlayer.provide(frame);
    // If audio was provided, flip from write-mode to read-mode
    if (didProvide) {
      getBuffer().flip();
    }
    return didProvide;
  }
}
