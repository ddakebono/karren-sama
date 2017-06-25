/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Battlegrounds implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        SSLContext sc;
        if(!interaction.hasParameter())
            return interaction.getRandomTemplate("noparam").getTemplate();
        String parameter = interaction.getParameter();
        JsonParser json = new JsonParser();
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, KarrenUtil.trustAllCertificates, new SecureRandom());
            HttpsURLConnection profileRequest = (HttpsURLConnection)new URL("https://pubgtracker.com/api/profile/pc/" + parameter).openConnection();
            profileRequest.setSSLSocketFactory(sc.getSocketFactory());
            profileRequest.setRequestProperty("TRN-Api-Key", Karren.conf.getTrackerNetworkAPIKey());
            profileRequest.connect();
            JsonObject profileData = json.parse(new InputStreamReader((InputStream)profileRequest.getContent())).getAsJsonObject();
            JsonArray statsArray = profileData.get("Stats").getAsJsonArray().get(0).getAsJsonObject().get("Stats").getAsJsonArray();
            ArrayList<JsonObject> statsArrayList = new ArrayList<>();
            for(JsonElement jsonObject : statsArray)
                statsArrayList.add(jsonObject.getAsJsonObject());
            msg = msg.replace("%username", profileData.get("PlayerName").getAsString());
            msg = msg.replace("%kills", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("Kills")).findFirst().get().get("value").getAsString());
            msg = msg.replace("%rating", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("Rating")).findFirst().get().get("value").getAsString());
            msg = msg.replace("%roundsPlayed", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("RoundsPlayed")).findFirst().get().get("value").getAsString());
            msg = msg.replace("%kdr", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("KillDeathRatio")).findFirst().get().get("value").getAsString());
            msg = msg.replace("%top10s", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("Top10s")).findFirst().get().get("value").getAsString());
            msg = msg.replace("%wins", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("Wins")).findFirst().get().get("value").getAsString());
            msg = msg.replace("%losses", statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase("Losses")).findFirst().get().get("value").getAsString());
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            return interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }
}
