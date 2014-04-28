package org.frostbite.karren.space;

/**
 * Created by frostbite on 27/04/14.
 */
public class SpaceEvent {
    private int eventID;
    private String eventData;
    private long startDate;
    private long endDate;
    public SpaceEvent(int id, String data, long startDate, long endDate){
        this.eventID = id;
        this.eventData = data;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public long getEventDuration(){
        return ((endDate-startDate)/1000)/60;
    }
    public int getEventID(){return eventID;}
    public String getEventData(){return eventData;}
    public long getStartDate(){return startDate;}
    public long getEndDate(){return endDate;}
}
