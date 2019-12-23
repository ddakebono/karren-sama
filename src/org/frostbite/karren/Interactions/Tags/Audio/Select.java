/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Audio;

import com.google.api.services.youtube.model.Video;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

import java.util.List;

public class Select extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        String param = interaction.getParameter();
        int selection = 0;
        try {
            selection = Integer.parseInt(param);
        } catch (NumberFormatException ignored){}
        if(selection>0 && selection<=3) {
            Search search = (Search) interaction.getNoProcessTagCache().get(0);
            List<Video> resultArray = search.getResultArray(result.getEvent().getAuthor().getId());
            interaction.setParameter(resultArray.get(selection-1).getId());
            msg = interaction.replaceMsg(msg,"%title", resultArray.get(selection - 1).getSnippet().getTitle());
        } else if(param.trim().equalsIgnoreCase("c")){
            msg = "Alright, I've deleted the results.";
            interaction.stopProcessing();
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
            interaction.addUsageCount();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jselect";
    }
}
