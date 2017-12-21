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
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.pircbotx.hooks.events.MessageEvent;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class OverwatchUAPIHero extends Tag {

    DecimalFormat df =new DecimalFormat("#.##");

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageEvent event) {
        SSLContext sc;
        String parameter = interaction.getParameter();
        parameter = parameter.replace("#", "-");
        String[] params = parameter.split(",");
        JsonParser gson = new JsonParser();
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, KarrenUtil.trustAllCertificates, new SecureRandom());
            if(params.length>1 && Arrays.stream(KarrenUtil.heroList).anyMatch(x -> x.equalsIgnoreCase(params[1]))) {
                HttpsURLConnection heroRequest = (HttpsURLConnection) new URL("https://owapi.net/api/v3/u/" + params[0] + "/heroes").openConnection();
                heroRequest.setSSLSocketFactory(sc.getSocketFactory());
                heroRequest.connect();
                JsonObject stats = gson.parse(new InputStreamReader((InputStream)heroRequest.getContent())).getAsJsonObject().getAsJsonObject("us").getAsJsonObject("heroes").getAsJsonObject("stats").getAsJsonObject("quickplay").getAsJsonObject(params[1].toLowerCase());
                if(stats!=null) {
                    Set<Map.Entry<String, JsonElement>> entries = stats.getAsJsonObject("average_stats").entrySet();
                    Set<Map.Entry<String, JsonElement>> entriesHero = stats.getAsJsonObject("hero_stats").entrySet();
                    Set<Map.Entry<String, JsonElement>> entriesGeneral = stats.getAsJsonObject("general_stats").entrySet();
                    msg = interaction.replaceMsg(msg,"%username", params[0]);
                    msg = interaction.replaceMsg(msg,"%hero", Arrays.stream(KarrenUtil.heroList).filter(x -> x.equalsIgnoreCase(params[1])).findFirst().get());
                    StringBuilder heroDataBlob = new StringBuilder("**Hero Stat Block**");
                    for (Map.Entry<String, JsonElement> stat : entries) {
                        if (KarrenUtil.getNormalizedName(stat.getKey()) != null) {
                            heroDataBlob.append("\n").append(KarrenUtil.getNormalizedName(stat.getKey())).append(": ").append(df.format(stat.getValue().getAsFloat()));
                        }
                    }
                    for (Map.Entry<String, JsonElement> stat : entriesHero) {
                        if (KarrenUtil.getNormalizedName(stat.getKey()) != null) {
                            heroDataBlob.append("\n").append(KarrenUtil.getNormalizedName(stat.getKey())).append(": ").append(df.format(stat.getValue().getAsFloat()));
                        }
                    }
                    for (Map.Entry<String, JsonElement> stat : entriesGeneral) {
                        if (KarrenUtil.getNormalizedName(stat.getKey()) != null) {
                            heroDataBlob.append("\n").append(KarrenUtil.getNormalizedName(stat.getKey())).append(": ").append(df.format(stat.getValue().getAsFloat()));
                        }
                    }
                    msg = interaction.replaceMsg(msg,"%blob", heroDataBlob.toString());
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                }
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            msg = interaction.getRandomTemplate("permission").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "overwatchhero";
    }

}
