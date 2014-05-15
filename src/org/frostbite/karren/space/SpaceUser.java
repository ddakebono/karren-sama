package org.frostbite.karren.space;

/**
 * Created by frostbite on 06/05/14.
 */
public class SpaceUser {
    private String nick;
    private int userID;
    private int factionID;
    private String email;
    public SpaceUser(String nick, int userID, int factionID, String email){
        this.nick = nick;
        this.userID = userID;
        this.factionID = factionID;
        this.email = email;
    }
    public String getNick(){return nick;}
    public int getUserID(){return userID;}
    public int getFactionID(){return factionID;}
    public String getEmail(){return email;}
    public void updateEmail(String email){this.email = email;}
    public void setFactionID(int factionID){this.factionID = factionID;}
}
