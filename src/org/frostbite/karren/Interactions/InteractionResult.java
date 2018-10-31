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
import discord4j.core.spec.MessageCreateSpec;

public class InteractionResult {
    MessageCreateSpec message;
    MessageCreateEvent event;
    boolean privateMessage = false;
    String overrideChannel = null;

    public InteractionResult(MessageCreateSpec message, MessageCreateEvent event){
        new InteractionResult(message, event, false, null);
    }

    public InteractionResult(MessageCreateSpec message, MessageCreateEvent event, boolean privateMessage, String overrideChannel) {
        this.message = message;
        this.privateMessage = privateMessage;
        this.overrideChannel = overrideChannel;
    }

    public MessageCreateSpec getMessage() {
        return message;
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
}
