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

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDL implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        ProcessBuilder youtubeDL = new ProcessBuilder();
        String param = interaction.getParameter();
        String id = getIDFromLink(param);
        if(id!=null) {
            youtubeDL.command(Karren.conf.getYoutubeDLBinary(), "-o", "cache/%(id)s.%(ext)s", "--extract-audio", "--audio-format", "mp3", "--audio-quality", "0", param);
            try {
                youtubeDL.start().waitFor();
                interaction.setYoutubeCacheFile(id);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            msg = interaction.getRandomTemplatesFail();
        }
        return msg;
    }

    private String getIDFromLink(String link){
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        if(link!=null) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(link);

            if (matcher.find()) {
                return matcher.group();
            }
        }

        return null;
    }
}
