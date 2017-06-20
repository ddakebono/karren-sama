package org.frostbite.karren.interactions;

/**
 * Created by ddakebono on 6/20/2017.
 */
public class InteractionTemplate {
    private int templateid;
    private String template;
    private String templateType;
    private Interaction interaction;

    public InteractionTemplate(String template, String templateType, Interaction interaction){
        this.template = template;
        this.templateType = templateType;
        this.interaction = interaction;
    }

    public int getTemplateid() {
        return templateid;
    }

    public void setTemplateid(int templateid) {
        this.templateid = templateid;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }
}
