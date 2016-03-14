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

public class StaticData {
    public enum ServiceStatus {
        ONLINE, OFFLINE, RESTARTING, MAINT
    }
    public enum SystemType {
        WINDOWS, LINUX, OTHER
    }
    public enum SendCommands {
        COMMAND, RESTART, STOP, START, STATUS, KILLEXT, EXTSYSINFO
    }
    public enum RecvCommands{
        JSON, BOOL, UNK
    }
}
