package org.frostbite.karren.space;

/**
 * Created by frostbite on 27/04/14.
 */
public class SpaceFaction {
    private int factionID;
    private String factionName;
    public SpaceFaction(int factionID, String factionName){
        this.factionName = factionName;
        this.factionID = factionID;
    }

    public int getFactionID() {
        return factionID;
    }
    public String getFactionName() {
        return factionName;
    }
}
