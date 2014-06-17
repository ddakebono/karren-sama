package org.frostbite.karren.OsSpecific;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

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
}
