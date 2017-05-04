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
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class OverwatchUAPIProfile implements Tag {

    final TrustManager[] trustAllCertificates = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null; // Not relevant.
                }
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }
                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }
            }
    };

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        SSLContext sc;
        String parameter = interaction.getParameter();
        parameter = parameter.replace("#", "-");
        JsonParser gson = new JsonParser();
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            //https://owapi.net/api/v3/u/DDAkebono-1984/blob
            HttpsURLConnection profileRequest = (HttpsURLConnection)new URL("https://owapi.net/api/v3/u/" + parameter + "/blob").openConnection();
            profileRequest.setSSLSocketFactory(sc.getSocketFactory());
            profileRequest.connect();
            JsonObject profile = gson.parse(new InputStreamReader((InputStream)profileRequest.getContent())).getAsJsonObject().getAsJsonObject("us");
            if(profile==null)
                throw new IOException();
            int wins = 0;
            JsonObject quickplay = profile.getAsJsonObject("stats").getAsJsonObject("quickplay");
            JsonObject competitive = profile.getAsJsonObject("stats").getAsJsonObject("competitive");
            JsonObject playtime = profile.getAsJsonObject("heroes").getAsJsonObject("playtime");
            msg = msg.replace("%username", parameter);
            msg = msg.replace("%level", Integer.toString(quickplay.getAsJsonObject("overall_stats").get("level").getAsInt() + (quickplay.getAsJsonObject("overall_stats").get("prestige").getAsInt()*100)));
            if(quickplay.getAsJsonObject("overall_stats").get("wins")!=null)
                wins+=quickplay.getAsJsonObject("overall_stats").get("wins").getAsInt();
            if(competitive.getAsJsonObject("overall_stats").get("wins")!=null)
                wins+=competitive.getAsJsonObject("overall_stats").get("wins").getAsInt();
            msg = msg.replace("%gameswon", String.valueOf(wins));
            if(quickplay.has("played") && competitive.has("played"))
                msg = msg.replace("%winrate", String.valueOf((wins / (quickplay.getAsJsonObject("overall_stats").get("games").getAsFloat() + competitive.getAsJsonObject("overall_stats").get("games").getAsFloat()))*100));
            else
                msg = msg.replace("%winrate", "Unknown");
            //msg = msg.replace("%playtime-quick", playtime.get("quick").getAsString());
            //msg = msg.replace("%playtime-comp", playtime.get("competitive").getAsString());
            /*if(!competitiveInfo.get("rank").isJsonNull())
                msg = msg.replace("%rank", competitiveInfo.get("rank").getAsString());
            else
                msg = msg.replace("%rank", "0");*/
        } catch (IOException e) {
            msg = interaction.getRandomTemplatesFail();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return msg;
    }

}
