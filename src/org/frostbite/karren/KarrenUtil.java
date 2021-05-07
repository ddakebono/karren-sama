/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import java.io.File;
import java.util.*;

public class KarrenUtil {

    public static <T> T instantiate(final String className, final Class<T> type){
        try{
            return type.cast(Class.forName(className).newInstance());
        } catch(InstantiationException
                | IllegalAccessException
                | ClassNotFoundException | NoClassDefFoundError e){
            e.printStackTrace();
            Karren.log.error("An interaction requested a tag that doesn't exist! " + className + " Please check to ensure that the interactions tags are spelt correctly, and are implemented.");
            return null;
        }
    }

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
            return files.toArray(new File[0]);
        } else {
            return files.toArray(new File[0]);
        }
    }

    public static String getVRCSafetySystemRank(List<String> tags){
        LinkedHashMap<String, String> permMap = new LinkedHashMap<>();
        permMap.put("system_trust_basic", "New User");
        permMap.put("system_trust_known", "User");
        permMap.put("system_trust_trusted", "Known User");
        permMap.put("system_trust_veteran", "Trusted User");
        permMap.put("system_trust_legend", "Veteran user");
        permMap.put("system_legend", "Legendary User");
        permMap.put("system_probable_troll", "Near Nuisance");
        permMap.put("system_troll", "Nuisance");
        permMap.put("admin_moderator", "Game Moderator");
        permMap.put("system_notamod", "It's Tupper");
        String permission = "Visitor (No Tags)";
        int permLevel = -1;
        for(String tag : tags){
            String level = permMap.getOrDefault(tag, null);
            if(level!=null){
                int index = new ArrayList<>(permMap.values()).indexOf(level);
                if(index>permLevel){
                    permission = level;
                    permLevel = index;
                }
            }
        }
        return permission;
    }

    /*public static boolean hasRole(IUser user, IGuild guild, String roleName) {
        return guild == null || roleName == null || user.getRolesForGuild(guild).stream().anyMatch(x -> x.getName().equals(roleName));
    }*/

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

    public static String calcTimeDiff(long diff1, long diff2){
        String backTime;
        long diffTime;
        long seconds;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        Date date = new Date();
        diffTime = diff1 - diff2;
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
        backTime = days + " Days, " + hours + " Hr, " + minutes + " Min, " + seconds + " Sec";
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
