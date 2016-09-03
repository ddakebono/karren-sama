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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class OverwatchUAPI implements Tag {

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
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String parameter = interaction.getParameter();
        JsonParser gson = new JsonParser();
        try {
            HttpsURLConnection profileRequest = (HttpsURLConnection)new URL("https://api.lootbox.eu/pc/us/" + parameter + "/profile").openConnection();
            if(sc != null)
                profileRequest.setSSLSocketFactory(sc.getSocketFactory());
            profileRequest.connect();
            JsonObject profile = gson.parse(new InputStreamReader((InputStream)profileRequest.getContent())).getAsJsonObject().getAsJsonObject("data");
            if(profile==null)
                throw new IOException();
            JsonObject quickplay = profile.getAsJsonObject("games").getAsJsonObject("quick");
            JsonObject competitive = profile.getAsJsonObject("games").getAsJsonObject("competitive");
            JsonObject playtime = profile.getAsJsonObject("playtime");
            JsonObject competitiveInfo = profile.getAsJsonObject("competitive");
            msg = msg.replace("%username", profile.get("username").getAsString());
            msg = msg.replace("%level", profile.get("level").getAsString());
            msg = msg.replace("%gameswon", String.valueOf(quickplay.get("wins").getAsInt() + competitive.get("wins").getAsInt()));
            if(quickplay.has("played") && competitive.has("played"))
                msg = msg.replace("%winrate", String.valueOf(((quickplay.get("wins").getAsFloat() + competitive.get("wins").getAsFloat()) / (quickplay.get("played").getAsFloat() + competitive.get("played").getAsFloat()))*100));
            else
                msg = msg.replace("%winrate", "Unknown");
            msg = msg.replace("%playtime-quick", playtime.get("quick").getAsString());
            msg = msg.replace("%playtime-comp", playtime.get("competitive").getAsString());
            if(!competitiveInfo.get("rank").isJsonNull())
                msg = msg.replace("%rank", competitiveInfo.get("rank").getAsString());
            else
                msg = msg.replace("%rank", "0");
        } catch (IOException e) {
            msg = interaction.getRandomTemplatesFail();
        }
        /*try {
            HeroSearchResults results = gson.fromJson(IOUtils.toString(new URL("http://masteroverwatch.com/leaderboards/pc/us/hero/" + heroes.getOrDefault(args[1].trim().toLowerCase(), "") + "/role/overall/score/search?name=" + args[0].trim()).openStream()), HeroSearchResults.class);
            Document result = Jsoup.parse(StringEscapeUtils.unescapeJava(results.getSingleEntry()));
            msg = msg.replace("%username", args[0].trim());
            msg = msg.replace("%rank", result.getElementsByClass("table-icon-rank").get(0).text());
            msg = msg.replace("%totalscore", result.select("div.table-stats-score > strong").text());
            msg = msg.replace("%winrate", result.getElementsByClass("table-stats-winrate").get(0).text());
            msg = msg.replace("%kdr", result.select("div.table-stats-kda > strong").text());
            msg = msg.replace("%timeplayed", result.getElementsByClass("table-stats-time").get(0).text());
            msg = msg.replace("%multis", result.getElementsByClass("table-stats-standard").get(0).text());
            msg = msg.replace("%medals", result.getElementsByClass("table-stats-standard").get(1).text());
            msg = msg.replace("%hero", args[1].trim());
        } catch (FileNotFoundException | NullPointerException e){
            msg = interaction.getRandomTemplatesFail();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            msg = interaction.getRandomTemplatesPermError();
        } catch (IllegalArgumentException e){
            msg = "No results were returned. (Unranked)";
        }*/
        return msg;
    }

}
