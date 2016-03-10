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
