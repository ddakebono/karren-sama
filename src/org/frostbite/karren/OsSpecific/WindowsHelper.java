package org.frostbite.karren.OsSpecific;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by frostbite on 09/06/14.
 */
public class WindowsHelper {
    public interface Shell32 extends StdCallLibrary{
        boolean IsUserAnAdmin() throws LastErrorException;
    }
    public boolean isSystemWindows(){
        String check = System.getProperty("os.name");
        return check.contains("Windows");
    }
    public boolean checkIfElevated(){
        Shell32 INSTANCE = (Shell32) Native.loadLibrary("shell32", Shell32.class);
        return INSTANCE.IsUserAnAdmin();
    }
    public void elevateApplication() throws IOException {
        Runtime rt = Runtime.getRuntime();
        File execPath = new File(WindowsHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String path = execPath.getPath();
        path = URLDecoder.decode(path, "UTF-8");
        System.out.println("Trying to elevate bot to higher permissions...");
        rt.exec("elevate.exe java -jar " + path + execPath.getName());
    }
}
