/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import com.github.twitch4j.helix.domain.*;
import org.frostbite.karren.Database.Objects.DbStreamer;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatusListener implements IListener<PresenceUpdateEvent> {

    String regex = "/(?:(?:http|https):\\/\\/)?(?:www.)?(?:twitch.tv)\\/([A-Za-z0-9-_\\.]+)";

    @Override
    public void handle(PresenceUpdateEvent presenceUpdateEvent) {
        if(presenceUpdateEvent.getNewPresence().getStreamingUrl().isPresent()){
            List<DbStreamer> streamers = Karren.bot.getSql().getStreamers(presenceUpdateEvent.getUser());
            //Stream link supplied
            if(streamers.size()>0 && presenceUpdateEvent.getNewPresence().getStreamingUrl().get().contains("twitch")){

                Pattern pattern = Pattern.compile(regex);
                Matcher matches = pattern.matcher(presenceUpdateEvent.getNewPresence().getStreamingUrl().get());
                matches.find();
                if(matches.groupCount()>=0) {

                    LinkedList<String> userLogin = new LinkedList<>();
                    userLogin.addFirst(matches.group(1));

                    StreamList result = Karren.bot.ttv.getHelix().getStreams("", "", 1, null, null, null, null, userLogin).execute();



                    if(result.getStreams().size()>0) {
                        Stream stream = result.getStreams().get(0);

                        LinkedList<String> gameID = new LinkedList<>();
                        gameID.addFirst(stream.getGameId().toString());
                        GameList resultGame = Karren.bot.ttv.getHelix().getGames(gameID, null).execute();

                        LinkedList<Long> userID = new LinkedList<>();
                        userID.addFirst(stream.getUserId());
                        UserList resultUser = Karren.bot.ttv.getHelix().getUsers("", userID, null).execute();

                        if(resultGame.getGames().size()>0 && resultUser.getUsers().size()>0) {

                            //Generate stream start message
                            EmbedObject embed = getEmbedMessage(stream, resultUser.getUsers().get(0), resultGame.getGames().get(0), presenceUpdateEvent.getNewPresence().getStreamingUrl().get());

                            for (DbStreamer streamer : streamers) {
                                IGuild guild = Karren.bot.client.getGuildByID(streamer.getGuildID());
                                IChannel channel = guild.getChannelByID(Karren.bot.sql.getGuild(guild).getStreamAnnounceChannel());
                                if(Karren.bot.sql.getGuild(guild).getStreamAnnounceMentionRole()!=0) {
                                    IRole role = Karren.bot.client.getRoleByID(Karren.bot.sql.getGuild(guild).getStreamAnnounceMentionRole());
                                    channel.sendMessage(role.mention(), embed);
                                } else {
                                    channel.sendMessage(embed);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private EmbedObject getEmbedMessage(Stream stream, User user, Game game, String url){
        EmbedBuilder embed = new EmbedBuilder();
        embed.withTitle(user.getDisplayName() + " has just went live!");
        embed.withDescription(url);
        embed.withThumbnail(user.getProfileImageUrl());
        embed.appendField("Title", stream.getTitle(), false);
        embed.appendField("Streaming", game.getName(), false);
        embed.withImage(stream.getThumbnailUrl(1280, 720));
        embed.withColor(Color.RED);
        return embed.build();
    }
}
