package org.frostbite.karren.OsSpecific;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.io.*;

public class OsDiscovery {
    public interface Shell32 extends StdCallLibrary{
        boolean IsUserAnAdmin() throws LastErrorException;
    }
    public String getSystemType(){
        String check = System.getProperty("os.name");
        String result = "Unknown";
        if(check.contains("nux")){
            result = "Linux";
        } else if(check.contains("windows")){
            result = "Windows";
        }
        return result;
    }
    public boolean checkIfInitd() throws IOException {
        Runtime run = Runtime.getRuntime();
        Process result = run.exec("ls /etc/init.d/");
        BufferedReader in = new BufferedReader(new InputStreamReader(result.getInputStream()));
        BufferedReader err = new BufferedReader(new InputStreamReader(result.getErrorStream()));
        boolean isInitd = false;
        int resCount = 0;
        int errCount = 0;
        while((in.readLine()) != null){
            resCount++;
        }
        while((err.readLine()) != null){
            errCount++;
        }
        if(resCount>1){
            isInitd = true;
        }
        if(errCount>0){
            isInitd = false;
        }
        return isInitd;
    }
    public boolean checkIfElevated(){
        Shell32 INSTANCE = (Shell32) Native.loadLibrary("shell32", Shell32.class);
        return INSTANCE.IsUserAnAdmin();
    }
}
