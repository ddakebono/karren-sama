/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.Random;

public class Interactions {
    private String[] triggers;
    private String[] tags;
    private String[] templates;
    private boolean enabled;
    private String helptext;
    private String identifier;
    private int confidence;

    public Interactions(String identifier, String[] tags, String templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, new String[]{templates}, triggers, confidence, enabled, helptext);
    }

    public Interactions(String identifier, String[] tags, String[] templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this.identifier = identifier;
        this.tags = tags;
        this.templates = templates;
        this.confidence = confidence;
        this.triggers = triggers;
        this.enabled = enabled;
        this.helptext = helptext;
    }
    /*
    handleMessage checks which interaction type the message is and runs the respective functions.
     */
    public String handleMessage(MessageReceivedEvent event){
        Random rng = new Random();
        String result = "";
        if(enabled) {
            int confidence = 0;
            if (!event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix())) {
                String[] tokenizedMessage = event.getMessage().getContent().split("\\s+");
                for (String check : triggers) {
                    for (String check2 : tokenizedMessage) {
                        if (check2.trim().toLowerCase().matches(check + "\\W?")) {
                            confidence++;
                        }
                    }
                }
            }
            if (confidence >= this.confidence)
                result = templates[rng.nextInt(templates.length)];
        }
        return result;
    }
    public String[] getTags(){return tags;}
    public String getTagsToString(){
        String result = "";
        for (String tag : tags) {
            result += tag + " ";
        }
        return result;
    }
    public String[] getTemplates(){return templates;}
    public String[] getTriggers(){
        return triggers;
    }
    public String getActivatorsToString(){
        String result = "";
        for (String anActivator : triggers) {
            result += anActivator + " ";
        }
        return result;
    }
    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }
    public String getHelptext(){return helptext;}
    public String getIdentifier(){return identifier;}
}
