/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

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
