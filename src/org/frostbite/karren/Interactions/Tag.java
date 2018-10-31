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

import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;

public class Tag {
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result){
        return msg;
    }
    public String getTagName(){
        return "NO NAME";
    }
    public PermissionSet getRequiredPermissions(){
        return PermissionSet.of(Permission.SEND_MESSAGES);
    }
    public Boolean getVoiceUsed() { return false; }
}
