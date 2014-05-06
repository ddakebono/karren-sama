package org.frostbite.karren.space;

/**
 * Created by frostbite on 06/05/14.
 */
public class SpaceUser {
    private String nick;
    private int userID;
    private int factionID;
    public SpaceUser(String nick, int userID, int factionID){
        this.nick = nick;
        this.userID = userID;
        this.factionID = factionID;
    }
    public String getNick(){return nick;}
    public int getUserID(){return userID;}
    public int getFactionID(){return factionID;}
    public void setFactionID(int factionID){this.factionID = factionID;}
}
