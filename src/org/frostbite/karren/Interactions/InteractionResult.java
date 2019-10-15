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

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InteractionResult {
    String message;
    MessageEmbed embed;
    MessageReceivedEvent event;
    boolean privateMessage = false;
    String overrideChannel = null;
    boolean errored = false;
    boolean doNotSend = false;
    boolean completed = false;

    public InteractionResult(MessageReceivedEvent event){
        new InteractionResult(event, false, null);
    }

    public InteractionResult(MessageReceivedEvent event, boolean privateMessage, String overrideChannel) {
        this.event = event;
        this.privateMessage = privateMessage;
        this.overrideChannel = overrideChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        completed = true;
    }

    public MessageEmbed getEmbed() {
        return embed;
    }

    public void setEmbed(MessageEmbed embedConsumer) {
        this.embed = embedConsumer;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    public String getOverrideChannel() {
        return overrideChannel;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setPrivateMessage(boolean privateMessage) {
        this.privateMessage = privateMessage;
    }

    public void setOverrideChannel(String overrideChannel) {
        this.overrideChannel = overrideChannel;
    }

    public boolean isErrored() {
        return errored;
    }

    public void setErrored(boolean errored) {
        this.errored = errored;
    }

    public boolean isDoNotSend() {
        return doNotSend;
    }

    public void setDoNotSend(boolean doNotSend) {
        this.doNotSend = doNotSend;
    }
}
