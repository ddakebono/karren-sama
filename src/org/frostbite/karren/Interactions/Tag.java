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

import net.dv8tion.jda.api.Permission;

public class Tag {
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result){
        return msg;
    }
    public String getTagName(){
        return "NO NAME";
    }
    public Permission[] getRequiredPermissions(){
        return new Permission[]{Permission.MESSAGE_WRITE};
    }
    public Boolean getVoiceUsed() { return false; }
}
