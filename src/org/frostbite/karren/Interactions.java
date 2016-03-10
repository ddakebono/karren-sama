/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Random;

public class Interactions {
    private String[] triggers;
    private String[] tags;
    private String[] templates;
    private boolean enabled;
    private String helptext;
    private String identifier;
    private int confidence;
    private String[] templatesFail;
    private String[] templatesPermError;
    private String permissionLevel;


    public Interactions(String identifier, String[] tags, String templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, new String[]{templates}, triggers, confidence, enabled, helptext);
    }

    public Interactions(String identifier, String[] tags, String[] templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, templates, triggers, confidence, enabled, helptext, null, null, "");
    }

    public Interactions(String identifier, String[] tags, String[] templates, String[] triggers, int confidence, boolean enabled, String helptext, String[] templatesFail, String[] templatesPermError, String permissionLevel){
        this.identifier = identifier;
        this.tags = tags;
        this.templates = templates;
        this.confidence = confidence;
        this.triggers = triggers;
        this.enabled = enabled;
        this.helptext = helptext;
        this.templatesPermError = templatesPermError;
        this.templatesFail = templatesFail;
        this.permissionLevel = permissionLevel;
    }
    /*
    handleMessage checks which interaction type the message is and runs the respective functions.
     */
    public String handleMessage(MessageReceivedEvent event){
        String result = null;
        int confidence = 0;
        if(enabled) {
            if(!event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix()) && !Arrays.asList(tags).contains("prefixed"))
                confidence = getConfidence(event.getMessage().getContent());
            if(event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix()) && Arrays.asList(tags).contains("prefixed"))
                confidence = getConfidence(event.getMessage().getContent().replace(Karren.conf.getCommandPrefix(), ""));
            if (confidence >= this.confidence)
                result = getRandomTemplate(templates);
        }
        return result;
    }

    public int getConfidence(String message){
        int confidence = 0;
        String[] tokenizedMessage = message.split("\\s+");
        for (String check : triggers) {
            for (String check2 : tokenizedMessage) {
                if (check2.trim().toLowerCase().matches(check + "\\W?")) {
                    confidence++;
                }
            }
        }
        return confidence;
    }

    public String getRandomTemplate(String[] templates) {
        Random rng = new Random();
        return templates[rng.nextInt(templates.length)];
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

    public String[] getTemplatesFail() {
        return templatesFail;
    }

    public String[] getTemplatesPermError() {
        return templatesPermError;
    }

    public String getRandomTemplatesFail() {
        if(templatesFail!=null)
            return getRandomTemplate(templatesFail);
        else
            return "ERROR";
    }

    public String getRandomTemplatesPermError() {
        if(templatesPermError!=null)
            return getRandomTemplate(templatesPermError);
        else
            return "PERMISSION ERROR";
    }

    public String getPermissionLevel(){return permissionLevel;}
}
