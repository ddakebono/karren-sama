/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.function.Consumer;

public class InteractionResult {
    String message;
    Consumer<EmbedCreateSpec> embedConsumer;
    MessageCreateEvent event;
    boolean privateMessage = false;
    String overrideChannel = null;
    boolean errored = false;
    boolean doNotSend = false;

    public InteractionResult(MessageCreateEvent event){
        new InteractionResult(event, false, null);
    }

    public InteractionResult(MessageCreateEvent event, boolean privateMessage, String overrideChannel) {
        this.event = event;
        this.privateMessage = privateMessage;
        this.overrideChannel = overrideChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Consumer<EmbedCreateSpec> getEmbedConsumer() {
        return embedConsumer;
    }

    public void setEmbedConsumer(Consumer<EmbedCreateSpec> embedConsumer) {
        this.embedConsumer = embedConsumer;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    public String getOverrideChannel() {
        return overrideChannel;
    }

    public MessageCreateEvent getEvent() {
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
