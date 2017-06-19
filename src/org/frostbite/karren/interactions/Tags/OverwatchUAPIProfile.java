/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.text.DecimalFormat;

public class OverwatchUAPIProfile implements Tag {
    DecimalFormat df =new DecimalFormat("#.##");

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        SSLContext sc;
        String parameter = interaction.getParameter();
        parameter = parameter.replace("#", "-");
        JsonParser gson = new JsonParser();
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, KarrenUtil.trustAllCertificates, new SecureRandom());
            HttpsURLConnection profileRequest = (HttpsURLConnection)new URL("https://owapi.net/api/v3/u/" + parameter + "/blob").openConnection();
            profileRequest.setSSLSocketFactory(sc.getSocketFactory());
            profileRequest.connect();
            JsonObject profile = gson.parse(new InputStreamReader((InputStream)profileRequest.getContent())).getAsJsonObject().getAsJsonObject("us");
            if(profile==null)
                throw new IOException();
            JsonObject quickplay = profile.getAsJsonObject("stats").getAsJsonObject("quickplay");
            JsonObject playtime = profile.getAsJsonObject("heroes").getAsJsonObject("playtime");
            msg = msg.replace("%username", parameter);
            msg = msg.replace("%level", Integer.toString(quickplay.getAsJsonObject("overall_stats").get("level").getAsInt() + (quickplay.getAsJsonObject("overall_stats").get("prestige").getAsInt()*100)));
            msg = msg.replace("%winrate", quickplay.getAsJsonObject("overall_stats").get("win_rate").getAsString());
            msg = msg.replace("%gameswon", String.valueOf(quickplay.getAsJsonObject("overall_stats").get("games").getAsInt()-quickplay.getAsJsonObject("overall_stats").get("losses").getAsInt()));
            double timeRanked = 0.0;
            double timeQuick = 0.0;
            for(String hero : KarrenUtil.heroList)
                timeRanked += playtime.getAsJsonObject("competitive").get(hero.toLowerCase()).getAsDouble();
            for(String hero : KarrenUtil.heroList)
                timeQuick += playtime.getAsJsonObject("quickplay").get(hero.toLowerCase()).getAsDouble();
            msg = msg.replace("%playtime-quick", df.format(timeQuick));
            msg = msg.replace("%playtime-comp", df.format(timeRanked));
            msg = msg.replace("%rank", quickplay.getAsJsonObject("overall_stats").get("comprank").getAsString());
        } catch (IOException e) {
            msg = interaction.getRandomTemplatesFail();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return msg;
    }

}
