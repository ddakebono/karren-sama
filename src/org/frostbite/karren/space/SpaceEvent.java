package org.frostbite.karren.space;

/**
 * Created by frostbite on 27/04/14.
 */
public class SpaceEvent {
    private int eventID;
    private String eventData;
    private long startDate;
    private long endDate;
    private String eventIdent;
    private boolean hasBegun;
    public SpaceEvent(int id, String data, long startDate, long endDate, String eventIdent){
        this.eventID = id;
        this.eventData = data;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventIdent = eventIdent;
        hasBegun = false;
    }
    public String getEventIdent(){return eventIdent;}
    public long getEventDuration(){
        return ((endDate-startDate)/1000)/60;
    }
    public int getEventID(){return eventID;}
    public String getEventData(){return eventData;}
    public long getStartDate(){return startDate;}
    public long getEndDate(){return endDate;}
    public boolean hasEventBegun(){return hasBegun;}
    public String getEventDataFormatted(){
        hasBegun = true;
        String[] eventComponents = eventData.split(":");
        String result = "Event: ";
        for(String component : eventComponents){
            switch (component){
                case "war":
                    result += "War between the factions";
            }
        }
        return result;
    }
}
