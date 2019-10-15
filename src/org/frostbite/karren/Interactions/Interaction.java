/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.frostbite.karren.Karren;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Expose private boolean guildOnly = false;
    private int usageCount = -1;
    private ArrayList<String> allowedUsers = new ArrayList<>();
    private ArrayList<Long> allowedServers = new ArrayList<Long>();
    private boolean stopProcessing = false;
    private transient List<User> mentionedUsers = new LinkedList<>();
    private boolean lock = false;
    private File interactionFile;
    private ArrayList<Tag> tagCache = new ArrayList<>();
    private boolean noClearInteraction = false;
    private MessageEmbed embed;
    @Expose private ArrayList<InteractionParameter> parameters;
    @Expose private ArrayList<InteractionEmbedFields> embedFields;
    @Expose private String friendlyName;
    private HashMap<String, String> replacedTextMap = new HashMap<>();
    private boolean tagAddedEmbeds = false;
    private String embedImage;
    private String embedURL;
    private String embedFooter;

    public Interaction(String identifier, String[] tags, String templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, new InteractionTemplate[]{new InteractionTemplate(templates, "normal", null)}, triggers, confidence, enabled, helptext);
    }

    public Interaction(String identifier, String[] tags, InteractionTemplate[] templates, String[] triggers, int confidence, boolean enabled, String helptext){
        this(identifier ,tags, templates, triggers, confidence, enabled, helptext,"", "", null, 0.0f, false);
    }

    public Interaction(String identifier, String[] tags, InteractionTemplate[] templates, int usageCount, String userID, float voiceVolume){
        this(identifier, tags, templates, null, 0, true, "", "", "", null, voiceVolume, false);
        this.usageCount = usageCount;
        this.allowedUsers.add(userID);
    }

    @JsonCreator
    public Interaction(@JsonProperty("triggers") String[] triggers, @JsonProperty("tags") String[] tags, @JsonProperty("templatesNew") InteractionTemplate[] templatesNew, @JsonProperty("templates") String[] templates, @JsonProperty("templatesFail") String[] templatesFail, @JsonProperty("templatesPermError") String[] templatesPermError, @JsonProperty("voiceFiles") String[] voiceFiles, @JsonProperty("enabled") boolean enabled, @JsonProperty("helptext") String helptext, @JsonProperty("idenifier") String identifier, @JsonProperty("confidence") int confidence, @JsonProperty("permissionLevel") String permissionLevel, @JsonProperty("channel") String channel, @JsonProperty("voiceVolume") float voiceVolume, @JsonProperty("parameter") String parameter, @JsonProperty("specialInteraction") boolean specialInteraction, @JsonProperty("embedFields") ArrayList<InteractionEmbedFields> embedFields, @JsonProperty("friendlyName") String friendlyName, @JsonProperty("allowedServers") Long[] allowedServers, @JsonProperty("guildOnly") boolean guildOnly) {
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
        this.embedFields = embedFields;
        this.friendlyName = friendlyName;
        this.allowedServers.addAll(Arrays.asList(allowedServers));
        this.guildOnly = guildOnly;
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

    public boolean checkTriggers(MessageReceivedEvent event){
        if(!lock) {
            cleanupInteraction();
            if(!isAllowedServer(event.getGuild()))
                return false;
            int confidenceChecked = 0;
            if (isAllowedUser(event.getAuthor()) && enabled && !event.getMessage().getContentRaw().isEmpty()) {
                Guild guild = event.getGuild();
                if (Arrays.asList(tags).contains("prefixed")) {
                    if (event.getMessage().getContentRaw().startsWith(Karren.bot.getGuildManager().getCommandPrefix(guild))) {
                        Pattern prefixedPattern = Pattern.compile("\\s+");
                        String[] regex = prefixedPattern.split(event.getMessage().getContentRaw().replace(Karren.bot.getGuildManager().getCommandPrefix(guild), ""));
                        if (regex.length > 0) {
                            confidenceChecked = getConfidence(regex[0], true, guild);
                        }
                    }
                } else {
                    //Non prefixed interaction
                    confidenceChecked = getConfidence(event.getMessage().getContentRaw(), false, guild);
                }
            }
            return confidenceChecked >= confidence;
        }
        return false;
    }

    public String getInitialTemplate(MessageReceivedEvent event){
        if(permissionLevel!=null && !permissionLevel.isEmpty()){
            List<Role> rolesMatch = null;
            //get roles
            if(event.getMember()!=null)
                rolesMatch = event.getMember().getRoles().stream().filter(x -> x.getName().equalsIgnoreCase(permissionLevel)).collect(Collectors.toList());
            if(rolesMatch!=null && rolesMatch.size()>0)
                return getRandomTemplate("normal").getTemplate();
            else
                return getRandomTemplate("permission").getTemplate();
        }
        return getRandomTemplate("normal").getTemplate();
    }

    private void cleanupInteraction(){
        stopProcessing = false;
        if(!noClearInteraction) {
            if (mentionedUsers == null)
                mentionedUsers = new LinkedList<>();
            mentionedUsers.clear();
            if(replacedTextMap==null)
                replacedTextMap = new HashMap<>();
            replacedTextMap.clear();
            if(tagAddedEmbeds)
                embedFields.clear();
            tagAddedEmbeds = false;
            embed = null;
            embedImage = null;
            embedURL = null;
            if (tagCache == null)
                tagCache = new ArrayList<>();
            tagCache.clear();
            if (Arrays.asList(tags).contains("parameter"))
                parameter = null;
        }
    }

    private boolean isAllowedUser(User user) {
        return allowedUsers == null || allowedUsers.isEmpty() || allowedUsers.contains(user.getId());
    }

    private boolean isAllowedServer(Guild guild) {
        return allowedServers == null || allowedServers.isEmpty() || allowedServers.contains(guild.getIdLong());
    }

    private int getConfidence(String message, boolean prefixed, Guild guild){
        int confidence = 0;
        Member selfMember = null;
        if(!prefixed){
            //Check for bot username, and server specific nicknames
            if(guild!=null)
                selfMember = guild.getMember(Karren.bot.client.getSelfUser());
            if(selfMember!=null){
                if(Arrays.asList(tags).contains("bot") && !message.toLowerCase().contains(Karren.bot.client.getSelfUser().getName().toLowerCase()) && !message.toLowerCase().contains(selfMember.getEffectiveName().toLowerCase()))
                    return 0;
            }else {
                if (Arrays.asList(tags).contains("bot") && !message.toLowerCase().contains(Karren.bot.client.getSelfUser().getName()))
                    return 0;
            }
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

    public String replaceMsg(String msg, String target, String value){
        replacedTextMap.put(target, value);
        return msg.replace(target, value);
    }

    public String getReplacementText(String target){
        for(String word : replacedTextMap.keySet()){
            if(target.toLowerCase().contains(word.toLowerCase())){
                return target.replaceAll(word, replacedTextMap.get(word));
            }
        }
        return target;
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

    public boolean isEmbedUsed(){
        return embed!=null;
    }

    public MessageEmbed getEmbed(){
        return embed;
    }

    public void setEmbed(MessageEmbed embed){
        this.embed = embed;
    }

    public boolean isStopProcessing() {
        return stopProcessing;
    }

    public void stopProcessing() {
        this.stopProcessing = true;
    }

    public List<User> getMentionedUsers() {
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

    public ArrayList<InteractionEmbedFields> getEmbedFields() {
        return embedFields;
    }

    public void setEmbedImage(String image){
        this.embedImage = image;
    }

    public String getEmbedImage() {
        return embedImage;
    }

    public void addEmbedField(InteractionEmbedFields field){
        if(embedFields==null)
            embedFields = new ArrayList<>();
        embedFields.add(field);
        tagAddedEmbeds = true;
    }

    public String getEmbedURL() {
        return embedURL;
    }

    public void setEmbedURL(String embedURL) {
        this.embedURL = embedURL;
    }

    public String getEmbedFooter() {
        return embedFooter;
    }

    public void setEmbedFooter(String embedFooter) {
        this.embedFooter = embedFooter;
    }

    public boolean isTagAddedEmbeds() {
        return tagAddedEmbeds;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public int getReplacementTextCount(){
        return replacedTextMap.size();
    }

    public boolean isGuildOnly() {
        return guildOnly;
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
            templatesNew = newTemplates.toArray(new InteractionTemplate[0]);
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

    public void setNoClearInteraction(boolean noClearInteraction) {
        this.noClearInteraction = noClearInteraction;
    }
}
