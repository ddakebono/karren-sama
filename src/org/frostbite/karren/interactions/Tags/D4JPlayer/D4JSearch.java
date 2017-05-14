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
import java.util.Arrays;
import java.util.HashMap;

public class D4JSearch implements Tag {

    HashMap<String, JSONArray> resultArrays = new HashMap<>();

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()){
            String searchText = interaction.getParameter().replaceAll(" ", "+");
            String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=3&q=" + searchText + "&key=" + Karren.conf.getGoogleAPIKey();
            Document doc = null;
            try {
                doc = Jsoup.connect(searchUrl).timeout(10*1000).ignoreContentType(true).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(doc!=null){
                String json = doc.text();
                JSONObject jsonObject = (JSONObject)new JSONTokener(json).nextValue();
                if(jsonObject.getJSONArray("items").length()>0) {
                    if (Arrays.asList(interaction.getTags()).contains("feelinglucky")) {
                        msg = msg.replace("%title", jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getString("title"));
                        interaction.setParameter(jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId"));
                    } else {
                        JSONArray results = jsonObject.getJSONArray("items");
                        StringBuilder resultMessage = new StringBuilder();
                        for (int i = 0; i < results.length(); i++) {
                            resultMessage.append("\n").append(i + 1).append(" : ").append(results.getJSONObject(i).getJSONObject("snippet").getString("title"));
                        }
                        resultArrays.put(event.getAuthor().getStringID(), results);
                        Karren.bot.getGuildManager().getInteractionProcessor(event.getGuild()).getInteractions().add(new Interaction("selectOption", new String[]{"parameter", "d4jselect", "d4jplay"}, new String[]{"You selected \"%title\", added to queue!"}, new String[]{"That's not one of the options!"}, 1, event.getAuthor().getStringID(), interaction.getVoiceVolume()));
                        msg = msg.replace("%results", resultMessage.toString());
                    }
                } else {
                    msg = interaction.getRandomTemplatesFail();
                }
            }
        } else {
            msg = interaction.getRandomTemplatesFail();
        }
        return msg;
    }

    public JSONArray getResultArray(String userID) {
        JSONArray resultArray = resultArrays.get(userID);
        resultArrays.remove(userID);
        return resultArray;
    }
}
