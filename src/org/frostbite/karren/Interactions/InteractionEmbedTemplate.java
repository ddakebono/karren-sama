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

public class InteractionEmbedTemplate {
    private InteractionEmbedFields[] embedFields;
    private String templateType;

    public InteractionEmbedTemplate(InteractionEmbedFields[] embedFields, String templateType) {
        this.embedFields = embedFields;
        this.templateType = templateType;
    }

    public InteractionEmbedFields[] getEmbedFields() {
        return embedFields;
    }

    public void setEmbedFields(InteractionEmbedFields[] embedFields) {
        this.embedFields = embedFields;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}
