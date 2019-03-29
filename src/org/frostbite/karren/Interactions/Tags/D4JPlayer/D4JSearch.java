/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.D4JPlayer;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionTemplate;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class D4JSearch extends Tag {

    HashMap<String, List<Video>> resultArrays = new HashMap<>();

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()) {
            String searchText = interaction.getParameter().replaceAll(" ", "+");
            try {
                YouTube.Search.List search = Karren.bot.yt.search().list("id");
                search.setKey(Karren.conf.getGoogleAPIKey());
                search.setQ(searchText);
                search.setType("video");
                search.setFields("items(id/videoId)");
                search.setMaxResults(3L);

                SearchListResponse slr = search.execute();
                List<SearchResult> results = slr.getItems();
                ArrayList<Video> videos = new ArrayList<>();
                for(SearchResult result : results){
                    YouTube.Videos.List list = Karren.bot.yt.videos().list("id, snippet, contentDetails").setId(result.getId().getVideoId());
                    list.setKey(Karren.conf.getGoogleAPIKey());
                    VideoListResponse vlr = list.execute();
                    videos.add(vlr.getItems().get(0));
                    interaction.addEmbedField(new InteractionEmbedFields(
                            0,
                            (results.indexOf(result)+1) + " : " + vlr.getItems().get(0).getSnippet().getTitle(),
                            "Duration: " + KarrenUtil.getMinSecFormattedString(Duration.parse(vlr.getItems().get(0).getContentDetails().getDuration()).toMillis()),
                            false
                    ));
                }
                if(results.size()>0){
                    resultArrays.put(event.getAuthor().getStringID(), videos);
                    Interaction selectInteraction = new Interaction("selectOption", new String[]{"novoicehijack", "parameter", "d4jselect", "d4jplay"}, new InteractionTemplate[]{new InteractionTemplate("You selected \"%title\", added to queue!", "normal", null), new InteractionTemplate("That's not one of the options!", "fail", null),  new InteractionTemplate("You must be in the same voice channel as me to control music.", "nohijack", null)}, 1, event.getAuthor().getStringID(), interaction.getVoiceVolume());
                    selectInteraction.getTagCache().add(this);
                    selectInteraction.setNoClearInteraction(true);
                    Karren.bot.getGuildManager().getInteractionProcessor(event.getGuild()).getInteractions().add(selectInteraction);
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                }
            } catch (GoogleJsonResponseException e){
                msg = interaction.getRandomTemplate("fail-except").getTemplate();
                Karren.log.error("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                msg = interaction.getRandomTemplate("fail-except").getTemplate();
                Karren.log.error("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            }
        } else {
            msg = interaction.getRandomTemplate("noparams").getTemplate();
        }
        return msg;
    }

    public List<Video> getResultArray(String id){
        List<Video> results = resultArrays.get(id);
        resultArrays.remove(id);
        return results;
    }

    @Override
    public String getTagName() {
        return "d4jsearch";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.VOICE_CONNECT, Permissions.VOICE_SPEAK);
    }

    @Override
    public Boolean getVoiceUsed() {
        return true;
    }
}
