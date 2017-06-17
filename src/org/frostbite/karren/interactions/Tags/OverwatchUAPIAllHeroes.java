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
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class OverwatchUAPIAllHeroes implements Tag {
    final String[] heroList = {"Reinhardt", "Tracer", "Zenyatta", "Junkrat", "Mccree", "Winston", "Orisa", "Hanzo", "Pharah", "Roadhog", "Zarya", "Torbjorn", "Mercy", "Soldier76", "Ana", "Widowmaker", "Genji", "Reaper", "Mei", "Bastion", "Symmetra", "Dva", "Sombra", "Lucio"};
    final TrustManager[] trustAllCertificates = new TrustManager[]{
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
            HttpsURLConnection heroRequest = (HttpsURLConnection) new URL("https://owapi.net/api/v3/u/" + parameter + "/heroes").openConnection();
            heroRequest.setSSLSocketFactory(sc.getSocketFactory());
            heroRequest.connect();
            JsonObject heroPlaytime = gson.parse(new InputStreamReader((InputStream)heroRequest.getContent())).getAsJsonObject().getAsJsonObject("us").getAsJsonObject("heroes").getAsJsonObject("playtime");
            TreeMap<String, Double> heroes = new TreeMap<>();
            for(String hero : heroList)
                heroes.put(hero, heroPlaytime.getAsJsonObject("competitive").get(hero.toLowerCase()).getAsDouble()+heroPlaytime.getAsJsonObject("quickplay").get(hero.toLowerCase()).getAsDouble());
            LinkedHashMap heroesSort = heroes.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            msg = msg.replace("%username", parameter);
            for(int i=0; i<3; i++) {
                msg = msg.replace("%hero" + i, (heroesSort.keySet().toArray())[(heroList.length-i)-1].toString());
                msg = msg.replace("%timeplayed" + i, String.valueOf(heroes.get((heroesSort.keySet().toArray())[(heroList.length-i)-1])));
            }
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            msg = interaction.getRandomTemplatesPermError();
        }
        return msg;
    }
}
