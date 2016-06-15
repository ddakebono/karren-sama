/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.TagObjects;

public class HeroSearchResults {
    private String[] entries;

    public HeroSearchResults(String[] entries){
        this.entries = entries;
    }

    public String[] getEntries(){
        return entries;
    }

    public boolean hasEntries(){
        return entries.length > 0;
    }

    public String getSingleEntry(){
        if(hasEntries())
            return entries[0];
        return null;
    }
}
