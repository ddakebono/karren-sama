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
import org.frostbite.karren.Karren;
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

public class OsuGetUser implements Tag {

    private boolean hasKey = true;

    public OsuGetUser(){
        if(Karren.conf.getOsuAPIKey().length()==0){
            Karren.log.info("osu! API key not supplied, osu! interaction tag will not be usable.");
            hasKey = false;
        }
    }

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
        JsonParser gson = new JsonParser();
        if (hasKey){
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCertificates, new SecureRandom());
                if (parameter != null) {
                    HttpsURLConnection profileRequest = (HttpsURLConnection) new URL("https://osu.ppy.sh/api/get_user?k=" + Karren.conf.getOsuAPIKey() + "&u=" + parameter.trim() + "&type=string").openConnection();
                    profileRequest.setSSLSocketFactory(sc.getSocketFactory());
                    profileRequest.connect();
                    JsonObject osuProfile = gson.parse(new InputStreamReader((InputStream) profileRequest.getContent())).getAsJsonArray().get(0).getAsJsonObject();
                    msg = msg.replace("%username", osuProfile.get("username").getAsString());
                    msg = msg.replace("%rank", osuProfile.get("pp_rank").getAsString());
                    msg = msg.replace("%score", osuProfile.get("total_score").getAsString());
                    msg = msg.replace("%rawpp", osuProfile.get("pp_raw").getAsString());
                    msg = msg.replace("%level", osuProfile.get("level").getAsString());
                    msg = msg.replace("%countryrank", osuProfile.get("pp_country_rank").getAsString());
                    msg = msg.replace("%country", osuProfile.get("country").getAsString());
                    msg = msg.replace("%playcount", osuProfile.get("playcount").getAsString());
                    msg = msg.replace("%countss", osuProfile.get("count_rank_ss").getAsString());
                    msg = msg.replace("%counts", osuProfile.get("count_rank_s").getAsString());
                    msg = msg.replace("%counta", osuProfile.get("count_rank_a").getAsString());
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                }
            } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
                msg = interaction.getRandomTemplate("fail").getTemplate();
                e.printStackTrace();
            }
        } else {
            msg = "Missing osu! API key, please inform the bot operator!";
        }

        return msg;
    }
}
