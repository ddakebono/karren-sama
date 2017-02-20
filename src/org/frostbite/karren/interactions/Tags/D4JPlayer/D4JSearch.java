/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.D4JPlayer;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;
import java.io.IOException;

public class D4JSearch implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()){
            String searchText = interaction.getParameter().replaceAll(" ", "+");
            String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=rating&q=" + searchText + "&key=" + Karren.conf.getGoogleAPIKey();
            Document doc = null;
            try {
                doc = Jsoup.connect(searchUrl).timeout(10*1000).ignoreContentType(true).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(doc!=null){
                String json = doc.text();
                JSONObject jsonObject = (JSONObject)new JSONTokener(json).nextValue();
                msg = msg.replace("%title", jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getString("title"));
                interaction.setParameter(jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId"));
            }
        } else {
            msg = interaction.getRandomTemplatesFail();
        }
        return msg;
    }
}
