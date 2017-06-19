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
import java.util.Arrays;

public class OverwatchUAPIHero implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
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
                JsonObject statsComp = gson.parse(new InputStreamReader((InputStream)heroRequest.getContent())).getAsJsonObject().getAsJsonObject("stats").getAsJsonObject("competitive").getAsJsonObject(params[1].toLowerCase());
                JsonObject statsQP = gson.parse(new InputStreamReader((InputStream)heroRequest.getContent())).getAsJsonObject().getAsJsonObject("stats").getAsJsonObject("quickplay").getAsJsonObject(params[1].toLowerCase());
                String heroDataBlob = "";

            } else {
                msg = interaction.getRandomTemplatesFail();
            }
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            msg = interaction.getRandomTemplatesPermError();
        }
        return msg;
    }
}
