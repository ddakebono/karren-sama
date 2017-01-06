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
import org.apache.commons.lang3.StringUtils;
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

public class OverwatchUAPIHero implements Tag {

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
        String[] params = parameter.split(",");
        JsonParser gson = new JsonParser();
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            if(params.length>1) {
                HttpsURLConnection heroRequest = (HttpsURLConnection) new URL("https://api.lootbox.eu/pc/us/" + params[0] + "/quickplay/hero/" + StringUtils.capitalize(params[1]) + "/").openConnection();
                heroRequest.setSSLSocketFactory(sc.getSocketFactory());
                heroRequest.connect();
                JsonObject hero = gson.parse(new InputStreamReader((InputStream)heroRequest.getContent())).getAsJsonObject().getAsJsonObject(StringUtils.capitalize(params[1]));
                msg = msg.replace("%username", params[0]);
                msg = msg.replace("%timeplayed", hero.get("TimePlayed").getAsString());
                msg = msg.replace("%hero", params[1]);
                msg = msg.replace("%damage", hero.get("DamageDone").getAsString());
                msg = msg.replace("%totalhealing", hero.get("HealingDone").getAsString());
                msg = msg.replace("%kills", hero.get("Eliminations").getAsString());
                msg = msg.replace("%deaths", hero.get("Deaths").getAsString());
            } else {
                msg = interaction.getRandomTemplatesFail();
            }
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            msg = interaction.getRandomTemplatesPermError();
        }
        return msg;
    }
}
