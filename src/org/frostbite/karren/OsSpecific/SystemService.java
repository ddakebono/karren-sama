package org.frostbite.karren.OsSpecific;

import java.io.IOException;

public class SystemService {
    private String name;
    private String ident;
    private Runtime rt = Runtime.getRuntime();
    public SystemService(String name, String ident){
        this.name = name;
        this.ident = ident;
    }
    public boolean winStop() throws IOException, InterruptedException {
        Process result = rt.exec("net stop " + name);
        result.waitFor();
        return result.exitValue() == 0;
    }
    public boolean winStart() throws IOException, InterruptedException {
        Process result = rt.exec("net start " + name);
        result.waitFor();
        return result.exitValue() == 0;
    }
    public boolean winRestart() throws InterruptedException, IOException {
        winStop();
        return winStart();
    }
    public boolean linStop() throws IOException, InterruptedException {
        Process result = rt.exec("sudo /etc/init.d/" + name + " stop");
        result.waitFor();
        return result.exitValue() == 0;
    }
    public boolean linStart() throws IOException, InterruptedException {
        Process result = rt.exec("sudo /etc/init.d/" + name + " start");
        result.waitFor();
        return result.exitValue() == 0;
    }
    public boolean linRestart() throws IOException, InterruptedException {
        linStop();
        return linStart();
    }
    public String getIdent(){return ident;}
    public String getName(){return name;}
}
