/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions;

import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;
import java.util.regex.Pattern;

public class Interaction {
    private String[] triggers;
    private String[] tags;
    private String[] templates;
    private String[] voiceFiles;
    private boolean enabled = true;
    private String helptext;
    private String identifier;
    private int confidence = 0;
    private String[] templatesFail;
    private String[] templatesPermError;
    private String permissionLevel;
    private String channel;
    private float voiceVolume = 0.1f;
    private String parameter;
    private boolean specialInteraction = false;
    private String[] childInteractions;
    private boolean isPermBad = false;
    private int usageCount = -1;
    private ArrayList<String> allowedUsers = new ArrayList<>();
    private boolean stopProcessing = false;

    public Interaction(String identifier, String[] tags, String templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, new String[]{templates}, triggers, confidence, enabled, helptext);
    }

    public Interaction(String identifier, String[] tags, String[] templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, templates, triggers, confidence, enabled, helptext, null, null, "", "", null, 0.0f, false, null);
    }

    public Interaction(String identifier, String[] tags, String[] templates, String[] templatesFail, int usageCount, String userID, float voiceVolume){
        this(identifier, tags, templates, null, 0, true, "", templatesFail, null, "", "", null, voiceVolume, false, null);
        this.usageCount = usageCount;
        this.allowedUsers.add(userID);
    }

    public Interaction(String identifier, String[] tags, String[] templates, String[] triggers, int confidence, boolean enabled, String helptext, String[] templatesFail, String[] templatesPermError, String permissionLevel, String channel, String[] voiceFiles, float voiceVolume, boolean specialInteraction, String[] childInteractions){
        this.specialInteraction = specialInteraction;
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
        this.channel = channel;
        this.voiceFiles = voiceFiles;
        this.voiceVolume = voiceVolume;
        this.childInteractions = childInteractions;
    }
    /*
    handleMessage checks which interaction type the message is and runs the respective functions.
     */
    String handleMessage(MessageReceivedEvent event){
        String result = null;
        int confidence = 0;
        if(enabled) {
            if (isAllowedUser(event.getAuthor())) {
                if (!event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix()) && !Arrays.asList(tags).contains("prefixed") && (!Arrays.asList(tags).contains("bot") || (event.getMessage().getContent().toLowerCase().contains(Karren.bot.getClient().getOurUser().getNicknameForGuild(event.getGuild())) || event.getMessage().getContent().toLowerCase().contains(Karren.bot.getClient().getOurUser().getName()))))
                    confidence = getConfidence(event.getMessage().getContent());
                if (event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix()) && Arrays.asList(tags).contains("prefixed")) {
                    //Get only word follow prefix
                    Pattern prefixedPattern = Pattern.compile("\\s+");
                    String[] regex = prefixedPattern.split(event.getMessage().getContent().replace(Karren.conf.getCommandPrefix(), ""));
                    if (regex.length > 0) {
                        confidence = getConfidence(regex[0]);
                    }
                }
                if (confidence >= this.confidence)
                    result = getRandomTemplate(templates);
                if (result != null && permissionLevel != null && permissionLevel.length() > 0 && !KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getGuild(), permissionLevel)) {
                    result = getRandomTemplatesPermError();
                    isPermBad = true;
                }
            }
        }
        return result;
    }

    private boolean isAllowedUser(IUser user) {
        return allowedUsers == null || allowedUsers.isEmpty() || allowedUsers.contains(user.getID());
    }

    private int getConfidence(String message){
        int confidence = 0;
        String[] tokenizedMessage = message.split("\\s+");
        if(triggers!=null) {
            for (String check : triggers) {
                for (String check2 : tokenizedMessage) {
                    if (check2.trim().toLowerCase().matches(check + "\\W?")) {
                        confidence++;
                    }
                }
            }
        }
        return confidence;
    }

    private String getRandomTemplate(String[] templates) {
        Random rng = new Random();
        return templates[rng.nextInt(templates.length)];
    }

    public String[] getTags(){return tags;}
    public String getTagsToString(){
        StringBuilder result = new StringBuilder();
        for (String tag : tags) {
            result.append(tag).append(" ");
        }
        return result.toString();
    }
    public String[] getTemplates(){return templates;}
    public String[] getTriggers(){
        return triggers;
    }
    public String getActivatorsToString(){
        StringBuilder result = new StringBuilder();
        for (String anActivator : triggers) {
            result.append(anActivator).append(" ");
        }
        return result.toString();
    }
    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }
    public String getHelptext(){return helptext;}
    public String getIdentifier(){return identifier;}

    public String getChannel() {
        return channel;
    }

    public String[] getTemplatesFail() {
        return templatesFail;
    }

    public String[] getTemplatesPermError() {
        return templatesPermError;
    }

    public String getRandomTemplatesFail() {
        if(templatesFail!=null) {
            return getRandomTemplate(templatesFail);
        } else {
            Karren.log.error("Interaction error, " + identifier + " uses failure templates but doesn't supply any!");
            return "ERROR";
        }
    }

    public String getRandomTemplates() {
        if(templates!=null) {
            return getRandomTemplate(templates);
        } else {
            Karren.log.error("Interaction error, " + identifier + " uses templates but doesn't supply any!");
            return "ERROR";
        }
    }

    public String getRandomTemplatesPermError() {
        if(templatesPermError!=null) {
            return getRandomTemplate(templatesPermError);
        } else {
            Karren.log.error("Interaction error, " + identifier + " uses permission error templates but doesn't supply any!");
            return "PERMISSION ERROR";
        }
    }

    public String getRandomVoiceFile(){
        if(voiceFiles!=null){
            return "conf/" + getRandomTemplate(voiceFiles);
        } else {
            return null;
        }
    }

    public boolean isChild(){
        for(String tag : tags){
            if(tag.equalsIgnoreCase("child"))
                return true;
        }
        return false;
    }

    public float getVoiceVolume() {
        return voiceVolume;
    }

    public String getPermissionLevel(){return permissionLevel;}

    public boolean isSpecialInteraction() {
        return specialInteraction;
    }

    public String[] getChildInteractions() {
        return childInteractions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getParameter() {
        if(this.parameter!=null && this.parameter.length()>0) {
            String parameterLast = parameter;
            if(this.getTagsToString().toLowerCase().contains("parameter"))
                this.parameter = "";
            return parameterLast;
        } else {
            return null;
        }
    }

    public boolean hasParameter(){
        return this.parameter!=null && this.parameter.length()>0;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public boolean isPermBad() {
        return isPermBad;
    }

    public int getConfidence() {
        return confidence;
    }

    public void addUsageCount(){
        usageCount++;
    }

    public boolean interactionUsed() {
        this.usageCount--;
        return this.usageCount == 0;
    }

    public boolean isStopProcessing() {
        return stopProcessing;
    }

    public void stopProcessing() {
        this.stopProcessing = true;
    }
}
