package org.frostbite.karren.InterConnect;

public class System {

    int ramTotal;
    int ramFree;
    int cpuUsage;

    StaticData.SystemType type;

    public System(int ramFree, int ramTotal, int cpuUsage, StaticData.SystemType type){
        this.ramFree = ramFree;
        this.ramTotal = ramTotal;
        this.cpuUsage = cpuUsage;
        this.type = type;
    }


}
