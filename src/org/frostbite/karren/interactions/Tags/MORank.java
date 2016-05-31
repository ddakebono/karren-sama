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
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.frostbite.karren.interactions.Tags.TagObjects.HeroSearchResults;
import org.frostbite.karren.interactions.Tags.TagObjects.MasterOverwatchHeroes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.io.IOException;
import java.net.URL;

public class MORank implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String parameter = interaction.getParameter();
        interaction.setParameter("");
        String[] args = parameter.split(",");
        Gson gson = new Gson();
        try {
            HeroSearchResults results = gson.fromJson(IOUtils.toString(new URL("https://masteroverwatch.com/leaderboards/pc/us/hero/ " + getHeroNumber(args[1]).getHeroNumber() + "/score/search?name=" + args[0].trim()).openStream()), HeroSearchResults.class);
            Document result = Jsoup.parse(StringEscapeUtils.unescapeJava(results.getSingleEntry()));
            msg = msg.replace("%rank", result.getElementsByClass("table-icon-rank").get(0).text());
            msg = msg.replace("%hero", args[1].trim());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            msg = interaction.getRandomTemplatesFail();
        } catch (ArrayIndexOutOfBoundsException e){
            msg = interaction.getRandomTemplatesPermError();
        }
        return msg;
    }

    private MasterOverwatchHeroes getHeroNumber(String hero){
        switch(hero.trim().toLowerCase()){
            case "bastion":
                return MasterOverwatchHeroes.BASTION;
            case "genji":
                return MasterOverwatchHeroes.GENJI;
            case "hanzo":
                return MasterOverwatchHeroes.HANZO;
            case "junkrat":
                return MasterOverwatchHeroes.JUNKRAT;
            case "lucio":
                return MasterOverwatchHeroes.LUCIO;
            case "mccree":
                return MasterOverwatchHeroes.MCCREE;
            case "mei":
                return MasterOverwatchHeroes.MEI;
            case "mercy":
                return MasterOverwatchHeroes.MERCY;
            case "pharah":
                return MasterOverwatchHeroes.PHARAH;
            case "reaper":
                return MasterOverwatchHeroes.REAPER;
            case "reinhardt":
                return MasterOverwatchHeroes.REINHARDT;
            case "roadhog":
                return MasterOverwatchHeroes.ROADHOG;
            case "soldier76":
                return MasterOverwatchHeroes.SOLDIER76;
            case "symmetra":
                return MasterOverwatchHeroes.SYMMETRA;
            case "torbjorn":
                return MasterOverwatchHeroes.TORBJORN;
            case "tracer":
                return MasterOverwatchHeroes.TRACER;
            case "widowmaker":
                return MasterOverwatchHeroes.WIDOWMAKER;
            case "winston":
                return MasterOverwatchHeroes.WINSTON;
            case "zarya":
                return MasterOverwatchHeroes.ZARYA;
            case "zenyatta":
                return MasterOverwatchHeroes.ZENYATTA;
        }
        return null;
    }

}
