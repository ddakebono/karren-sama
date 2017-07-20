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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
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
import java.util.EnumSet;
import java.util.Optional;


public class Battlegrounds extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        SSLContext sc;
        if(!interaction.hasParameter())
            return interaction.getRandomTemplate("noparam").getTemplate();
        String parameter = interaction.getParameter();
        String[] params = parameter.split(",");
        if(params.length>1) {
            JsonParser json = new JsonParser();
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, KarrenUtil.trustAllCertificates, new SecureRandom());
                HttpsURLConnection profileRequest = (HttpsURLConnection) new URL("https://pubgtracker.com/api/profile/pc/" + params[0].trim()).openConnection();
                profileRequest.setSSLSocketFactory(sc.getSocketFactory());
                profileRequest.setRequestProperty("TRN-Api-Key", Karren.conf.getTrackerNetworkAPIKey());
                profileRequest.connect();
                JsonReader jsonReader = new JsonReader(new InputStreamReader((InputStream)profileRequest.getContent()));
                jsonReader.setLenient(true);
                JsonObject profileData = json.parse(jsonReader).getAsJsonObject();
                JsonObject selectedSeason = null;
                for (JsonElement jsonElement : profileData.get("Stats").getAsJsonArray()) {
                    if (jsonElement.getAsJsonObject().get("Region").getAsString().equalsIgnoreCase("na") && jsonElement.getAsJsonObject().get("Season").getAsString().equalsIgnoreCase(profileData.get("defaultSeason").getAsString()) && jsonElement.getAsJsonObject().get("Match").getAsString().equalsIgnoreCase(params[1].trim())) {
                        selectedSeason = jsonElement.getAsJsonObject();
                        break;
                    }
                }
                ArrayList<JsonObject> statsArrayList = new ArrayList<>();
                if (selectedSeason != null)
                    for (JsonElement jsonObject : selectedSeason.get("Stats").getAsJsonArray())
                        statsArrayList.add(jsonObject.getAsJsonObject());
                msg = msg.replace("%username", profileData.get("PlayerName").getAsString());
                msg = msg.replace("%kills", getValueFromJson("Kills", statsArrayList));
                msg = msg.replace("%rating", getValueFromJson("Rating", statsArrayList));
                msg = msg.replace("%roundsPlayed", getValueFromJson("RoundsPlayed", statsArrayList));
                msg = msg.replace("%kdr", getValueFromJson("KillDeathRatio", statsArrayList));
                msg = msg.replace("%top10s", getValueFromJson("Top10s", statsArrayList));
                msg = msg.replace("%wins", getValueFromJson("Wins", statsArrayList));
                msg = msg.replace("%losses", getValueFromJson("Losses", statsArrayList));
            } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
                return interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            msg = interaction.getRandomTemplate("noparam").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "battlegrounds";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        EnumSet<Permissions> requiredPerms = EnumSet.of(Permissions.SEND_MESSAGES);
        return requiredPerms;
    }

    private String getValueFromJson(String target, ArrayList<JsonObject> statsArrayList){
        Optional<JsonObject> object = statsArrayList.stream().filter(x -> x.get("field").getAsString().equalsIgnoreCase(target)).findFirst();
        return object.map(jsonObject -> jsonObject.get("value").getAsString()).orElse("0");
    }
}
