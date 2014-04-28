package org.frostbite.karren.space;

/**
 * Created by frostbite on 27/04/14.
 */
public class SpaceFaction {
    private int factionID;
    private String factionName;
    private String[] factionMembers;
    public SpaceFaction(int factionID, String factionName, String[] factionMembers){
        this.factionName = factionName;
        this.factionMembers = factionMembers;
        this.factionID = factionID;
    }
}
