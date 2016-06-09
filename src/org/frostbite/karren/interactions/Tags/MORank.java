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

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.frostbite.karren.interactions.Tags.TagObjects.HeroSearchResults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class MORank implements Tag {

    HashMap<String, String> heroes = new HashMap<>();

    public MORank(){
        heroes.put("bastion", "15");
        heroes.put("dva", "22");
        heroes.put("genji", "21");
        heroes.put("hanzo", "16");
        heroes.put("junkrat", "2");
        heroes.put("lucio", "3");
        heroes.put("mccree", "6");
        heroes.put("mei", "20");
        heroes.put("mercy", "17");
        heroes.put("pharah", "11");
        heroes.put("reaper", "8");
        heroes.put("reinhardt", "12");
        heroes.put("roadhog", "1");
        heroes.put("soldier76", "4");
        heroes.put("symmetra", "13");
        heroes.put("torbjorn", "14");
        heroes.put("tracer", "7");
        heroes.put("widowmaker", "9");
        heroes.put("winston", "10");
        heroes.put("zarya", "5");
        heroes.put("zenyatta", "18");
    }

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String parameter = interaction.getParameter();
        interaction.setParameter("");
        String[] args = parameter.split(",");
        Gson gson = new Gson();
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            msg = interaction.getRandomTemplatesFail();
        } catch (ArrayIndexOutOfBoundsException e){
            msg = interaction.getRandomTemplatesPermError();
        } catch (IllegalArgumentException e){
            msg = "No results were returned. (Unranked)";
        }
        return msg;
    }

}
