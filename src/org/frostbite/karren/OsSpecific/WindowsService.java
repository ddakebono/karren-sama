package org.frostbite.karren.OsSpecific;

import java.io.IOException;

public class WindowsService {
    private String name;
    private String ident;
    private Runtime rt = Runtime.getRuntime();
    public WindowsService(String name, String ident){
        this.name = name;
        this.ident = ident;
    }
    public boolean stop() throws IOException, InterruptedException {
        Process result = rt.exec("net stop " + name);
        Thread.sleep(5000);
        return result.exitValue() == 0;
    }
    public boolean start() throws IOException, InterruptedException {
        Process result = rt.exec("net start " + name);
        Thread.sleep(5000);
        return result.exitValue() == 0;
    }
    public boolean restart() throws InterruptedException, IOException {
        stop();
        return start();
    }
    public String getIdent(){return ident;}
    public String getName(){return name;}
}
