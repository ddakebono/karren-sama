/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions;

import com.google.gson.annotations.Expose;

public class InteractionEmbedFields {
    private int embedTemplateID;
    @Expose private String fieldTitle;
    @Expose private String fieldValue;
    @Expose private boolean inline;
    private Interaction interaction;

    public InteractionEmbedFields(int embedTemplateID, String fieldTitle, String fieldValue, boolean inline) {
        this.embedTemplateID = embedTemplateID;
        this.fieldTitle = fieldTitle;
        this.fieldValue = fieldValue;
        this.inline = inline;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }
}
