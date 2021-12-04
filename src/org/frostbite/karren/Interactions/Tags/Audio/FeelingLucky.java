/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Audio;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.api.Permission;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FeelingLucky extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter()) {
            String searchText = interaction.getParameter().replaceAll(" ", "+");
            try {
                YouTube.Search.List search = Karren.bot.yt.search().list(Collections.singletonList("id, snippet"));
                search.setKey(Karren.conf.getGoogleAPIKey());
                search.setQ(searchText);
                search.setType(Collections.singletonList("video"));
                search.setFields("items(id/videoId, snippet/title)");
                search.setMaxResults(1L);

                SearchListResponse slr = search.execute();
                List<SearchResult> results = slr.getItems();

                if(results.size()>0){
                    msg = interaction.replaceMsg(msg, "%title", results.get(0).getSnippet().getTitle());
                    interaction.setParameter(results.get(0).getId().getVideoId());
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

    @Override
    public String getTagName() {
        return "feelinglucky";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
    }

    @Override
    public Boolean getVoiceUsed() {
        return true;
    }
}
