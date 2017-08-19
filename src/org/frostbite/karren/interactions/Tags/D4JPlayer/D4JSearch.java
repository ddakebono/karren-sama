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
import org.frostbite.karren.interactions.InteractionTemplate;
import org.frostbite.karren.interactions.Tag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;

public class D4JSearch extends Tag {

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
                    JSONArray results = jsonObject.getJSONArray("items");
                    if (Arrays.asList(interaction.getTags()).contains("feelinglucky")) {
                        if(!results.getJSONObject(0).getJSONObject("id").isNull("videoId")) {
                            msg = msg.replace("%title", results.getJSONObject(0).getJSONObject("snippet").getString("title"));
                            interaction.setParameter(results.getJSONObject(0).getJSONObject("id").getString("videoId"));
                        } else {
                            msg = interaction.getRandomTemplate("fail").getTemplate();
                        }
                    } else {
                        StringBuilder resultMessage = new StringBuilder();
                        for (int i = 0; i < results.length(); i++) {
                            resultMessage.append("\n").append(i + 1).append(" : ").append(results.getJSONObject(i).getJSONObject("snippet").getString("title"));
                        }
                        resultArrays.put(event.getAuthor().getStringID(), results);
                        Interaction selectInteraction = new Interaction("selectOption", new String[]{"novoicehijack", "parameter", "d4jselect", "d4jplay"}, new InteractionTemplate[]{new InteractionTemplate("You selected \"%title\", added to queue!", "normal", null), new InteractionTemplate("That's not one of the options!", "fail", null),  new InteractionTemplate("You must be in the same voice channel as me to control music.", "nohijack", null)}, 1, event.getAuthor().getStringID(), interaction.getVoiceVolume());
                        selectInteraction.getTagCache().add(this);
                        selectInteraction.setNoClearInteraction(true);
                        Karren.bot.getGuildManager().getInteractionProcessor(event.getGuild()).getInteractions().add(selectInteraction);
                        msg = msg.replace("%results", resultMessage.toString());
                    }
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                }
            }
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jsearch";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }

    public JSONArray getResultArray(String userID) {
        JSONArray resultArray = resultArrays.get(userID);
        resultArrays.remove(userID);
        return resultArray;
    }
}
