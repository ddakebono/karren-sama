/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class KarrenUtil {

    public static final String[] heroList = {"Reinhardt", "Tracer", "Zenyatta", "Junkrat", "Mccree", "Winston", "Orisa", "Hanzo", "Pharah", "Roadhog", "Zarya", "Torbjorn", "Mercy", "Soldier76", "Ana", "Widowmaker", "Genji", "Reaper", "Mei", "Bastion", "Symmetra", "Dva", "Sombra", "Lucio"};
    public static final TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null; // Not relevant.
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }
            }
    };

    @SuppressWarnings("ConstantConditions")
    public static File[] getFilesInFolders(File directory){
        ArrayList<File> files = new ArrayList<>();
        if(directory.isDirectory()){
            for(File file : directory.listFiles()){
                if(file.isDirectory())
                    Collections.addAll(files, getFilesInFolders(file));
                else
                    files.add(file);
            }
            return files.toArray(new File[files.size()]);
        } else {
            return files.toArray(new File[files.size()]);
        }
    }

    public static boolean hasRole(IUser user, IGuild guild, String roleName) {
        return guild == null || roleName == null || user.getRolesForGuild(guild).stream().anyMatch(x -> x.getName().equals(roleName));
    }

    public static String calcAway(long leaveDate){
        String backTime;
        long diffTime;
        long seconds;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        Date date = new Date();
        diffTime = date.getTime()-leaveDate;
        seconds = diffTime/1000;
        if(seconds>=60){
            minutes = seconds/60;
            seconds = seconds-(minutes*60);
        }
        if(minutes>=60){
            hours = minutes/60;
            minutes = minutes-(hours*60);
        }
        if(hours>=24){
            days = hours/24;
            hours = hours-(days*24);
        }
        backTime = days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
        return backTime;
    }

    public static String getMinSecFormattedString(long time){
        String result;
        long seconds = time/1000;
        long minutes = 0;
        if(seconds/60>=1) {
            minutes = seconds / 60;
            seconds = seconds - (minutes*60);
        }
        if(time!=0 && seconds<10) {
            result = minutes + ":" + "0" + seconds;
        } else {
            result = minutes + ":" + seconds;
        }
        return result;
    }
}
