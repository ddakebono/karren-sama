/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;

public class Interactions {
    private ArrayList<String> activator = new ArrayList<>();
    private String identifier = "";
    private String[] tags;
    private String responseTemplate = "";
    private int confidenceAmount;
    public Interactions(String identifier, String[] tags, String responseTemplate, String[] activators, int confidenceAmount){
        this.identifier = identifier;
        this.tags = tags;
        this.responseTemplate = responseTemplate;
        this.confidenceAmount = confidenceAmount;
        Collections.addAll(this.activator, activators);
    }
    public void addActivator(String activator){
        this.activator.add(activator.trim().toLowerCase());
    }
    /*
    handleMessage checks which interaction type the message is and runs the respective functions.
     */
    public String handleMessage(MessageReceivedEvent event){
        String result = "";
        int confidence = 0;
        if(!event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix())) {
            String[] tokenizedMessage = event.getMessage().getContent().split("\\s+");
            for (String check : activator) {
                for (String check2 : tokenizedMessage) {
                    if (check2.trim().toLowerCase().matches(check + "\\W?")) {
                        confidence++;
                    }
                }
            }
        }
        if(confidence>=confidenceAmount)
            result = responseTemplate;
        return result;
    }
    public String getIdentifier(){return identifier;}
    public String[] getTags(){return tags;}
    public String getTagsToString(){
        String result = "";
        for (String tag : tags) {
            result += tag;
        }
        return result;
    }
    public String getResponseTemplate(){return responseTemplate;}
    public String[] getActivator(){
        String[] result = new String[activator.size()];
        for(int i=0; i<result.length; i++){
            result[i] = activator.get(i);
        }
        return result;
    }
    public String getActivatorsToString(){
        String result = "";
        for (String anActivator : activator) {
            result += anActivator + " ";
        }
        return result;
    }
}
