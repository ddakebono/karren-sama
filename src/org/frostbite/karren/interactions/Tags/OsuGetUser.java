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
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.EnumSet;

public class OsuGetUser extends Tag {

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
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.withAuthorName("osu! Stats");
                    embed.withColor(Color.RED);
                    msg = msg.replace("%username", osuProfile.get("username").getAsString());
                    embed.withDescription(msg);
                    embed.appendField("Global Ranking", osuProfile.get("pp_rank").getAsString(), true);
                    embed.appendField("Country Ranking", osuProfile.get("pp_country_rank").getAsString(), true);
                    embed.appendField("Total Score", osuProfile.get("total_score").getAsString(), true);
                    embed.appendField("PP", osuProfile.get("pp_raw").getAsString(), true);
                    embed.appendField("Level", osuProfile.get("level").getAsString(), true);
                    embed.appendField("Country", osuProfile.get("country").getAsString(), true);
                    embed.appendField("Playcount", osuProfile.get("playcount").getAsString() + " plays", false);
                    embed.appendField("SS", osuProfile.get("count_rank_ss").getAsString(), true);
                    embed.appendField("S", osuProfile.get("count_rank_s").getAsString(), true);
                    embed.appendField("A", osuProfile.get("count_rank_a").getAsString(), true);
                    embed.withFooterText("Requested By: " + event.getAuthor().getName());
                    interaction.setEmbed(embed);
                    return null;
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

    @Override
    public String getTagName() {
        return "osugetuser";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
