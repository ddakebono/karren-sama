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

public class InteractionParameter {

    @Expose public String name; //Name of the parameter, will be used for variable and template replacement)
    @Expose public String prefix = ""; //Allows the use of many parameters
    @Expose public String description = "";
    @Expose public String value = ""; //If an interaction uses a fixed value
    @Expose public String validation = "";
    @Expose public boolean optional = false; //Sets if a parameter can be skipped in the command
    @Expose public boolean enabled = true; //Sets if parameter is available

    public InteractionParameter(String name, String prefix, String value, boolean optional, boolean enabled, String description) {
        this.name = name;
        this.prefix = prefix;
        this.value = value;
        this.optional = optional;
        this.enabled = enabled;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
