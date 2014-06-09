package org.frostbite.karren.OsSpecific;

import java.io.IOException;

/**
 * Created by frostbite on 09/06/14.
 */
public class WindowsService {
    private String name;
    private String ident;
    private Runtime rt = Runtime.getRuntime();
    public WindowsService(String name, String ident){
        this.name = name;
        this.ident = ident;
    }
    public boolean stop() throws IOException {
        Process result = rt.exec("sc stop " + name);
        if(result.exitValue() == 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean start() throws IOException {
        Process result = rt.exec("sc start " + name);
        if(result.exitValue() == 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean restart() throws InterruptedException, IOException {
        stop();
        Thread.sleep(5000);
        return start();
    }
    public String getIdent(){return ident;}
    public String getName(){return name;}
}
