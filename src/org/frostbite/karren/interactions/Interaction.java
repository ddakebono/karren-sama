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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Interaction {
    @Expose private String[] triggers;
    @Expose private String[] tags;
    @Expose private InteractionTemplate[] templatesNew;
    private String[] templates;
    private String[] templatesFail;
    private String[] templatesPermError;
    @Expose private String[] voiceFiles;
    @Expose private boolean enabled = true;
    @Expose private String helptext;
    @Expose private String identifier;
    @Expose private int confidence = 0;
    @Expose private String permissionLevel;
    @Expose private String channel;
    @Expose private float voiceVolume = 0.1f;
    @Expose private String parameter;
    @Expose private boolean specialInteraction = false;
    @Expose private boolean isPermBad = false;
    private int usageCount = -1;
    private ArrayList<String> allowedUsers = new ArrayList<>();
    private boolean stopProcessing = false;
    private int confidenceChecked = 0;
    private List<IUser> mentionedUsers = new LinkedList<>();
    private boolean lock = false;
    private File interactionFile;
    private ArrayList<Tag> tagCache = new ArrayList<>();

    public Interaction(String identifier, String[] tags, String templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, new InteractionTemplate[]{new InteractionTemplate(templates, "normal", null)}, triggers, confidence, enabled, helptext);
    }

    public Interaction(String identifier, String[] tags, InteractionTemplate[] templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, templates, triggers, confidence, enabled, helptext,"", "", null, 0.0f, false);
    }

    public Interaction(String identifier, String[] tags, InteractionTemplate[] templates, String[] templatesFail, int usageCount, String userID, float voiceVolume){
        this(identifier, tags, templates, null, 0, true, "", "", "", null, voiceVolume, false);
        this.usageCount = usageCount;
        this.allowedUsers.add(userID);
    }

    @JsonCreator
    public Interaction(@JsonProperty("triggers") String[] triggers, @JsonProperty("tags") String[] tags, @JsonProperty("templatesNew") InteractionTemplate[] templatesNew, @JsonProperty("templates") String[] templates, @JsonProperty("templatesFail") String[] templatesFail,@JsonProperty("templatesPermError") String[] templatesPermError, @JsonProperty("voiceFiles") String[] voiceFiles, @JsonProperty("enabled") boolean enabled, @JsonProperty("helptext") String helptext, @JsonProperty("idenifier") String identifier, @JsonProperty("confidence") int confidence, @JsonProperty("permissionLevel") String permissionLevel, @JsonProperty("channel") String channel, @JsonProperty("voiceVolume") float voiceVolume, @JsonProperty("parameter") String parameter, @JsonProperty("specialInteraction") boolean specialInteraction) {
        this.triggers = triggers;
        this.tags = tags;
        this.templatesNew = templatesNew;
        this.templates = templates;
        this.templatesFail = templatesFail;
        this.templatesPermError = templatesPermError;
        this.voiceFiles = voiceFiles;
        this.enabled = enabled;
        this.helptext = helptext;
        this.identifier = identifier;
        this.confidence = confidence;
        this.permissionLevel = permissionLevel;
        this.channel = channel;
        this.voiceVolume = voiceVolume;
        this.parameter = parameter;
        this.specialInteraction = specialInteraction;
    }

    public Interaction(String identifier, String[] tags, InteractionTemplate[] templatesNew, String[] triggers, int confidence, boolean enabled, String helptext, String permissionLevel, String channel, String[] voiceFiles, float voiceVolume, boolean specialInteraction){
        this.specialInteraction = specialInteraction;
        this.identifier = identifier;
        this.tags = tags;
        this.templatesNew = templatesNew;
        this.confidence = confidence;
        this.triggers = triggers;
        this.enabled = enabled;
        this.helptext = helptext;
        this.permissionLevel = permissionLevel;
        this.channel = channel;
        this.voiceFiles = voiceFiles;
        this.voiceVolume = voiceVolume;
    }
    /*
    handleMessage checks which interaction type the message is and runs the respective functions.
     */
    String handleMessage(MessageReceivedEvent event){
        String result = null;
        if(!lock) {
            cleanupInteraction();
            confidenceChecked = 0;
            try {
                if (enabled) {
                    if (isAllowedUser(event.getAuthor())) {
                        if (event.getMessage().getContent().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild())) && Arrays.asList(tags).contains("prefixed")) {
                            //Get only word follow prefix
                            Pattern prefixedPattern = Pattern.compile("\\s+");
                            String[] regex = prefixedPattern.split(event.getMessage().getContent().replace(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()), ""));
                            if (regex.length > 0) {
                                confidenceChecked = getConfidence(regex[0], true, event.getGuild());
                            }
                        }
                        if (!event.getMessage().getContent().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild())) && !Arrays.asList(tags).contains("prefixed")) {
                            confidenceChecked = getConfidence(event.getMessage().getContent(), false, event.getGuild());
                        }
                        if (confidenceChecked >= this.confidence)
                            result = getRandomTemplate("normal").getTemplate();
                        if (result != null && permissionLevel != null && permissionLevel.length() > 0 && !KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getGuild(), permissionLevel)) {
                            result = getRandomTemplate("permission").getTemplate();
                            isPermBad = true;
                        }
                    }
                }
            } catch (NullPointerException e) {
                Karren.log.error("An error has occured with interaction " + this.identifier);
                e.printStackTrace();
            }
        }
        return result;
    }

    private void cleanupInteraction(){
        if(mentionedUsers==null)
            mentionedUsers = new LinkedList<>();
        mentionedUsers.clear();
        if(tagCache==null)
            tagCache = new ArrayList<>();
        tagCache.clear();
        confidenceChecked = 0;
        if(Arrays.asList(tags).contains("parameter"))
            parameter = null;
    }

    private boolean isAllowedUser(IUser user) {
        return allowedUsers == null || allowedUsers.isEmpty() || allowedUsers.contains(user.getStringID());
    }

    private int getConfidence(String message, boolean prefixed, IGuild guild){
        int confidence = 0;
        if(!prefixed){
            //Add getNicknameForGuild once it's fixed and doesn't return a null
            if(Arrays.asList(tags).contains("bot") && !message.toLowerCase().contains(Karren.bot.getClient().getOurUser().getName().toLowerCase()))
                return 0;
        }
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

    public InteractionTemplate getRandomTemplate(String type) {
        InteractionTemplate[] random = Arrays.stream(templatesNew).filter(x -> x.getTemplateType().equalsIgnoreCase(type)).toArray(InteractionTemplate[]::new);
        if(random.length>0) {
            Random rng = new Random();
            return random[rng.nextInt(random.length)];
        } else {
            Karren.log.error("Warning, interaction " + identifier + " is requesting a template for type " +type + " but none were found!");
            return new InteractionTemplate("Missing template for type " + type, type, this);
        }
    }

    public String[] getTags(){return tags;}
    public String getTagsToString(){
        StringBuilder result = new StringBuilder();
        for (String tag : tags) {
            result.append(tag).append(" ");
        }
        return result.toString();
    }
    public InteractionTemplate[] getTemplates(String type){return (InteractionTemplate[]) Arrays.stream(templatesNew).filter(x -> x.getTemplateType().equalsIgnoreCase(type)).toArray();}
    public InteractionTemplate[] getTemplates(){return getTemplates("normal");}
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

    public InteractionTemplate[] getTemplatesFail() {
        return getTemplates("fail");
    }

    public InteractionTemplate[] getTemplatesPermError() {
        return getTemplates("permission");
    }

    public String getRandomVoiceFile(){
        if(voiceFiles!=null){
            Random rng = new Random();
            return "conf/" + voiceFiles[rng.nextInt(voiceFiles.length)];
        } else {
            return null;
        }
    }

    public float getVoiceVolume() {
        return voiceVolume;
    }

    public String getPermissionLevel(){return permissionLevel;}

    public boolean isSpecialInteraction() {
        return specialInteraction;
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

    public int getConfidenceChecked() {
        return confidenceChecked;
    }

    public List<IUser> getMentionedUsers() {
        return mentionedUsers;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public File getInteractionFile() {
        return interactionFile;
    }

    public void setInteractionFile(File interactionFile) {
        this.interactionFile = interactionFile;
    }

    public ArrayList<Tag> getTagCache() {
        return tagCache;
    }

    public boolean interactionOldFormatUpdate(){
        if(templates!=null || templatesFail!=null || templatesPermError!=null){
            Karren.log.info("Upgrading format on interaction " + identifier);
            ArrayList<InteractionTemplate> newTemplates = new ArrayList<>();
            if(templates!=null)
                for(String normal : templates)
                    newTemplates.add(new InteractionTemplate(normal, "normal", this));
            if(templatesFail!=null)
                for(String fail : templatesFail)
                    newTemplates.add(new InteractionTemplate(fail, "fail", this));
            if(templatesPermError!=null)
                for(String permission : templatesPermError)
                    newTemplates.add(new InteractionTemplate(permission, "permission", this));
            templatesNew = newTemplates.toArray(new InteractionTemplate[newTemplates.size()]);
            Gson json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            try (Writer writer = new FileWriter(interactionFile)) {
                json.toJson(this, writer);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
