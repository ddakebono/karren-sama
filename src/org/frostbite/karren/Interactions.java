/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;

/**
 * Created by frostbite on 3/6/14.
 */
public class Interactions {
    private ArrayList<String> activator = new ArrayList<String>();
    private String identifier = "";
    private String[] tags;
    private String responseTemplate = "";
    private int confidenceAmount;
    public Interactions(String identifier, String[] tags, String responseTemplate, String[] activators, int confidenceAmount){
        this.identifier = identifier;
        this.tags = tags;
        this.responseTemplate = responseTemplate;
        this.confidenceAmount = confidenceAmount;
        for(int i=0; i<activators.length; i++)
            this.activator.add(activators[i]);
    }
    public void addActivator(String activator){
        this.activator.add(activator.trim().toLowerCase());
    }
    /*
    handleMessage checks which interaction type the message is and runs the respective functions.
     */
    public String handleMessage(MessageEvent event){
        String result = "";
        int confidence = 0;
        String[] tokenizedMessage = event.getMessage().split("\\s+");
        for(String check : activator){
            for(String check2 : tokenizedMessage) {
                if (check2.trim().toLowerCase().matches(check + "\\W?")) {
                    confidence++;
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
        for(int i=0; i<tags.length; i++){
            result += tags[i];
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
        for(int i=0; i<activator.size(); i++){
            result += activator.get(i) + " ";
        }
        return result;
    }
}
